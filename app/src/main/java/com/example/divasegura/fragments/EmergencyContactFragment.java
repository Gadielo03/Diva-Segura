package com.example.divasegura.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.divasegura.R;
import com.example.divasegura.activities.WelcomeActivity;
import com.example.divasegura.controladores.ContactoController;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.modelos.Usuario;

public class EmergencyContactFragment extends Fragment {
    private EditText etNombre, etTelefono, etParentesco;
    private Button btnNext;
    private int contactNumber;
    private UsuariosController usuariosController;
    private ContactoController contactoController;
    Usuario usuario;

    public EmergencyContactFragment(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_emergency_contact, container, false);

        etNombre = view.findViewById(R.id.etNombreContacto);
        etTelefono = view.findViewById(R.id.etTelefonoContacto);
        etParentesco = view.findViewById(R.id.etParentescoContacto);
        btnNext = view.findViewById(R.id.btnNext);

        usuariosController = new UsuariosController(this.getContext());
        contactoController = new ContactoController(this.getContext());
        usuariosController.open();
        contactoController.open();

        usuario = usuariosController.obtenerUsuarioUnico();
        System.out.println(usuario.getNombre());

        btnNext.setOnClickListener(v -> {
            if(!validateInputFields()) {
                return;
            }
            if(!saveContactInfo()) {
                return;
            }

            ((WelcomeActivity) getActivity()).navigateToPage(contactNumber + 1);
        });

        return view;
    }

    public boolean validateInputFields() {

        if (etNombre.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty() || etParentesco.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTelefono.getText().toString().length() != 10) {
            Toast.makeText(getActivity(), "Los números deben tener 10 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean saveContactInfo() {
        String nombre = etNombre.getText().toString();
        String telefono = etTelefono.getText().toString();
        String parentesco = etParentesco.getText().toString();

        long result = contactoController.insertarContacto(usuario.getId(), nombre, telefono, parentesco, contactNumber);
        if (result != -1) {
            Toast.makeText(getActivity(), "Contacto guardado correctamente", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            Toast.makeText(getActivity(), "Error al guardar el contacto", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
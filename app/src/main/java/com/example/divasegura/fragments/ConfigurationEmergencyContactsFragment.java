package com.example.divasegura.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.divasegura.R;
import com.example.divasegura.controladores.ContactoController;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.modelos.Contacto;
import com.example.divasegura.modelos.Usuario;

public class ConfigurationEmergencyContactsFragment extends Fragment {
    EditText etNombreContacto1,etNumeroContacto1,etParentescoContacto1,
    etNombreContacto2,etNumeroContacto2,etParentescoContacto2;

    Button btnGuardarContactos;

    UsuariosController usuariosController;
    ContactoController contactoController;
    Usuario usuario;
    Contacto contacto1, contacto2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuration_emergency_contacts, container, false);

        usuariosController = new UsuariosController(this.getContext());
        contactoController = new ContactoController(this.getContext());
        usuariosController.open();
        contactoController.open();

        usuario = usuariosController.obtenerUsuarioUnico();
        contacto1 = contactoController.obtenerContactoPorTipo(usuario.getId(), 1);
        contacto2 = contactoController.obtenerContactoPorTipo(usuario.getId(), 2);

        etNombreContacto1 = view.findViewById(R.id.etNombreContacto1Config);
        etNumeroContacto1 = view.findViewById(R.id.etTelefonoContacto1Config);
        etParentescoContacto1 = view.findViewById(R.id.etParentescoContacto1Config);

        etNombreContacto2 = view.findViewById(R.id.etNombreContacto2Config);
        etNumeroContacto2 = view.findViewById(R.id.etTelefonoContacto2Config);
        etParentescoContacto2 = view.findViewById(R.id.etParentescoContacto2Config); // Fixed

        asignarValores();

        btnGuardarContactos = view.findViewById(R.id.btnGuardarCambiosContactosConfig);

        btnGuardarContactos.setOnClickListener(v -> {
            if (!validarCampos())
                return;

            if (guardarValoresNuevos()) {
                // Mostrar mensaje de éxito
                Toast.makeText(getContext(), "Contactos guardados correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Error al guardar contactos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public boolean validarCampos(){
        if(etNombreContacto1.getText().toString().isEmpty()){
            etNombreContacto1.setError("Campo requerido");
            return false;
        }
        if(etNumeroContacto1.getText().toString().isEmpty()){
            etNumeroContacto1.setError("Campo requerido");
            return false;
        }
        if(etParentescoContacto1.getText().toString().isEmpty()){
            etParentescoContacto1.setError("Campo requerido");
            return false;
        }
        if(etNombreContacto2.getText().toString().isEmpty()){
            etNombreContacto2.setError("Campo requerido");
            return false;
        }
        if(etNumeroContacto2.getText().toString().isEmpty()){
            etNumeroContacto2.setError("Campo requerido");
            return false;
        }
        if(etParentescoContacto2.getText().toString().isEmpty()){
            etParentescoContacto2.setError("Campo requerido");
            return false;
        }

        return true;
    }

    public void asignarValores(){
        if(contacto1 != null){
            etNombreContacto1.setText(contacto1.getNombre());
            etNumeroContacto1.setText(contacto1.getNumero());
            etParentescoContacto1.setText(contacto1.getRelacion());
        }
        if(contacto2 != null){
            etNombreContacto2.setText(contacto2.getNombre());
            etNumeroContacto2.setText(contacto2.getNumero());
            etParentescoContacto2.setText(contacto2.getRelacion());
        }
    }

    public boolean guardarValoresNuevos(){
            contacto1.setNombre(etNombreContacto1.getText().toString());
            contacto1.setNumero(etNumeroContacto1.getText().toString());
            contacto1.setRelacion(etParentescoContacto1.getText().toString());

            contacto2.setNombre(etNombreContacto2.getText().toString());
            contacto2.setNumero(etNumeroContacto2.getText().toString());
            contacto2.setRelacion(etParentescoContacto2.getText().toString());

            int res1 = contactoController.actualizarContacto(contacto1.getUsuarioId(),contacto1.getNombre(),contacto1.getNumero(),contacto1.getRelacion());

            int res2 = contactoController.actualizarContacto(contacto2.getUsuarioId(), contacto2.getNombre(), contacto2.getNumero(), contacto2.getRelacion());

        return res1 != -1 && res2 != -1 ? true : false;

    }
}
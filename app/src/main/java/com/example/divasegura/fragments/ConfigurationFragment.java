package com.example.divasegura.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.divasegura.R;
import com.example.divasegura.controladores.UsuariosController;
import com.example.divasegura.modelos.Usuario;
import com.example.divasegura.utils.AppPreferences;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConfigurationFragment extends Fragment {

    ImageView ivPreviewPhoto;
    EditText etNumeroTelefono,etDomicilio;
    SwitchCompat swTomarFoto;

    Button btnGuardarCambios,btnCambiarFoto;
    UsuariosController usuariosController;
    Usuario usuario;

    String newPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        // Inflate the layout for this fragment

        ivPreviewPhoto = view.findViewById(R.id.ivPreviewPhotoConfig);
        etNumeroTelefono = view.findViewById(R.id.etTelefonoUsuarioConfig);
        etDomicilio = view.findViewById(R.id.etDomicilioUsuarioConfig);
        swTomarFoto = view.findViewById(R.id.stTomarFotoAutomaticoConfig);
        btnGuardarCambios = view.findViewById(R.id.btnGuardarCambiosUsuarioConfig);
        btnCambiarFoto = view.findViewById(R.id.btnChangePhotoConfig);

        usuariosController = new UsuariosController(this.getContext());
        usuariosController.open();
        // Cargar los datos del usuario en los EditText
        usuario = usuariosController.obtenerUsuarioUnico();
        if (usuario != null) {
            etNumeroTelefono.setText(usuario.getNumero());
            etDomicilio.setText(usuario.getDomicilio());
            swTomarFoto.setChecked(AppPreferences.isAutomaticTakePictureEnabled(this.getContext()));

            // Cargar la foto del usuario en el ImageView
            if (usuario.getRutaFoto() != null) {
                mostrarFoto(usuario.getRutaFoto());
            }
        }

        btnGuardarCambios.setOnClickListener(v ->{
            if(!validarCampos())
                Toast.makeText(getContext(), "No se Aceptan Campos Vacios", Toast.LENGTH_SHORT).show();
            if(guardarCambios())
                 Toast.makeText(getContext(), "Cambios guardados", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getContext(), "Error al guardar los cambios", Toast.LENGTH_SHORT).show();
        });

        btnCambiarFoto.setOnClickListener(v ->{
            checkCameraPermission();
        });

        return view;
    }

    public boolean validarCampos() {
        String telefono = etNumeroTelefono.getText().toString();
        String domicilio = etDomicilio.getText().toString();

        if (telefono.isEmpty()) {
            etNumeroTelefono.setError("Campo requerido");
            return false;
        }
        if (domicilio.isEmpty()) {
            etDomicilio.setError("Campo requerido");
            return false;
        }
        return true;
    }

    public boolean guardarCambios() {
        String telefono = etNumeroTelefono.getText().toString();
        String domicilio = etDomicilio.getText().toString();

        if (telefono.isEmpty() || domicilio.isEmpty()) {
            return false;
        }

        // Actualizar el usuario en la base de datos
        usuario.setNumero(telefono);
        usuario.setDomicilio(domicilio);
        if (newPhotoPath != null) {
            usuario.setRutaFoto(newPhotoPath);
        }
        usuariosController.actualizarUsuario(usuario.getId(), usuario.getNombre(), usuario.getNumero(), usuario.getDomicilio(), usuario.getRutaFoto());
        AppPreferences.setAutomaticTakePictureEnabled(this.getContext(),swTomarFoto.isChecked());

        return true;
    }
    private void mostrarFoto(String imagePath) {
        if (imagePath == null) {
            ivPreviewPhoto.setImageResource(R.drawable.ic_launcher_background); // Imagen por defecto
            return;
        }

        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            // Usa FileProvider para evitar problemas de permisos en Android 7+
            Uri photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    requireContext().getPackageName() + ".provider",
                    imgFile
            );

            ivPreviewPhoto.setImageURI(photoUri);
        } else {
            ivPreviewPhoto.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            dispatchTakePictureIntent();
        }
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(requireContext(), "Error al crear el archivo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        requireContext().getPackageName() + ".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        newPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // La foto se tomó correctamente, ahora cargarla en el ImageView
            mostrarFoto(newPhotoPath);
        }
    }
}
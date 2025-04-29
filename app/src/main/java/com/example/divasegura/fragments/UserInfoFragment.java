package com.example.divasegura.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.divasegura.R;
import com.example.divasegura.activities.WelcomeActivity;
import com.example.divasegura.controladores.CRUDHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInfoFragment extends Fragment {
    EditText etTelefono, etNombre, etDomicilio;
    Button btnNext, btnTakePhoto;
    ImageView ivPreviewPhoto;
    boolean isPhotoTaken = false;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private CRUDHelper crudHelper;

    String currentPhotoPath;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        crudHelper = new CRUDHelper(getContext());
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        etTelefono = view.findViewById(R.id.etTelefonoUsuario);
        etNombre = view.findViewById(R.id.etNombreUsuario);
        etDomicilio = view.findViewById(R.id.etDomicilioUsuario);
        btnNext = view.findViewById(R.id.btnNext1);
        btnTakePhoto = view.findViewById(R.id.btnTakePhoto);
        ivPreviewPhoto = view.findViewById(R.id.ivPreviewPhoto);

        btnNext.setOnClickListener(v -> {
            if (validateInputFields()) {
                ((WelcomeActivity) getActivity()).navigateToPage(1);
            }

            // Save user info to database
            // TODO

        });

        btnTakePhoto.setOnClickListener(v -> {
            checkCameraPermission();
        });

        return view;
    }

    public boolean validateInputFields() {
        if (etNombre.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty() || etDomicilio.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (etTelefono.getText().toString().length() != 10) {
            Toast.makeText(getActivity(), "Los números deben tener 10 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isPhotoTaken) {
            Toast.makeText(getActivity(), "Por favor toma una foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
                isPhotoTaken = true;
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

        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // La foto se tomó correctamente, ahora cargarla en el ImageView
            mostrarFoto(currentPhotoPath);
        }
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
}
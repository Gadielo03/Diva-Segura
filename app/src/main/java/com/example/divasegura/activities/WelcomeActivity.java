package com.example.divasegura.activities;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.divasegura.controladores.CRUDHelper;
import com.example.divasegura.R;
import com.example.divasegura.utils.AppPreferences;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WelcomeActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    Button btnTakePhoto,btnRegistrar;
    boolean isPhotoTaken = false;
    private String currentPhotoPath;

    private EditText etNombreUsuario, etNumeroUsuario, etDomicilioUsuario;
    private EditText etNumeroContacto1, etNombreContacto1;
    private EditText etRelacionContacto1, etRelacionContacto2;
    private EditText etNumeroContacto2, etNombreContacto2;

    private CRUDHelper crudHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //para Testeo descomentar esta linea de codigo para reiniciar el first run
        AppPreferences.resetFirstRun(this);

        if (AppPreferences.isFirstRun(this)) {
            setContentView(R.layout.activity_welcome);

            EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombreUsuario = findViewById(R.id.etNombreUsuario);
        etNumeroUsuario = findViewById(R.id.etTelefonoUsuario);
        etDomicilioUsuario = findViewById(R.id.etDomicilioUsuario);
        etNumeroContacto1 = findViewById(R.id.etTelefonoContactoEmergencia1);
        etNombreContacto1 = findViewById(R.id.etNombreContactoEmergencia1);
        etRelacionContacto1 = findViewById(R.id.etRelacionContactoEmergencia1);
        etRelacionContacto2 = findViewById(R.id.etRelacionContactoEmergencia2);
        etNumeroContacto2 = findViewById(R.id.etTelefonoContactoEmergencia2);
        etNombreContacto2 = findViewById(R.id.etNombreContactoEmergencia2);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(v -> {
            checkCameraPermission();
        });

        btnRegistrar.setOnClickListener(v -> {
            if (RegistrosValidos(v)) {
                guardarDatos(v);
            }
        });

        crudHelper = new CRUDHelper(this);
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public boolean RegistrosValidos(View view) {
        // Validar que se haya tomado/seleccionado una foto
        if (!isPhotoTaken) {
            Toast.makeText(this, "Por favor toma o selecciona una foto", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar campos obligatorios
        if (etNombreUsuario.getText().toString().isEmpty() ||
                etNumeroUsuario.getText().toString().isEmpty() ||
                etNombreContacto1.getText().toString().isEmpty() ||
                etNumeroContacto1.getText().toString().isEmpty()) {

            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Validar longitud de números telefónicos
        if (etNumeroUsuario.getText().toString().length() != 10 ||
                etNumeroContacto1.getText().toString().length() != 10 ||
                etNumeroContacto2.getText().toString().length() != 10) {

            Toast.makeText(this, "Los números deben tener 10 dígitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void guardarDatos(View view) {
        try {
            crudHelper.open();

            crudHelper.eliminarTodosUsuarios();
            // Guardar usuario con foto
            long userId = crudHelper.insertarUsuarioConFoto(
                    etNombreUsuario.getText().toString(),
                    etNumeroUsuario.getText().toString(),
                    etDomicilioUsuario.getText().toString(),
                    currentPhotoPath
            );

            if (userId == -1) {
                Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show();
                return;
            }

            // Guardar contactos
            boolean contactosGuardados = guardarContactos(userId);

            if (contactosGuardados) {
                // Pasar a la siguiente actividad
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al guardar contactos", Toast.LENGTH_SHORT).show();
            }
        } finally {
            crudHelper.close();
            AppPreferences.setFirstRun(this, false);
        }
    }

    private boolean guardarContactos(long userId) {
        try {
            crudHelper.open();

            long id1 = crudHelper.insertarContacto(userId,
                    etNombreContacto1.getText().toString(),
                    etNumeroContacto1.getText().toString(),
                    etRelacionContacto1.getText().toString(),
                    1);
            long id2 = crudHelper.insertarContacto(userId,
                    etNombreContacto2.getText().toString(),
                    etNumeroContacto2.getText().toString(),
                    etRelacionContacto2.getText().toString(),
                    2);

            return id1 != -1 && id2 != -1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            crudHelper.close();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        crudHelper.open(); // Abrir conexión cuando la actividad se inicia/reanuda
    }

    @Override
    protected void onPause() {
        super.onPause();
        crudHelper.close(); // Cerrar conexión cuando la actividad se pausa
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        } else {
            dispatchTakePictureIntent();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error al crear el archivo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getApplicationContext().getPackageName() + ".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Directorio donde se guardarán las fotos
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Guardamos la ruta absoluta
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Foto guardada
            Toast.makeText(this, "Foto guardada correctamente", Toast.LENGTH_SHORT).show();
            isPhotoTaken = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
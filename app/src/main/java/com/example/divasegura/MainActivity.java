package com.example.divasegura;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private CRUDHelper crudHelper;
    private ImageView imgFotoPerfil;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgFotoPerfil = findViewById(R.id.ivFotoUsuario);

        crudHelper = new CRUDHelper(this);

        usuario = new Usuario();

        cargarUsuario();

    }

    private void mostrarFoto(String imagePath) {
        File imgFile = new File(imagePath);

        if (imgFile.exists()) {
            imgFotoPerfil.setImageURI(Uri.fromFile(imgFile));
        } else {
            // Manejar el caso en que la imagen no existe
            imgFotoPerfil.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private void cargarUsuario() {
        crudHelper.open();
        usuario = crudHelper.obtenerUsuario();

        if (usuario != null) {
            String imagePath = usuario.getRutaFoto();
            mostrarFoto(imagePath);
        } else {
            // Manejar el caso en que no se encontró el usuario

        }
    }
}
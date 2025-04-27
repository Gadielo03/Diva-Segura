package com.example.divasegura.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.divasegura.controladores.CRUDHelper;
import com.example.divasegura.R;
import com.example.divasegura.modelos.Usuario;

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
            //Todo

        }
    }
}
package com.example.divasegura;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

import static com.example.divasegura.Estructura.EstructuraContacto.*;
import static com.example.divasegura.Estructura.EstructuraUsuario.*;

public class CrearBD extends SQLiteOpenHelper {
    private static final String NOMBRE_BD = "DivaSegura.db";
    private static final int VERSION_BD = 1;

    public CrearBD(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Usuarios
        String crearTablaUsuarios = "CREATE TABLE " + Estructura.EstructuraUsuario.NOMBRE_TABLA + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Estructura.EstructuraUsuario.COLUMNA_NOMBRE + " TEXT NOT NULL," +
                Estructura.EstructuraUsuario.COLUMNA_NUMERO + " TEXT NOT NULL," +
                COLUMNA_DOMICILIO + " TEXT NOT NULL," +
                COLUMNA_RUTA_FOTO + " TEXT NOT NULL)";

        db.execSQL(crearTablaUsuarios);

        // Crear tabla Contactos
        String crearTablaContactos = "CREATE TABLE " + Estructura.EstructuraContacto.NOMBRE_TABLA + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMNA_USUARIO_ID + " INTEGER NOT NULL, " +
                Estructura.EstructuraContacto.COLUMNA_NOMBRE + " TEXT NOT NULL, " +
                Estructura.EstructuraContacto.COLUMNA_NUMERO + " TEXT NOT NULL, " +
                COLUMNA_RELACION + " TEXT, " +
                COLUMNA_TIPO_CONTACTO + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMNA_USUARIO_ID + ") REFERENCES " +
                Estructura.EstructuraUsuario.NOMBRE_TABLA + "(" + _ID + ") ON DELETE CASCADE)";

        db.execSQL(crearTablaContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Estructura.EstructuraContacto.NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + Estructura.EstructuraContacto.NOMBRE_TABLA);
        onCreate(db);
    }
}
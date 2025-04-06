package com.example.divasegura;

import android.provider.BaseColumns;

public class Estructura {
    public Estructura() {}

    // Tabla de Usuarios
    public static abstract class EstructuraUsuario implements BaseColumns {
        public static final String NOMBRE_TABLA = "Usuarios";
        public static final String COLUMNA_NOMBRE = "nombre";
        public static final String COLUMNA_NUMERO = "numero";
        public static final String COLUMNA_DOMICILIO = "domicilio";
        public static final String COLUMNA_RUTA_FOTO = "ruta_foto";
    }

    // Tabla de Contactos
    public static abstract class EstructuraContacto implements BaseColumns {
        public static final String NOMBRE_TABLA = "Contactos";
        public static final String COLUMNA_USUARIO_ID = "usuario_id";
        public static final String COLUMNA_NOMBRE = "nombre";
        public static final String COLUMNA_NUMERO = "numero";
        public static final String COLUMNA_RELACION = "relacion";
        public static final String COLUMNA_TIPO_CONTACTO = "tipo_contacto";
    }
}
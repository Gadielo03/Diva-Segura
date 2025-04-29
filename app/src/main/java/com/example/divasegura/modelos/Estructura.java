package com.example.divasegura.modelos;

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

    // Tabla de Registro de Alertas
    public static abstract class EstructuraRegistro implements BaseColumns {
        public static final String NOMBRE_TABLA = "RegistroAlertas";
        public static final String COLUMNA_USUARIO_ID = "usuario_id";
        public static final String COLUMNA_FECHA = "fecha";
        public static final String COLUMNA_LATITUD = "latitud";
        public static final String COLUMNA_LONGITUD = "longitud";
        public static final String COLUMNA_FOTOGRAFIA = "fotografia";
    }
}
package com.example.divasegura;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import static com.example.divasegura.Estructura.EstructuraContacto.*;
import static com.example.divasegura.Estructura.EstructuraUsuario.*;

public class CRUDHelper {
    private CrearBD dbHelper;
    private SQLiteDatabase database;

    public CRUDHelper(Context context) {
        dbHelper = new CrearBD(context);
    }

    // Abrir conexión a la base de datos
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Cerrar conexión
    public void close() {
        dbHelper.close();
    }

    // ================= OPERACIONES PARA USUARIOS =================

    /**
     * Guarda un nuevo usuario en la base de datos
     * @return ID del usuario insertado o -1 si hubo error
     */
    public long insertarUsuario(String nombre, String numero, String domicilio) {
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraUsuario.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraUsuario.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_DOMICILIO, domicilio);

        return database.insert(Estructura.EstructuraUsuario.NOMBRE_TABLA, null, values);
    }

    /**
     * Busca un usuario por nombre
     * @return Cursor con los datos del usuario o null si no existe
     */
    public Cursor buscarUsuarioPorNombre(String nombre) {
        return database.query(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                new String[]{_ID, Estructura.EstructuraUsuario.COLUMNA_NOMBRE, Estructura.EstructuraUsuario.COLUMNA_NUMERO, COLUMNA_DOMICILIO},
                Estructura.EstructuraUsuario.COLUMNA_NOMBRE + " = ?",
                new String[]{nombre},
                null, null, null
        );
    }

    // En tu CRUDHelper.java
    @SuppressLint("Range")
    public Usuario obtenerUsuario() {
        Usuario usuario = null;

        Cursor cursor = database.query(
                "Usuarios",  // Nombre de tu tabla
                null,        // Todas las columnas
                null,        // Sin WHERE (obtiene todos)
                null,
                null, null,
                null,        // Sin orden
                "1"          // Limitar a 1 resultado
        );

        if (cursor.moveToFirst()) {
            usuario = new Usuario();
            usuario.setId(cursor.getInt(cursor.getColumnIndex(Estructura.EstructuraUsuario._ID)));
            usuario.setNombre(cursor.getString(cursor.getColumnIndex(Estructura.EstructuraUsuario.COLUMNA_NOMBRE)));
            usuario.setNumero(cursor.getString(cursor.getColumnIndex(Estructura.EstructuraUsuario.COLUMNA_NUMERO)));
            usuario.setDomicilio(cursor.getString(cursor.getColumnIndex(COLUMNA_DOMICILIO)));
            usuario.setRutaFoto(cursor.getString(cursor.getColumnIndex(COLUMNA_RUTA_FOTO)));
        }
        cursor.close();

        return usuario;
    }

    /**
     * Actualiza los datos de un usuario
     * @return número de filas afectadas
     */
    public int actualizarUsuario(long id, String nombre, String numero, String domicilio) {
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraUsuario.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraUsuario.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_DOMICILIO, domicilio);

        return database.update(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                values,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Elimina un usuario y sus contactos asociados (por CASCADE)
     * @return número de filas afectadas
     */
    public int eliminarUsuario(long id) {
        return database.delete(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    //borrar Todos los Usuarios
    public void eliminarTodosUsuarios() {
        database.execSQL("DELETE FROM " + Estructura.EstructuraUsuario.NOMBRE_TABLA);
    }

    // ================= OPERACIONES PARA CONTACTOS =================

    /**
     * Guarda un nuevo contacto asociado a un usuario
     */
    public long insertarContacto(long usuarioId, String nombre, String numero,
                                 String relacion, int tipoContacto) {
        ContentValues values = new ContentValues();
        values.put(COLUMNA_USUARIO_ID, usuarioId);
        values.put(Estructura.EstructuraContacto.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraContacto.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_RELACION, relacion);
        values.put(COLUMNA_TIPO_CONTACTO, tipoContacto);

        return database.insert(Estructura.EstructuraContacto.NOMBRE_TABLA, null, values);
    }

    /**
     * Busca los contactos de un usuario
     * @param usuarioId ID del usuario
     * @param tipoContacto 1 para contacto1, 2 para contacto2, o null para todos
     */
    public Cursor buscarContactos(long usuarioId, Integer tipoContacto) {
        String whereClause = COLUMNA_USUARIO_ID + " = ?";
        String[] whereArgs = {String.valueOf(usuarioId)};

        if (tipoContacto != null) {
            whereClause += " AND " + COLUMNA_TIPO_CONTACTO + " = ?";
            whereArgs = new String[]{String.valueOf(usuarioId), String.valueOf(tipoContacto)};
        }

        return database.query(
                Estructura.EstructuraContacto.NOMBRE_TABLA,
                new String[]{_ID, Estructura.EstructuraContacto.COLUMNA_NOMBRE, Estructura.EstructuraContacto.COLUMNA_NUMERO, COLUMNA_RELACION},
                whereClause,
                whereArgs,
                null, null, null
        );
    }

    /**
     * Actualiza un contacto
     */
    public int actualizarContacto(long id, String nombre, String numero, String relacion) {
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraContacto.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraContacto.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_RELACION, relacion);

        return database.update(
                Estructura.EstructuraContacto.NOMBRE_TABLA,
                values,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    /**
     * Elimina un contacto específico
     */
    public int eliminarContacto(long id) {
        return database.delete(
                Estructura.EstructuraContacto.NOMBRE_TABLA,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public long insertarUsuarioConFoto(String nombre, String numero, String domicilio, String rutaFoto) {
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraUsuario.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraUsuario.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_DOMICILIO, domicilio);
        values.put(COLUMNA_RUTA_FOTO, rutaFoto);

        return database.insert(Estructura.EstructuraUsuario.NOMBRE_TABLA, null, values);
    }

    public boolean actualizarFotoUsuario(long userId, String rutaFoto) {
        ContentValues values = new ContentValues();
        values.put(COLUMNA_RUTA_FOTO, rutaFoto);

        int rowsAffected = database.update(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                values,
                _ID + " = ?",
                new String[]{String.valueOf(userId)}
        );

        return rowsAffected > 0;
    }

    public String obtenerRutaFoto(long userId) {
        Cursor cursor = database.query(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                new String[]{COLUMNA_RUTA_FOTO},
                _ID + " = ?",
                new String[]{String.valueOf(userId)},
                null, null, null
        );

        String ruta = null;
        if (cursor.moveToFirst()) {
            ruta = cursor.getString(0);
        }
        cursor.close();

        return ruta;
    }

    // ================= OPERACIONES COMPUESTAS =================

    /**
     * Guarda usuario con sus dos contactos (operación atómica)
     */
    public boolean guardarUsuarioCompleto(String nombreUsuario, String numeroUsuario, String domicilio,
                                          String nombreContacto1, String numeroContacto1, String relacionContacto1,
                                          String nombreContacto2, String numeroContacto2) {
        try {
            database.beginTransaction();

            // Insertar usuario
            long usuarioId = insertarUsuario(nombreUsuario, numeroUsuario, domicilio);
            if (usuarioId == -1) return false;

            // Insertar contactos
            if (insertarContacto(usuarioId, nombreContacto1, numeroContacto1, relacionContacto1, 1) == -1) {
                database.endTransaction();
                return false;
            }

            if (insertarContacto(usuarioId, nombreContacto2, numeroContacto2, "Contacto secundario", 2) == -1) {
                database.endTransaction();
                return false;
            }

            database.setTransactionSuccessful();
            return true;
        } finally {
            database.endTransaction();
        }
    }
}
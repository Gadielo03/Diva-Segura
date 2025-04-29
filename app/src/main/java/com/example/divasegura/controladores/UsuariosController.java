package com.example.divasegura.controladores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.*;

import com.example.divasegura.modelos.Estructura;
import com.example.divasegura.modelos.Usuario;
import com.example.divasegura.utils.CrearBD;

public class UsuariosController {
    private CrearBD dbHelper;
    private SQLiteDatabase database;

    public UsuariosController(Context context) {
        dbHelper = new CrearBD(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Cerrar conexión
    public void close() {
        dbHelper.close();
    }

    public long insertarUsuario(String nombre, String numero, String domicilio, String foto){
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraUsuario.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraUsuario.COLUMNA_NUMERO, numero);
        values.put(Estructura.EstructuraUsuario.COLUMNA_DOMICILIO, domicilio);
        values.put(Estructura.EstructuraUsuario.COLUMNA_RUTA_FOTO, foto);

        return database.insert(Estructura.EstructuraUsuario.NOMBRE_TABLA, null, values);
    }

    public Usuario obtenerUsuarioUnico() {
        Cursor cursor = database.query(
                NOMBRE_TABLA,
                new String[]{_ID, COLUMNA_NOMBRE, COLUMNA_NUMERO, COLUMNA_DOMICILIO, COLUMNA_RUTA_FOTO},
                null, null, null, null, null, "1"
        );

        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario(
                    cursor.getLong(cursor.getColumnIndexOrThrow(_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_NOMBRE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_NUMERO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_DOMICILIO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMNA_RUTA_FOTO))
            );
            cursor.close();
            return usuario;
        }

        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public int actualizarUsuario(long id, String nombre, String numero, String domicilio, String foto) {
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraUsuario.COLUMNA_NOMBRE, nombre);
        values.put(Estructura.EstructuraUsuario.COLUMNA_NUMERO, numero);
        values.put(COLUMNA_DOMICILIO, domicilio);
        values.put(COLUMNA_RUTA_FOTO, foto);

        return database.update(
                Estructura.EstructuraUsuario.NOMBRE_TABLA,
                values,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
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
    public void eliminarTodosUsuarios() {
        database.execSQL("DELETE FROM " + Estructura.EstructuraUsuario.NOMBRE_TABLA);
    }

}

package com.example.divasegura.controladores;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.divasegura.modelos.Estructura;
import static com.example.divasegura.modelos.Estructura.EstructuraContacto.*;

import com.example.divasegura.modelos.Usuario;
import com.example.divasegura.utils.CrearBD;

public class ContactoController {
    private CrearBD dbHelper;
    private SQLiteDatabase database;

    public ContactoController(Context context) {
        dbHelper = new CrearBD(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    // Cerrar conexión
    public void close() {
        dbHelper.close();
    }

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

    public int eliminarContacto(long id) {
        return database.delete(
                Estructura.EstructuraContacto.NOMBRE_TABLA,
                _ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }
    public boolean guardarContactos(Context context,
                                    String nombreUsuario, String numeroUsuario, String domicilio, String foto,
                                    String nombreContacto1, String numeroContacto1, String relacionContacto1,
                                    String nombreContacto2, String numeroContacto2) {
        boolean success = false;

        try {
            database.beginTransaction();

            // Insert user using existing UsuariosController
            UsuariosController usuariosController = new UsuariosController(context);
            usuariosController.open();

            // First delete any existing user and contacts to maintain single user setup
            usuariosController.eliminarTodosUsuarios();

            long usuarioId = usuariosController.insertarUsuario(nombreUsuario, numeroUsuario, domicilio, foto);

            if (usuarioId != -1) {
                // Insert primary contact
                long contacto1Id = insertarContacto(usuarioId, nombreContacto1, numeroContacto1,
                        relacionContacto1, 1);

                // Insert secondary contact
                long contacto2Id = insertarContacto(usuarioId, nombreContacto2, numeroContacto2,
                        "Contacto secundario", 2);

                if (contacto1Id != -1 && contacto2Id != -1) {
                    database.setTransactionSuccessful();
                    success = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        } finally {
            try {
                database.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return success;
    }

}

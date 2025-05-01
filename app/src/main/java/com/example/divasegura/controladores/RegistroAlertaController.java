package com.example.divasegura.controladores;

import static android.provider.BaseColumns._ID;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.COLUMNA_DOMICILIO;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.COLUMNA_NOMBRE;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.COLUMNA_NUMERO;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.COLUMNA_RUTA_FOTO;
import static com.example.divasegura.modelos.Estructura.EstructuraUsuario.NOMBRE_TABLA;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.divasegura.modelos.Estructura;
import com.example.divasegura.modelos.RegistroAlerta;
import com.example.divasegura.utils.CrearBD;

import java.util.ArrayList;

public class RegistroAlertaController {
    private CrearBD dbHelper;
    private SQLiteDatabase database;

    public RegistroAlertaController(Context context) {
        dbHelper = new CrearBD(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){dbHelper.close();}

    public long insertarRegistroAlertaConFoto(long usuarioId,String Fecha
                                        ,double latitud, double longitud, String fotografia){
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraRegistro.COLUMNA_USUARIO_ID, usuarioId);
        values.put(Estructura.EstructuraRegistro.COLUMNA_FECHA, Fecha);
        values.put(Estructura.EstructuraRegistro.COLUMNA_LATITUD, latitud);
        values.put(Estructura.EstructuraRegistro.COLUMNA_LONGITUD, longitud);
        values.put(Estructura.EstructuraRegistro.COLUMNA_FOTOGRAFIA, fotografia);

        return database.insert(Estructura.EstructuraRegistro.NOMBRE_TABLA, null, values);
    }

    public long insertarRegistroAlertaSinFoto(long usuarioId,String Fecha
            ,double latitud, double longitud){
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraRegistro.COLUMNA_USUARIO_ID, usuarioId);
        values.put(Estructura.EstructuraRegistro.COLUMNA_FECHA, Fecha);
        values.put(Estructura.EstructuraRegistro.COLUMNA_LATITUD, latitud);
        values.put(Estructura.EstructuraRegistro.COLUMNA_LONGITUD, longitud);

        return database.insert(Estructura.EstructuraRegistro.NOMBRE_TABLA, null, values);
    }

    public long actualizarFoto(long registroID,String foto){
        ContentValues values = new ContentValues();
        values.put(Estructura.EstructuraRegistro.COLUMNA_FOTOGRAFIA, foto);
        return database.update(Estructura.EstructuraRegistro.NOMBRE_TABLA, values,
                Estructura.EstructuraRegistro._ID + " = ?", new String[]{String.valueOf(registroID)});
    }

    public long eliminarRegistro(long registroID){
        return database.delete(Estructura.EstructuraRegistro.NOMBRE_TABLA,
                Estructura.EstructuraRegistro._ID + " = ?", new String[]{String.valueOf(registroID)});
    }

    public ArrayList<RegistroAlerta> obtenerTodosLosRegistros(){
        Cursor cursor = database.query(
                Estructura.EstructuraRegistro.NOMBRE_TABLA,
                new String[]{_ID,
                        Estructura.EstructuraRegistro.COLUMNA_USUARIO_ID,
                        Estructura.EstructuraRegistro.COLUMNA_FECHA,
                        Estructura.EstructuraRegistro.COLUMNA_LATITUD,
                        Estructura.EstructuraRegistro.COLUMNA_LONGITUD,
                        Estructura.EstructuraRegistro.COLUMNA_FOTOGRAFIA},
                null, null, null, null, null
        );

        ArrayList<RegistroAlerta> listaRegistros = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                RegistroAlerta registro = new RegistroAlerta(
                        cursor.getLong(cursor.getColumnIndexOrThrow(_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(Estructura.EstructuraRegistro.COLUMNA_USUARIO_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Estructura.EstructuraRegistro.COLUMNA_FECHA)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Estructura.EstructuraRegistro.COLUMNA_LATITUD)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(Estructura.EstructuraRegistro.COLUMNA_LONGITUD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(Estructura.EstructuraRegistro.COLUMNA_FOTOGRAFIA))
                );
                listaRegistros.add(registro);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return listaRegistros;
    }

    public void eliminarTodosRegistros(){
        database.delete(Estructura.EstructuraRegistro.NOMBRE_TABLA, null, null);
    }

}

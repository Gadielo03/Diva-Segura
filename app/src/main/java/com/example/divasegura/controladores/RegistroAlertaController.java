package com.example.divasegura.controladores;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.divasegura.modelos.Estructura;
import com.example.divasegura.utils.CrearBD;

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

    public void eliminarTodosRegistros(){
        database.delete(Estructura.EstructuraRegistro.NOMBRE_TABLA, null, null);
    }

}

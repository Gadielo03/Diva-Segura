package com.example.divasegura.modelos;

public class Contacto {
    private long id;
    private long usuarioId;
    private String nombre;
    private String numero;
    private String relacion;
    private int tipoContacto;

    public Contacto(long id, long usuarioId, String nombre, String numero, String relacion, int tipoContacto) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.numero = numero;
        this.relacion = relacion;
        this.tipoContacto = tipoContacto;
    }
    public Contacto() {
        this.id = -1;
        this.usuarioId = -1;
        this.nombre = "";
        this.numero = "";
        this.relacion = "";
        this.tipoContacto = -1;
    }

    public long getId() {
        return id;
    }
    public long getUsuarioId() {
        return usuarioId;
    }
    public String getNombre() {
        return nombre;
    }
    public String getNumero() {
        return numero;
    }
    public String getRelacion() {
        return relacion;
    }
    public int getTipoContacto() {
        return tipoContacto;
    }
}
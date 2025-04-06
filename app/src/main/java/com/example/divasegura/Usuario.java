package com.example.divasegura;

public class Usuario {
    private long id;
    private String nombre;
    private String numero;
    private String domicilio;
    private String rutaFoto;

    public Usuario(long id,String nombre, String numero, String domicilio, String rutaFoto) {
        this.nombre = nombre;
        this.numero = numero;
        this.domicilio = domicilio;
        this.rutaFoto = rutaFoto;
        this.id = id;
    }

    public Usuario(){
        this.nombre = "";
        this.numero = "";
        this.domicilio = "";
        this.rutaFoto = "";
        this.id = -1;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

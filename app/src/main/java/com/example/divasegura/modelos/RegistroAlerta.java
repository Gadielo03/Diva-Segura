package com.example.divasegura.modelos;

public class RegistroAlerta {
    private long id;
    private long usuarioId;
    private String fecha;
    private double latitud;
    private double longitud;
    private String fotografia;

    public RegistroAlerta(long id, long usuarioId, String fecha, double latitud, double longitud, String fotografia) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fotografia = fotografia;
    }

    public RegistroAlerta(){
        this.id = -1;
        this.usuarioId = -1;
        this.fecha = "";
        this.latitud = 0;
        this.longitud = 0;
        this.fotografia = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
}

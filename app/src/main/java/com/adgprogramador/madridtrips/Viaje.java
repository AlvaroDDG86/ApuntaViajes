package com.adgprogramador.madridtrips;

/**
 * Created by Alvaro on 24/01/2017.
 */

public class Viaje {
    private int id;
    private String fecha;
    private String medio;
    private String importe;
    private String kms;
    private String trayecto;

    public Viaje(int id, String fecha, String medio, String importe, String kms, String trayecto) {
        this.id = id;
        this.fecha = fecha;
        this.medio = medio;
        this.importe = importe;
        this.kms = kms;
        this.trayecto = trayecto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMedio() {
        return medio;
    }

    public void setMedio(String medio) {
        this.medio = medio;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getTrayecto() {
        return trayecto;
    }

    public void setTrayecto(String trayecto) {
        this.trayecto = trayecto;
    }
}

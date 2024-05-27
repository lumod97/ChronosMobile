package com.easj.chromosmobile.Entitys;

public class ReporteMarcas {
    private int idMarcacion;
    private String trabajador;
    private String Dni;
    private String Fecha;
    private String Hora;
    private String Descripcion;
    private String tipoMarcacion;

    public String getProcesada() {
        return Procesada;
    }

    public void setProcesada(String procesada) {
        Procesada = procesada;
    }

    private String Procesada;

    public String getTipoMarcacion() {
        return tipoMarcacion;
    }

    public void setTipoMarcacion(String tipoMarcacion) {
        this.tipoMarcacion = tipoMarcacion;
    }

    public int getIdMarcacion() {
        return idMarcacion;
    }

    public void setIdMarcacion(int idMarcacion) {
        this.idMarcacion = idMarcacion;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
}

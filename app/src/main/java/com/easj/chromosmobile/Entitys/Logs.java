package com.easj.chromosmobile.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "logs")
public class Logs {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Accion;
    private String Fecha;
    private String Usuario;
    private int Puerta;
    private int Dispositivo;
    private String demora;
    private String condicion;
    private int transferido;

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    private  String parametros;

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccion() {
        return Accion;
    }

    public void setAccion(String accion) {
        Accion = accion;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public int getPuerta() {
        return Puerta;
    }

    public void setPuerta(int puerta) {
        Puerta = puerta;
    }

    public int getDispositivo() {
        return Dispositivo;
    }

    public void setDispositivo(int dispositivo) {
        Dispositivo = dispositivo;
    }

    public String getDemora() {
        return demora;
    }

    public void setDemora(String demora) {
        this.demora = demora;
    }

    public int getTransferido() {
        return transferido;
    }

    public void setTransferido(int transferido) {
        this.transferido = transferido;
    }
}

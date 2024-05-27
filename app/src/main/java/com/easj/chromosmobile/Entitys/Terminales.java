package com.easj.chromosmobile.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cns_terminales")
public class Terminales {
    @PrimaryKey(autoGenerate = false)
    private int id;

    private String Mac;
    private String Ip;
    private String Descripcion;
    //foreign
    private int IdPuerta;
    private String FechaRegistro;
    private String FechaActualiza;
    private String IdEstado;
    private String Tipo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return Mac;
    }

    public void setMac(String mac) {
        Mac = mac;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getIdPuerta() {
        return IdPuerta;
    }

    public void setIdPuerta(int idPuerta) {
        IdPuerta = idPuerta;
    }

    public String getFechaRegistro() {
        return FechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        FechaRegistro = fechaRegistro;
    }

    public String getFechaActualiza() {
        return FechaActualiza;
    }

    public void setFechaActualiza(String fechaActualiza) {
        FechaActualiza = fechaActualiza;
    }

    public String getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(String idEstado) {
        IdEstado = idEstado;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }
}

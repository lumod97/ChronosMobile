package com.easj.chromosmobile.Entitys;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity(tableName = "cns_marcas")
public class Marcas {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String idMarcacion;
    private String Dni;
    private String Fecha;
    private String Hora;
    private int Accion;
    private String IdDispositivo;
    private int Procesada;
    private String IdPuntoControl;
    private String FechaEnvio;
    private String TipoMarcacion;

    public String getIdMarcacion() {
        return idMarcacion;
    }

    public void setIdMarcacion(String idMarcacion) {
        this.idMarcacion = idMarcacion;
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

    public int getAccion() {
        return Accion;
    }

    public void setAccion(int accion) {
        Accion = accion;
    }

    public String getIdDispositivo() {
        return IdDispositivo;
    }

    public void setIdDispositivo(String idDispositivo) {
        IdDispositivo = idDispositivo;
    }

    public int getProcesada() {
        return Procesada;
    }

    public void setProcesada(int procesada) {
        Procesada = procesada;
    }

    public String getIdPuntoControl() {
        return IdPuntoControl;
    }

    public void setIdPuntoControl(String idPuntoControl) {
        IdPuntoControl = idPuntoControl;
    }

    public String getFechaEnvio() {
        return FechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        FechaEnvio = fechaEnvio;
    }

    public String getTipoMarcacion() {
        return TipoMarcacion;
    }

    public void setTipoMarcacion(String tipoMarcacion) {
        TipoMarcacion = tipoMarcacion;
    }
}

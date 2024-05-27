package com.easj.chromosmobile.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cns_puertas")
public class Puertas {
    @PrimaryKey(autoGenerate = false)
    private int id;

    private String Descripcion;
    private String IdEstado;
    private String DniUsuarioCrea;
    private String FechaHoraCreacion;
    private String DniUsuarioActualiza;
    private String FechaHoraActualizacion;
    private int IdTipo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(String idEstado) {
        IdEstado = idEstado;
    }

    public String getDniUsuarioCrea() {
        return DniUsuarioCrea;
    }

    public void setDniUsuarioCrea(String dniUsuarioCrea) {
        DniUsuarioCrea = dniUsuarioCrea;
    }

    public String getFechaHoraCreacion() {
        return FechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        FechaHoraCreacion = fechaHoraCreacion;
    }

    public String getDniUsuarioActualiza() {
        return DniUsuarioActualiza;
    }

    public void setDniUsuarioActualiza(String dniUsuarioActualiza) {
        DniUsuarioActualiza = dniUsuarioActualiza;
    }

    public String getFechaHoraActualizacion() {
        return FechaHoraActualizacion;
    }

    public void setFechaHoraActualizacion(String fechaHoraActualizacion) {
        FechaHoraActualizacion = fechaHoraActualizacion;
    }

    public int getIdTipo() {
        return IdTipo;
    }

    public void setIdTipo(int idTipo) {
        IdTipo = idTipo;
    }
}

package com.easj.chromosmobile.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mst_personas")
public class Personas {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String Dni;
    private String Nombres;
    private String Paterno;
    private String Materno;
    private String Planilla;
    private String Observacion;
//    Foreign
    private int IdEstado;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return Dni;
    }

    public void setDni(String dni) {
        Dni = dni;
    }

    public String getNombres() {
        return Nombres;
    }

    public void setNombres(String nombres) {
        Nombres = nombres;
    }

    public String getPaterno() {
        return Paterno;
    }

    public void setPaterno(String paterno) {
        Paterno = paterno;
    }

    public String getMaterno() {
        return Materno;
    }

    public void setMaterno(String materno) {
        Materno = materno;
    }

    public String getPlanilla() {
        return Planilla;
    }

    public void setPlanilla(String planilla) {
        Planilla = planilla;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public int getIdEstado() {
        return IdEstado;
    }

    public void setIdEstado(int idEstado) {
        IdEstado = idEstado;
    }
}

package com.easj.chromosmobile.Entitys;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cns_marcasProcesadas")
public class MarcasProcesadas {
    @PrimaryKey
    private int id;
    private String Dni;
    private String Fecha;
    private String M1;
    private String M2;
    private String M3;
    private String M4;
    private String M5;
    private String M6;
    private String TiempoReceso;
    private String TiempoReceso2;
    private String TiempoJornada;
    private String Garita;
    private String M1_Real;
    private String M2_Real;
    private String M3_Real;
    private String M4_Real;
    private String M5_Real;
    private String M6_Real;
    private String TiempoReceso_Real;
    private String TiempoReceso2_Real;
    private String TiempoJornada_Real;
    private String Estado;
    private String Observacion;
    private String TMF_M1;
    private String TMF_M2;
    private String TMF_M3;
    private String TMF_M4;
    private String TMF_M5;
    private String TMF_M6;

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

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getM1() {
        return M1;
    }

    public void setM1(String m1) {
        M1 = m1;
    }

    public String getM2() {
        return M2;
    }

    public void setM2(String m2) {
        M2 = m2;
    }

    public String getM3() {
        return M3;
    }

    public void setM3(String m3) {
        M3 = m3;
    }

    public String getM4() {
        return M4;
    }

    public void setM4(String m4) {
        M4 = m4;
    }

    public String getM5() {
        return M5;
    }

    public void setM5(String m5) {
        M5 = m5;
    }

    public String getM6() {
        return M6;
    }

    public void setM6(String m6) {
        M6 = m6;
    }

    public String getTiempoReceso() {
        return TiempoReceso;
    }

    public void setTiempoReceso(String tiempoReceso) {
        TiempoReceso = tiempoReceso;
    }

    public String getTiempoReceso2() {
        return TiempoReceso2;
    }

    public void setTiempoReceso2(String tiempoReceso2) {
        TiempoReceso2 = tiempoReceso2;
    }

    public String getTiempoJornada() {
        return TiempoJornada;
    }

    public void setTiempoJornada(String tiempoJornada) {
        TiempoJornada = tiempoJornada;
    }

    public String getGarita() {
        return Garita;
    }

    public void setGarita(String garita) {
        Garita = garita;
    }

    public String getM1_Real() {
        return M1_Real;
    }

    public void setM1_Real(String m1_Real) {
        M1_Real = m1_Real;
    }

    public String getM2_Real() {
        return M2_Real;
    }

    public void setM2_Real(String m2_Real) {
        M2_Real = m2_Real;
    }

    public String getM3_Real() {
        return M3_Real;
    }

    public void setM3_Real(String m3_Real) {
        M3_Real = m3_Real;
    }

    public String getM4_Real() {
        return M4_Real;
    }

    public void setM4_Real(String m4_Real) {
        M4_Real = m4_Real;
    }

    public String getM5_Real() {
        return M5_Real;
    }

    public void setM5_Real(String m5_Real) {
        M5_Real = m5_Real;
    }

    public String getM6_Real() {
        return M6_Real;
    }

    public void setM6_Real(String m6_Real) {
        M6_Real = m6_Real;
    }

    public String getTiempoReceso_Real() {
        return TiempoReceso_Real;
    }

    public void setTiempoReceso_Real(String tiempoReceso_Real) {
        TiempoReceso_Real = tiempoReceso_Real;
    }

    public String getTiempoReceso2_Real() {
        return TiempoReceso2_Real;
    }

    public void setTiempoReceso2_Real(String tiempoReceso2_Real) {
        TiempoReceso2_Real = tiempoReceso2_Real;
    }

    public String getTiempoJornada_Real() {
        return TiempoJornada_Real;
    }

    public void setTiempoJornada_Real(String tiempoJornada_Real) {
        TiempoJornada_Real = tiempoJornada_Real;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getObservacion() {
        return Observacion;
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getTMF_M1() {
        return TMF_M1;
    }

    public void setTMF_M1(String TMF_M1) {
        this.TMF_M1 = TMF_M1;
    }

    public String getTMF_M2() {
        return TMF_M2;
    }

    public void setTMF_M2(String TMF_M2) {
        this.TMF_M2 = TMF_M2;
    }

    public String getTMF_M3() {
        return TMF_M3;
    }

    public void setTMF_M3(String TMF_M3) {
        this.TMF_M3 = TMF_M3;
    }

    public String getTMF_M4() {
        return TMF_M4;
    }

    public void setTMF_M4(String TMF_M4) {
        this.TMF_M4 = TMF_M4;
    }

    public String getTMF_M5() {
        return TMF_M5;
    }

    public void setTMF_M5(String TMF_M5) {
        this.TMF_M5 = TMF_M5;
    }

    public String getTMF_M6() {
        return TMF_M6;
    }

    public void setTMF_M6(String TMF_M6) {
        this.TMF_M6 = TMF_M6;
    }
}

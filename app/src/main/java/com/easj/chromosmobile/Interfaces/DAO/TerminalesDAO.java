package com.easj.chromosmobile.Interfaces.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Personas;
import com.easj.chromosmobile.Entitys.Puertas;
import com.easj.chromosmobile.Entitys.Terminales;

import java.util.List;

@Dao
public interface TerminalesDAO {
    @Insert
    void insertarTerminales(Terminales terminales);

    @Query("SELECT * FROM cns_terminales")
    List<Terminales> obtenerTerminales();

    @Query("SELECT COUNT(*) FROM cns_terminales")
    int obtenerCantidadTerminales();

    @Query("SELECT Descripcion FROM cns_terminales WHERE Mac = :mac")
    String obtenerDeviceName(String mac);

    @Query("SELECT Id FROM cns_terminales WHERE Mac = :mac")
    int obtenerDeviceId(String mac);

    @Delete
    void eliminarTerminales(List<Terminales> terminales);
}

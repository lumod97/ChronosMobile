package com.easj.chromosmobile.Interfaces.DAO;

import android.util.Pair;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Personas;
import com.easj.chromosmobile.Entitys.Puertas;

import java.util.List;

@Dao
public interface PuertasDAO {
    @Insert
    void insertarPuerta(Puertas puertas);

    @Query("SELECT * FROM cns_puertas")
    List<Puertas> obtenerPuertas();

    @Query("SELECT COUNT(*) FROM cns_puertas")
    int obtenerCantidadPuertas();

    @Delete
    void eliminarPuertas(List<Puertas> puertas);

    @Query("SELECT Descripcion FROM cns_puertas WHERE id= :id")
    String obtenerDescripcionPuerta(int id);

}

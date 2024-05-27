package com.easj.chromosmobile.Interfaces.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Personas;

import java.util.List;

@Dao
public interface PersonasDAO {
    @Insert
    void insertarPersonas(Personas personas);

    @Query("SELECT * FROM mst_personas")
    List<Personas> obtenerPersonas();

    @Query("SELECT * FROM mst_personas WHERE Dni = :dni")
    List<Personas> buscarPersona(String dni);

      @Delete
    void eliminarPersonas(List<Personas> personas);

    @Query("SELECT COUNT(*) FROM mst_personas")
    int obtenerCantidadPersonas();

}

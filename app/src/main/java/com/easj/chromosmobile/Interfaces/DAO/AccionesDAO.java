package com.easj.chromosmobile.Interfaces.DAO;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Acciones;

import java.util.List;

@Dao
public interface AccionesDAO {
    @Insert
    void insertarAccion(Acciones acciones);

    @Query("SELECT * from cns_acciones")
    List<Acciones> obtenerAcciones();

    @Delete
    void eliminarAcciones(List<Acciones> acciones);
}

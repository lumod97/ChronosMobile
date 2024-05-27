package com.easj.chromosmobile.Interfaces.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Estados;

import java.util.List;

@Dao
public interface EstadosDAO {
    @Insert
    void insertarEstado(Estados estados);
    @Query("SELECT * FROM mst_estados")
    List<Estados> obtenerEstados();
}

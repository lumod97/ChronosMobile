package com.easj.chromosmobile.Interfaces.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.MarcasProcesadas;

import java.util.List;

@Dao
public interface MarcasProcesadasDAO {

    @Insert
    void insertarMarcaProcesada(MarcasProcesadas marcasProcesadas);

    @Query("SELECT * FROM cns_marcasProcesadas")
    List<MarcasProcesadas> obtenerMarcasProcesadas();
}

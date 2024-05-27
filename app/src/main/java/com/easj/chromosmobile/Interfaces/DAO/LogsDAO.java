package com.easj.chromosmobile.Interfaces.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.easj.chromosmobile.Entitys.Logs;

import java.util.List;

@Dao
public interface LogsDAO {
    @Insert
    void insertarLog(Logs logs);

    @Query("SELECT * FROM logs")
    List<Logs> obtenerLogs();

    @Query("SELECT * FROM logs WHERE transferido = 0")
    List<Logs> obtenerLogsSinProcesar();

    @Query("SELECT MAX(Fecha) FROM logs WHERE Accion = :accion")
    String obtenerMaximoLog(String accion);

    @Query("UPDATE logs SET transferido = 1")
    void procesarLogs();
}

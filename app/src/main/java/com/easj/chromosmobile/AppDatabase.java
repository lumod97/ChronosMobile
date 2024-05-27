package com.easj.chromosmobile;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.easj.chromosmobile.Entitys.Acciones;
import com.easj.chromosmobile.Entitys.Estados;
import com.easj.chromosmobile.Entitys.Logs;
import com.easj.chromosmobile.Entitys.Marcas;
import com.easj.chromosmobile.Entitys.MarcasProcesadas;
import com.easj.chromosmobile.Entitys.Personas;
import com.easj.chromosmobile.Entitys.Puertas;
import com.easj.chromosmobile.Entitys.ReporteMarcas;
import com.easj.chromosmobile.Entitys.Terminales;
import com.easj.chromosmobile.Interfaces.DAO.AccionesDAO;
import com.easj.chromosmobile.Interfaces.DAO.EstadosDAO;
import com.easj.chromosmobile.Interfaces.DAO.LogsDAO;
import com.easj.chromosmobile.Interfaces.DAO.MarcasDAO;
import com.easj.chromosmobile.Interfaces.DAO.MarcasProcesadasDAO;
import com.easj.chromosmobile.Interfaces.DAO.PersonasDAO;
import com.easj.chromosmobile.Interfaces.DAO.PuertasDAO;
import com.easj.chromosmobile.Interfaces.DAO.TerminalesDAO;

@Database(entities = {
        Marcas.class,
        Acciones.class,
        Estados.class,
        MarcasProcesadas.class,
        Personas.class,
        Puertas.class,
        Terminales.class,
        Logs.class,
//        ReporteMarcas.class
    }, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MarcasDAO marcasDAO();
    public abstract AccionesDAO accionesDAO();
    public abstract EstadosDAO estadosDAO();
    public abstract MarcasProcesadasDAO marcasProcesadasDAO();
    public abstract PersonasDAO personasDAO();
    public abstract PuertasDAO puertasDAO();
    public abstract TerminalesDAO terminalesDAO();
    public abstract LogsDAO logsDAO();
//    public abstract ReporteMarcasDAO reporteMarcasDAO();
}

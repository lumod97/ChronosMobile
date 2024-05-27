package com.easj.chromosmobile.Interfaces.DAO;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.easj.chromosmobile.Entitys.Marcas;
import com.easj.chromosmobile.Entitys.ReporteMarcas;

import java.util.List;

@Dao
public interface MarcasDAO {
    @Insert
    void insertarMarca(Marcas marcas);

    @Query("SELECT * FROM cns_marcas")
    List<Marcas> obtenerMarcas();

    @Query("SELECT * FROM cns_marcas WHERE Procesada = 0")
    List<Marcas> obtenerMarcasSinProcesar();

    @Query("UPDATE cns_marcas SET Procesada = 1 WHERE Procesada = 0")
    void procesarMarcas();

    @Query("select m.idMarcacion, p.Paterno || \" \"|| p.Materno || \", \" || p.Nombres trabajador, m.Dni, m.Fecha, m.Hora, a.Descripcion, m.TipoMarcacion tipoMarcacion, m.Procesada from cns_marcas m\n" +
            "INNER JOIN cns_acciones a ON a.id = m.Accion\n" +
            "LEFT JOIN mst_personas p ON p.Dni = m.Dni" +
            " WHERE m.Fecha = :fecha AND" +
                "(p.Paterno LIKE \"%\" || :busqueda || \"%\"" +
                "OR p.Materno LIKE \"%\" || :busqueda || \"%\"" +
                "OR p.Nombres LIKE \"%\" || :busqueda || \"%\"" +
                "OR m.Dni LIKE \"%\" || :busqueda || \"%\")" +
                "AND (m.Procesada = :idEstado OR :idEstado = 2)")
    List<ReporteMarcas> obtenerReporteMarcas(String fecha, String busqueda, int idEstado);

    @Query("SELECT COUNT(*) FROM cns_marcas WHERE Dni = :dni AND Fecha = :fecha AND Accion = :accion")
    boolean verificarExistenciaMarca(String dni, String fecha, int accion);
}
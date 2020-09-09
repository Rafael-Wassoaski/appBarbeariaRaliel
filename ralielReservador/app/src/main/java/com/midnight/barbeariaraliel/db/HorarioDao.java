package com.midnight.barbeariaraliel.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.midnight.barbeariaraliel.classes.Horario;

import java.util.List;

@Dao
public interface HorarioDao {

    @Query("SELECT * FROM horario")
    List<Horario> getAllHorarios();

    @Query("SELECT * FROM horario GROUP BY horario")
    List<Horario> getMostRecent();

    @Insert
    void insertHorario(Horario horario);


}

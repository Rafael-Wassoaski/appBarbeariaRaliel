package com.midnight.barbeariaraliel.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.midnight.barbeariaraliel.classes.Horario;


@Database(entities = Horario.class, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public abstract HorarioDao horarioDao();

}

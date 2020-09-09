package com.midnight.barbeariaraliel.db;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Room;

import com.midnight.barbeariaraliel.classes.Horario;
import com.midnight.barbeariaraliel.fragmentos.meus_horarios;
import com.midnight.barbeariaraliel.interfaces.Meus_horarios_async;

import java.util.ArrayList;
import java.util.List;

public class DBGet extends AsyncTask<Context, String, List> {

    public Meus_horarios_async meus_horarios_async;
    @Override
    protected List doInBackground(Context... contexts) {

        AppDataBase db = Room.databaseBuilder(contexts[0], AppDataBase.class, "horario").build();
        List <Horario> horariosReservados  = db.horarioDao().getMostRecent();


        for(Horario horario : horariosReservados){
            Log.d("banco", horario.getHorario());
        }

        return horariosReservados;
    }

    @Override
    protected void onPostExecute(List list) {
        super.onPostExecute(list);
        meus_horarios_async.setHorariosReservados((ArrayList) list, null);
    }
}

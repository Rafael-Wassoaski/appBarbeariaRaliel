package com.midnight.barbeariaraliel.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.midnight.barbeariaraliel.classes.Horario;
import com.midnight.barbeariaraliel.fragmentos.meus_horarios;

import org.json.JSONException;
import org.json.JSONObject;

public class DBSave extends AsyncTask<Context, String, String> {

    private JSONObject dados;
    public meus_horarios meus_horaios;

    public void setDados(JSONObject dados) {
        this.dados = dados;
    }

    @Override
    protected String doInBackground(Context... contexts) {

        AppDataBase appDataBase = Room.databaseBuilder(contexts[0], AppDataBase.class, "horario").build();
        try {
            appDataBase.horarioDao().insertHorario(new Horario(dados.getString("hora"), dados.getString("nome"), dados.getString("telefone")));
            meus_horaios.addReserva(new Horario(dados.getString("hora"), dados.getString("nome"), dados.getString("telefone")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

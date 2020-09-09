package com.midnight.barbeariaraliel.asyncTasks;


import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.midnight.barbeariaraliel.fragmentos.horarios_livres;
import com.midnight.barbeariaraliel.interfaces.Horarios_lires_async;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestMaker extends AsyncTask<horarios_livres, JSONObject, String> {

    public Horarios_lires_async response;

    public String getHorarios(){
        BufferedReader reader = null;
        StringBuilder sb;
        String uri = "https://ServerRaliel.extremegame300.repl.co";

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            con.setRequestProperty("Content-Type", "application/json; utf-8");



            sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return sb.toString();

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(horarios_livres... horarios_livres) {
        horarios_livres = horarios_livres;
        return getHorarios();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        response.setHorarios(s, null);
    }
}

package com.midnight.barbeariaraliel.asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.midnight.barbeariaraliel.fragmentos.meus_horarios;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestMakerCancelHorario extends AsyncTask<String, String, String> {


    public meus_horarios meus_horariosClass;
    private JSONObject jsonObject;
    private int positon;
    public Handler handler;

    private String cancelarHora(String data, String nome, String id){
        BufferedReader reader = null;
        StringBuilder sb;
        String uri = "https://ServerRaliel.extremegame300.repl.co/cancelarHorario";

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");


            jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("data", data);
            jsonObject.put("nome", nome);


            OutputStream os = con.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.close();

            sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.d("Teste", sb.toString());


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
        Log.d("Teste", sb.toString());
        return sb.toString();

    }


    @Override
    protected String doInBackground(String... strings) {
        positon = Integer.parseInt(strings[3]);
        Log.d("Teste", "clic");
        return cancelarHora(strings[0], strings[1], strings[2]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        JSONObject newJson = null;
        try {
            newJson = new JSONObject(s);
            Log.d("Response", newJson.toString());

            if(newJson.getInt("status") == 200){
                Bundle bundle = new Bundle();
                bundle.putInt("position", positon);
                Message message = new Message();
                message.setData(bundle);
                handler.sendMessage(message);

                Toast.makeText(meus_horariosClass.getContext(), "Horario cancelado", Toast.LENGTH_SHORT).show();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

package com.midnight.barbeariaraliel.asyncTasks;


import android.os.AsyncTask;
import android.util.Log;


import com.midnight.barbeariaraliel.classes.HorarioAdapter;
import com.midnight.barbeariaraliel.fragmentos.popUp;
import com.midnight.barbeariaraliel.interfaces.Meus_horarios_async;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RequestMakerReserva extends AsyncTask<String, JSONObject, String> {

    public Meus_horarios_async meus_horarios_async;

    public String getHorarios(String nome, String id, String horario, String obs, String telefone, String telefoneBarber, String  nomeBarber){
        BufferedReader reader = null;
        StringBuilder sb;
        String uri = "https://ServerRaliel.extremegame300.repl.co/reservarHorario";


        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("data", horario);
            jsonObject.put("obs", obs);
            jsonObject.put("telefone", telefone);
            jsonObject.put("nome", nome);

            Log.d("JSON", jsonObject.toString());

            OutputStream os = con.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
            os.close();

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

        //Log.d("Response", sb.toString().replace('}', ',')+"\n"+"\"Telefone\": "+ telefoneBarber + ",\n"+"\"Nome\": "+ nomeBarber + ",\n" + "\"Data\": " + horario + "}");
        return sb.toString().replace('}', ',')+"\n"+"\"telefone\": \"" +telefoneBarber + "\",\n"+"\"nome\": \""+ nomeBarber + "\",\n" + "\"hora\": \"" + horario + "\"}";

    }


    @Override
    protected String doInBackground(String... hastoken) {
        return getHorarios(
                hastoken[0],hastoken[1], hastoken[2], hastoken[3], hastoken[4], hastoken[5], hastoken[6]
        );
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {

            JSONObject newJson = new JSONObject(s);
            Log.d("Response", newJson.toString());
            if(newJson.getInt("status") == 200){

                popUp.saveHorario("Horario resevado com sucesso", newJson , 200);
                //meus_horarios_async.saveHorario(200, newJson);

            }else{
                popUp.saveHorario("Falha ao reservar o horario", newJson, 400);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

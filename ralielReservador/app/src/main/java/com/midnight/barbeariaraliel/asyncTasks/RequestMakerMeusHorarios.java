package com.midnight.barbeariaraliel.asyncTasks;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.midnight.barbeariaraliel.MainActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RequestMakerMeusHorarios extends AsyncTask<String, String, String> {

    public Handler handler;

    private String getMeusHorarios(){

        BufferedReader reader = null;
        StringBuilder sb;
        String uri = "https://ServerRaliel.extremegame300.repl.co/meusHorarios";
        try {
            URL url = new URL(uri);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");


            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idUser", MainActivity.id);

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

        return sb.toString();

    }

    @Override
    protected String doInBackground(String[] strings) {
        return getMeusHorarios();
    }

    @Override
    protected void onPostExecute(String str) {
        super.onPostExecute(str);
        Bundle bundle = new Bundle();
        bundle.putString("dados", str);
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}

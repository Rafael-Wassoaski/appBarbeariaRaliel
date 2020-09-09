package com.midnight.barbeariaraliel.fragmentos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.room.Room;

import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMakerReserva;
import com.midnight.barbeariaraliel.classes.HorarioAdapter;
import com.midnight.barbeariaraliel.db.DBSave;
import com.midnight.barbeariaraliel.interfaces.Meus_horarios_async;
import com.midnight.barbeariaraliel.interfaces.encerrar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class popUp extends Activity implements encerrar {

    private static Context context;
    private static int position;
    private final encerrar acabar = this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.pop_up);
        Intent intent = getIntent();
        position = intent.getExtras().getInt("dados_horario_position");
        final String horartio = intent.getExtras().getString("dados_horario_data");
        final String id = Integer.toString(intent.getExtras().getInt("dados_horario_id"));
        final String telefoneBarber = intent.getExtras().getString("dados_hora_telefone");
        final String nomeBarber = intent.getExtras().getString("dados_hora_nome");
        final EditText nome = (EditText) findViewById(R.id.nome);
        final EditText obs = (EditText) findViewById(R.id.obs);
        final EditText telefone = (EditText) findViewById(R.id.telefone);
        Button button = (Button) findViewById(R.id.reservar);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestMakerReserva request = new RequestMakerReserva();
                request.encerrar = acabar;
                request.execute(nome.getText().toString(), id, horartio, obs.getText().toString(), telefone.getText().toString(), telefoneBarber, nomeBarber);

            }
        });

    }

    public void acabar(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public static void saveHorario(String msg, JSONObject newJson, int code){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        if(code == 200) {
            DBSave db = new DBSave();
            db.setDados(newJson);
            db.execute(context);
            //HorarioAdapter.removerReservado(position);

        }
    }

}

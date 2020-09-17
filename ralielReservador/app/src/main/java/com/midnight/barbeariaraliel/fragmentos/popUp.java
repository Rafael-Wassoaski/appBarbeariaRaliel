package com.midnight.barbeariaraliel.fragmentos;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.midnight.barbeariaraliel.MainActivity;
import com.midnight.barbeariaraliel.R;
import com.midnight.barbeariaraliel.asyncTasks.RequestMakerReserva;

import com.midnight.barbeariaraliel.interfaces.encerrar;


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


        if(MainActivity.nome != null){
            nome.setText(MainActivity.nome);
            nome.setClickable(false);
            nome.setFocusable(false);
            telefone.setText(getSharedPreferences("usuario", MODE_PRIVATE).getString("telefone",  null));

        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestMakerReserva request = new RequestMakerReserva();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        super.handleMessage(msg);
                        exibirResultado(msg.getData().getString("msg"));
                    }
                };
                request.encerrar = acabar;
                request.handler = handler;
                request.execute(nome.getText().toString(), id, horartio, obs.getText().toString(), telefone.getText().toString(), telefoneBarber, nomeBarber);

            }
        });

    }

    public void exibirResultado (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    public void acabar(){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("position", position);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

}

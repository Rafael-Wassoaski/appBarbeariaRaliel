package com.midnight.barbeariaraliel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.midnight.barbeariaraliel.classes.BrPhoneNumberFormatter;

import java.lang.ref.WeakReference;

public class RegistroActivity extends AppCompatActivity {

    private boolean isEmpty(String string){

        if(string.equals("")){
            return false;
        }
        if(string.equals(" ")){
            return false;
        }
        if(string.length() == 0){
            return false;
        }

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        final EditText nome = findViewById(R.id.nome);
        final EditText telefone = findViewById(R.id.telefone);
        Button registrar = findViewById(R.id.registrar);
        BrPhoneNumberFormatter formatter = new BrPhoneNumberFormatter(new WeakReference<EditText>(telefone));
        telefone.addTextChangedListener(formatter);

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(nome.getText().toString())){
                    if (isEmpty(telefone.getText().toString())){
                        SharedPreferences.Editor editor = getSharedPreferences("usuario", MODE_PRIVATE).edit();
                        editor.putString("nome", nome.getText().toString());
                        editor.putString("telefone", telefone.getText().toString());
                        editor.putBoolean("logged", true);
                        if(editor.commit()){
                            Toast.makeText(getApplicationContext(), "Usuário salvo com sucesso", Toast.LENGTH_SHORT).show();
                            Intent activity = null;
                            activity = new Intent(getBaseContext(), MainActivity.class);
                            activity.putExtra("nome", nome.getText().toString());
                            activity.putExtra("id", getSharedPreferences("usuario", MODE_PRIVATE).getString("id", null));
                            startActivity(activity);
                            finish();
                        }

                    }else{
                        Toast.makeText(getApplicationContext(), "Informe um telefone válido", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Informe um nome válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
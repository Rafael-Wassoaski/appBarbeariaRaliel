package com.midnight.barbeariaraliel;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActicvity extends AppCompatActivity {

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("vtncfdp", "aaaaaa");

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login_acticvity);
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions( Arrays.asList("email", "public_profile"));

        if(getPreferences().get(0) != null && getPreferences().get(1)!= null){
            startMain(getPreferences().get(0), getPreferences().get(1));
        }

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                // Application code
                                try {
                                    String name = object.getString("name");
                                    String id = object.getString("id");
                                    SharedPreferences preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("nome", name);
                                    editor.putString("id", id);
                                    if(editor.commit()){
                                        Toast.makeText(getApplicationContext(), "Usário salvo com sucesso", Toast.LENGTH_LONG).show();

                                    }else{
                                        Toast.makeText(getApplicationContext(), "Erro ao salvar o usuário, tente novamente", Toast.LENGTH_LONG).show();
                                    }



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Erro ao realizar login, tente novamente", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private List<String> getPreferences(){
        List<String> dados = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        dados.add(preferences.getString("nome", null));
        dados.add(preferences.getString("id", null));
        return dados;
    }

    private void startMain(String name, String id){
        Intent mainActiviy = new Intent(getBaseContext(), MainActivity.class);

        mainActiviy.putExtra("nome", name);
        mainActiviy.putExtra("id", id);

        startActivity(mainActiviy);
        finish();
    }
    private void tokenHandler(AccessToken token){
        Log.d("Teste", token.toString());
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        setResult(RESULT_CANCELED);
//        finish();
//    }
}
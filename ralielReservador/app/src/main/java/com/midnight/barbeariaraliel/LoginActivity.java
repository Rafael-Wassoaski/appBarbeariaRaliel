package com.midnight.barbeariaraliel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login_acticvity);
        LoginButton loginFacebookButton = findViewById(R.id.login_button);
        SignInButton loginGoogleButton = findViewById(R.id.googleLoginButton);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);


        loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signintent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signintent, 0);
            }
        });


        loginFacebookButton.setReadPermissions( Arrays.asList("email", "public_profile"));

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account != null || getSharedPreferences("usuario", MODE_PRIVATE).getBoolean("logged", false)){
            startMain(getSharedPreferences("usuario", Context.MODE_PRIVATE).getString("id", null));
        }

        loginFacebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    putCredentials(object.getString("id"));
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

    private void putCredentials(String id){
        SharedPreferences preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", id);
        // Application code
            if(editor.commit()){
                Toast.makeText(getApplicationContext(), "Usário salvo com sucesso", Toast.LENGTH_LONG).show();
                startMain(id);

            }else{
                Toast.makeText(getApplicationContext(), "Erro ao salvar o usuário, tente novamente", Toast.LENGTH_LONG).show();
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0){
          Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                putCredentials(account.getId());
            } catch (ApiException e) {
                Log.d("GoogleData", e.getMessage());
                e.printStackTrace();
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private List<String> getPreferences(){
        List<String> dados = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("usuario", Context.MODE_PRIVATE);
        dados.add(preferences.getString("nome", ""));
        dados.add(preferences.getString("id", ""));
        return dados;
    }

    private void startMain(String id){
        Intent activity = null;
        Log.d("Nome", getSharedPreferences("usuario", Context.MODE_PRIVATE).getAll().toString());
        if(getSharedPreferences("usuario", Context.MODE_PRIVATE).getString("nome", null) != null){
            activity = new Intent(getBaseContext(), MainActivity.class);
            activity.putExtra("nome", getSharedPreferences("usuario", Context.MODE_PRIVATE).getString("nome", null));
            activity.putExtra("id", id);
        }else{
            activity = new Intent(getBaseContext(), RegistroActivity.class);
        }



        startActivity(activity);
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
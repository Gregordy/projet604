package com.example.mo.projetIFT604;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import butterknife.ButterKnife;
import butterknife.Bind;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private static final int REQUEST_SIGNUP = 0;
    private String result_login = "false";
    private SharedPreferences sharedpreferences;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

       /* if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Entrez une adresse mail valide");
            return;
        } else {
            _emailText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Le mot de passe doit contenir entre 4 et 10 caractères alphanumériques");
            return;
        } else {
            _passwordText.setError(null);
        }

        // Envoie de la requête http avec la methode post à la base de données
        PostClass requeteHttp = new PostClass();
        requeteHttp.execute(email, password);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }


    // Classe qui contient 3 méthodes pour pouvoir effectuer une requete http
    // Ces requêtes nécessitent d'être effectuer dans un  thread
    private class PostClass extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog progressDialog = new ProgressDialog(Login.this, R.style.AppTheme_Dark_Dialog);

        @Override //Cette méthode s'execute en premier, elle ouvre une simple boite de dialogue
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authentification...");
            progressDialog.show();
        }

        @Override//Cette méthode s'execute en deuxième
        protected JSONObject doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    String result;

                    result = CommunicationServeur.envoiMessage("users", "GET", params);



                    //////////////////////JSON////////////////////////////////////
                    try {

                        JSONObject object = new JSONObject(result);
                        Log.d("RETOUR:", object.toString());
                        return object; // On retourne true ou false


                    } catch (JSONException e) {
                        Log.e("log_tag", "Error parsing data " + e.toString());

                    }


                }


            }

            return null;

        }

        @Override // La troisième méthode qui s'exécute en dernier
        // String th, est la valeur que nous a retourné doInBackground
        protected void onPostExecute(JSONObject th) {
            progressDialog.dismiss();

            if (th != null) {

                if (!th.equals("false")) {
                    sharedpreferences = getSharedPreferences("id_utilisateur", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();

                    try {
                        editor.putString("id", th.getString("value"));
                        editor.putString("total", th.getString("total"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.commit();
                    Toast.makeText(getBaseContext(), "Connexion réussie", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(Login.this, Interface.class);
                    startActivity(myIntent);


                } else {
                    Toast.makeText(getBaseContext(), "Erreur de connexion ", Toast.LENGTH_LONG).show();
                }

            }
            else {
                Toast.makeText(getBaseContext(), "Erreur de connexion ", Toast.LENGTH_LONG).show();
            }

        }

    }


}

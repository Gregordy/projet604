package projet.utilisateur;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import projet.application.MenuActivity;
import projet.R;
import projet.general.CommunicationServeur;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Classe permettant la connexion de l'utilisateur et la redirection
 * vers le coeur de l'application
 */

public class ConnexionActivity extends AppCompatActivity {

    private static final int REQUEST_SIGNUP = 0;

    /**
     * Mise en place de tous les éléments de formulaire
     */
    @Bind(R.id.input_pseudo)
    EditText _pseudoText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    /**
     * Cycle de vie de l'activité : à la création
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Mise en place du Layout correspondant
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        /**
         *  Ecouteur d'événement : dès qu'un clic se produit sur le bouton "Se connecter",
         *  on lance les méthodes de verificationFormulaireLogin
         */
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationFormulaireLogin();
            }
        });

        /**
         *  Ecouteur d'événement : dès qu'un clic se produit sur le lien "Pas encore inscrit ?"
         *  on lance l'activité chargée d'inscrire l'utilisateur
         */
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    /**
     * Méthode appelée directement après le clic sur le bouton "Se connecter"
     */
    public void verificationFormulaireLogin() {

        String name = _pseudoText.getText().toString();
        String password = _passwordText.getText().toString();

        // Si l'utilisateur n'a pas rentré de pseudo :
        if (name.isEmpty()) {
            _pseudoText.setError("Vous devez rentrer votre pseudonyme !");
            return;
        } else {
            _pseudoText.setError(null);
        }

        // Si l'utilisater n'a pas rentré de mot de passe :
        if (password.isEmpty()) {
            _passwordText.setError("Vous devez rentrer votre mot de passe pour vous connecter !");
            return;
        } else {
            _passwordText.setError(null);
        }

        // Si tout est ok, on envoie la requête via AsyncTask :
        PostClass requeteHttp = new PostClass();
        requeteHttp.execute(name, password);
    }

    /**
     * Si l'utilisateur désire s'inscrire, on arrête l'activité "ConnexionActivity"
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    /**
     * On désactive le retour à l'Activité principale (MainActivity)
     */
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    /**
     * Si la connexion est un succès, on termine l'activité "ConnexionActivity"
     */
    public void onLoginSuccess() {
        _loginButton.setEnabled(false);
        finish();
    }

    /**
     * Si la connexion est un échec, le bouton de connexion reste actif
     */
    public void onLoginFailed() {
        _loginButton.setEnabled(true);
    }

    /**
     * Sous-classe héritant de AsyncTask permettant la connexion de l'utilisateur
     * Est appelée juste après verificationFormulaireLogin() si celle-ci est un succès.
     */
    private class PostClass extends AsyncTask<String, Void, JSONObject> {

        final ProgressDialog progressDialog = new ProgressDialog(ConnexionActivity.this, R.style.AppTheme_Dark_Dialog);

        /**
         * S'exécute en tout premier et indique à l'utilisateur qu'on le connecte
         */
        @Override
        protected void onPreExecute() {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Connexion au serveur en cours...");
            progressDialog.show();
        }

        /**
         * S'exécute en second. Interroge le service REST pour voir si la connexion est OK ou non.
         * @param params : [0] contient le pseudo, [1] contient le mot de passe
         * @return JSONObject
         */
        @Override
        protected JSONObject doInBackground(String... params) {

            ConnectivityManager check = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] info = check.getAllNetworkInfo();

            for (int i = 0; i < info.length; i++) {

                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    // Requete au service REST (GET, ressources users) :
                    String result = CommunicationServeur.envoiMessage("users", "GET", params);

                    try {
                        JSONObject object = new JSONObject(result);
                        return object;
                    } catch (JSONException e) {
                        Log.e("log_tag", "Error parsing data " + e.toString());
                    }
                }
            }

            return null;
        }

        /**
         * Troisième et dernière méthode de connexion.
         * @param th retourné par DoInBackground
         */
        @Override
        protected void onPostExecute(JSONObject th) {

            if (th != null) {
                try {
                    // Si la réponse du serveur pour l'argument resultatConnexion est "true"
                    if (th.getBoolean("resultatConnexion")) {

                        // Enregistrement des informations dans les préférences Utilisateurs :
                        SharedPreferences sharedpreferences = getSharedPreferences("id_utilisateur", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        try {
                            editor.putString("id", th.getString("id_utilisateur"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        editor.commit();

                        // Fermeture de la fenêtre "connexion en cours"
                        progressDialog.dismiss();

                        // Fermeture de l'activité et affichage
                        Toast.makeText(getBaseContext(), "Bienvenue sur notre application !", Toast.LENGTH_LONG).show();

                        // Démarrage de la prochaine activité
                        Intent myIntent = new Intent(ConnexionActivity.this, MenuActivity.class);
                        startActivity(myIntent);
                        ConnexionActivity.this.onLoginSuccess();
                    } else {
                        try {
                            progressDialog.dismiss();
                            ConnexionActivity.this.onLoginFailed();
                            Toast.makeText(getBaseContext(), th.getString("erreurConnexion"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else {
                progressDialog.dismiss();
                ConnexionActivity.this.onLoginFailed();
                Toast.makeText(getBaseContext(), "Erreur inconnue du serveur... Nous sommes désolés.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

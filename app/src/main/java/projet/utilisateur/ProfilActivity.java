package projet.utilisateur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import projet.R;

/**
 * Classe / Activité gérant le profil de l'utilisateur
 */
public class ProfilActivity extends AppCompatActivity {

    /**
     * Cycle de vie d'une activité : à la création
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Toast.makeText(getBaseContext(), "Profil correctement chargé !", Toast.LENGTH_LONG).show();
    }

}

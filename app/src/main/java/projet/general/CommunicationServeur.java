package projet.general;

import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class CommunicationServeur
 * Elle gère et centralise toutes les requêtes envoyées au serveur
 *
 * Service web sous le modèle RESTful
 */

public class CommunicationServeur {

    /**
     * Envoie le message au service REST et retourne la réponse JSON (en String)
     *
     * @param nomRessource : users, events, sports
     * @param nomMethode : GET, POST, PUT ou DELETE
     * @param params : Autres paramètres de la requête sous la forme d'un tableau
     * @return résultat JSON (String) de la requête
     */
    public static String envoiMessage(String nomRessource, String nomMethode, String... params) {

        URL url;
        String result = null;
        Log.d("Envoi d'une requête", "RESSOURCE = "+nomRessource+", METHODE = "+nomMethode);

        try {

            // Construction de l'URL
            String paramsURL = "";

            for (String param : params) {
                paramsURL += "/" + param.trim();
            }

            String URLServeur = "http://humanapp.assos.efrei.fr/IFT604-Projet";
            url = new URL(URLServeur + "/"+nomRessource + paramsURL);

            // Construction de la requête
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod(nomMethode);

            // Envoi de la requête
            connection.connect();
            Log.d("Envoi d'une requête", "URL = "+ URLServeur + "/"+nomRessource + paramsURL+", CODE DE RETOUR = "+connection.getResponseCode());

            // Récupération de la réponse
            Log.d("Réponse de la requête", "REPONSE = "+connection.getResponseMessage());

            Reader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            char[] buffer = new char[2000];

            int nbLecture = reader.read(buffer);
            Log.d("Réponse de la requête", "TAILLE = "+nbLecture);
            result = new String(buffer);

            reader.close();

            // Fermeture de la connexion
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Renvoi de la réponse de la requête
        return result;
    }


}

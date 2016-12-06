package projet.general;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static String envoiMessage(final String nomRessource, final String nomMethode, final String... params) {
      Log.d("Envoi d'une requête", "RESSOURCE = "+nomRessource+", METHODE = "+nomMethode);

      ExecutorService executorService = Executors.newSingleThreadExecutor();
      Callable<String> callable       = new Callable<String>() {
        @Override
        public String call() throws Exception {
          try {
            URL url;
            String paramsURL = "";

            // Construction de l'URL
            if(params != null) { for (String param : params) { paramsURL += "/" + param.trim(); } }

            url = new URL("http://humanapp.assos.efrei.fr/IFT604-Projet/"+nomRessource + paramsURL);

            // Construction de la requête
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod(nomMethode);
            connection.connect(); // Envoi de la requête

            Log.d("Envoi d'une requête", "URL = http://humanapp.assos.efrei.fr/IFT604-Projet/"+nomRessource + paramsURL+", CODE DE RETOUR = "+connection.getResponseCode());
            Log.d("Réponse de la requête", "REPONSE = " + connection.getResponseMessage());

            // Traitement de la réponse
            byte[] read = new byte[512];
            ByteArrayOutputStream reader = new ByteArrayOutputStream(2048);
            for(int i; -1 != (i = connection.getInputStream().read(read)); reader.write(read, 0, i));

            connection.disconnect();

            return reader.toString();
          } catch(IOException ex) {
            ex.printStackTrace();
          }
          return null;
        }
      };

      Future<String> future = executorService.submit(callable);

      String res = null;
      try { res = future.get(); } catch (InterruptedException | ExecutionException e) { e.printStackTrace(); }
      executorService.shutdown();
      
      return res; // Renvoi de la réponse de la requête
    }


}

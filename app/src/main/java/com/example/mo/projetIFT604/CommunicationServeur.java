package com.example.mo.projetIFT604;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CommunicationServeur {

    private static String URLServeur = "http://humanapp.assos.efrei.fr/IFT604-Projet";

    public static String envoiMessage(String nomRessource, String nomMethode, String... params) {

        URL url = null;

        try {
            String paramsURL = "";

            for (int i = 0; i < params.length; i++)
            {
                paramsURL += "/"+params[i];
            }

            url = new URL(URLServeur + "/"+nomRessource+"/" + paramsURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestMethod(nomMethode);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            Reader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
            char[] buffer = new char[50];
            reader.read(buffer);
            String result = new String(buffer);
            reader.close();
            connection.disconnect();

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}

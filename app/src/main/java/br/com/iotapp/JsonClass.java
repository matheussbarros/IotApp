package br.com.iotapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonClass {


    JSONObject jObect = null;
    String json = "";

    //Recebe sua url
    public JSONObject getJSONFromUrl(URL... params) {

        try{
            Log.v("MSB","Entrou na classe");
            HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder("");
            String linha = null;
            while((linha = reader.readLine()) != null)
                sb.append(linha);
            json = sb.toString();

        }catch(IOException e){
            e.printStackTrace();
            return  null;
        }






      /**  HttpURLConnection connection = null;
        try {
            Log.v("MSB","Entrou na classe");
            connection = (HttpURLConnection) params[0].openConnection();
            int response = connection.getResponseCode();
            Log.v("MSB",Integer.toString(response));
            if (response == HttpURLConnection.HTTP_OK) {
                Log.v("MSB","Conectou");
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    json = builder.toString();

                    Log.v("MSB",json);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

            e.printStackTrace();
            Log.v("MSB","nãoConectou");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }*/

        // Transforma a String de resposta em um JSonObject
        try {
            jObect = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // retorna o objeto
        return jObect;

    }
}

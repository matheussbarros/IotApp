package br.com.iotapp;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetApi extends AsyncTask<URL, Void, JSONObject> {


    protected JSONObject doInBackground(URL... params) {
        HttpURLConnection connection = null;
        try {

            connection = (HttpURLConnection) params[0].openConnection();
            int response = connection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
                return new JSONObject(builder.toString());
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (connection != null) {


                connection.disconnect();
            }
        }
        return null;
    }

    protected void onPostExecute(JSONObject sensor) {

        convertJSONToObject(sensor);





    }


    private void convertJSONToObject(JSONObject apiSensor){

        try{

            JSONObject objSensor = apiSensor.getJSONObject("objSensor");

            Sensor medicaoSensor = new Sensor(objSensor.getDouble("humidade"),
                    objSensor.getDouble("luminosidade"),
                    objSensor.getString("estado"));



        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

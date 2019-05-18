package br.com.iotapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView valorHumidadeText;
    private TextView valorLuminosidadeText;
    private TextView valorEstadoText;

    private Button ligarButton;
    private Button desligarButton;

    JSONObject jObect = null;
    String json = "";
    String apiUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ligarButton = findViewById(R.id.ligarButton);
        desligarButton = findViewById(R.id.desligarButton);
        valorHumidadeText = findViewById(R.id.valorHumidadeText);
        valorLuminosidadeText = findViewById(R.id.valorLuminosidadeText);
        valorEstadoText = findViewById(R.id.valorEstadoText);
        Log.v("MSB","foi criado");
    }


    public void getObj(URL endereco){
        new GetApi().execute(endereco);
    }


    class GetApi extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
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
                Log.v("MSB",json);
            }catch(IOException e){
                Log.v("MSB","entro qui");
                e.printStackTrace();
                return  null;
            }
            // Transforma a String de resposta em um JSonObject
            try {
                jObect = new JSONObject(json);
                Log.v("MSB","passou da conversão");
                Log.v("MSB",jObect.getString("estado"));
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            // retorna o objeto
            return jObect;
        }


        @Override
        protected void onPostExecute(JSONObject obj) {




            try{


                valorHumidadeText.setText(obj.getString("humidade"));
                valorLuminosidadeText.setText(obj.getString("luminosidade"));
                valorEstadoText.setText(obj.getString("estado"));

              Toast.makeText(MainActivity.this,obj.getString("estado"), Toast.LENGTH_SHORT).show();
            }catch (JSONException e){
               e.printStackTrace();

           }

        }
    }



    private URL createURL (String endPoint){
        String baseUrl = endPoint;
        try{
            String urlString = baseUrl;
            return new URL(urlString);
        }
        catch( Exception e){
            e.printStackTrace();
        }
        return null;
    }





    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ligarButton:

                apiUrl = getString(R.string.web_service_url);
                URL url = createURL (apiUrl);
                if (url != null){

                    JSONObject contact = null;

                    getObj(url);

                    Log.v("MSB","chegou aqui");





                }
                else{
                    Toast.makeText(MainActivity.this,"Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }



                break;

            case R.id.desligarButton:

                break;

        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        ligarButton.setOnClickListener(this);
        desligarButton.setOnClickListener(this);


    }





}

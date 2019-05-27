package br.com.iotapp;

import android.os.AsyncTask;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView valorHumidadeText;
    private TextView valorLuminosidadeText;
    private TextView valorEstadoText;
    private TextView valorEstadoBuzzerText;

    private Button ligarLedButton;
    private Button desligarLedButton;
    private Button ligarBuzzerButton;
    private Button desligarBuzzerButton;
    private Button atualizarButton;

    JSONObject jObect = null;
    String json = "";

    URL urlApi = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ligarLedButton = findViewById(R.id.ligarLedButton);
        desligarLedButton = findViewById(R.id.desligarLedButton);
        ligarBuzzerButton = findViewById(R.id.ligarBuzzerButton);
        desligarBuzzerButton = findViewById(R.id.desligarBuzzerButton);


        atualizarButton = findViewById(R.id.atualizarButton);
        valorHumidadeText = findViewById(R.id.valorHumidadeText);
        valorLuminosidadeText = findViewById(R.id.valorLuminosidadeText);
        valorEstadoText = findViewById(R.id.valorEstadoText);
        valorEstadoBuzzerText = findViewById(R.id.valorEstadoBuzzerText);
        Log.v("MSB", "foi criado");

        String webService = getString(R.string.web_service_url);

        urlApi = createURL(webService);

        /*
            Executa a classe ConnApi na execução no App.
         */
        ConnectApi(urlApi);

    }



    public void ConnectApi(URL endereco) {new ConnApi().execute(endereco);}
    public void ConnectApi2(URL endereco) { new ConnApi2().execute(endereco);}
    /*
        Classe que se comunica com a API e Atualiza o MainActivity.
     */
    class ConnApi extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            try {
                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder("");
                String linha = null;
                while ((linha = reader.readLine()) != null)
                    sb.append(linha);
                json = sb.toString();
                Log.v("MSB", json);
            } catch (IOException e) {

                e.printStackTrace();
                return null;
            }
            // Transforma a String de resposta em um JSonObject
            try {
                jObect = new JSONObject(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return jObect;
        }
        /*
        Recebe o Objeto Json retornado do doInBackground
         */
        @Override
        protected void onPostExecute(JSONObject obj) {
            try {

                /*
                    Passa os Valores do Objeto Json para os textView.
                */
                valorHumidadeText.setText(obj.getString("humidade"));
                valorLuminosidadeText.setText(obj.getString("luminosidade"));


                String ledd = obj.getString("led");
                if(ledd.equals("DESLIGADO")){
                    valorEstadoText.setText("Desligado");
                }else{
                    valorEstadoText.setText("Ligado");
                }
                String buzz = obj.getString("buzzer");
                if(buzz.equals("DESLIGADO")){
                    valorEstadoBuzzerText.setText("Desligado");
                }else{
                    valorEstadoBuzzerText.setText("Ligado");
                }
                Log.v("MSB","Led: "+ ledd);
                Log.v("MSB","Buzeer: "+ buzz);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    class ConnApi2 extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... params) {
            try {

                HttpURLConnection connection = (HttpURLConnection) params[0].openConnection();
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder("");
                String linha = null;
                while ((linha = reader.readLine()) != null)
                    sb.append(linha);
                json = sb.toString();
                Log.v("MSB", json);
            } catch (IOException e) {
                Log.v("MSB", "caiu na exception / não conectou a API");
                e.printStackTrace();
                return null;
            }
            return json;
        }
        /*
        Recebe o Objeto Json retornado do doInBackground
         */
        @Override
        protected void onPostExecute(String obj) {
            try {
                ConnectApi(urlApi);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Método que retorna um Obj URL
    */
    private URL createURL(String endPoint) {
        String baseUrl = endPoint;
        try {
            String urlString = baseUrl;
            return new URL(urlString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ligarLedButton:
                //acessa o endPoint que envia o parametro LIGAR para o led
                URL urlLigaLed = createURL(getString(R.string.ws_led_on));
                if (urlApi != null) {
                    ConnectApi2(urlLigaLed);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.desligarLedButton:
                //acessa o endPoint que envia o parametro DESLIGAR  para o led
                URL urlDesLed = createURL(getString(R.string.ws_led_off));
                if (urlApi != null) {
                    ConnectApi2(urlDesLed);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.ligarBuzzerButton:
                URL urlLigaBuzzer = createURL(getString(R.string.ws_buzzer_on));
                if (urlApi != null) {
                    ConnectApi2(urlLigaBuzzer);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.desligarBuzzerButton:
                URL urlDesBuzzer = createURL(getString(R.string.ws_buzzer_off));
                if (urlApi != null) {
                    ConnectApi2(urlDesBuzzer);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.atualizarButton:
                if (urlApi != null) {
                    ConnectApi(urlApi);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        ligarLedButton.setOnClickListener(this);
        desligarLedButton.setOnClickListener(this);
        ligarBuzzerButton.setOnClickListener(this);
        desligarBuzzerButton.setOnClickListener(this);
        atualizarButton.setOnClickListener(this);
    }


}

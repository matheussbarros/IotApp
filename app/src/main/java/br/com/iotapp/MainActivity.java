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

    private Button ligarButton;
    private Button desligarButton;

    JSONObject jObect = null;
    String json = "";
    String apiUrl = "";
    String estado = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ligarButton = findViewById(R.id.ligarButton);
        desligarButton = findViewById(R.id.desligarButton);
        valorHumidadeText = findViewById(R.id.valorHumidadeText);
        valorLuminosidadeText = findViewById(R.id.valorLuminosidadeText);
        valorEstadoText = findViewById(R.id.valorEstadoText);
        Log.v("MSB", "foi criado");


        apiUrl = getString(R.string.web_service_url);
        URL url = createURL(apiUrl);

        /*
            Executa a classe GetApi2 na execução no App.
         */
        getObj(url);

    }


    /*
        Executa a classe GetApi2 quando chamada.
     */
    public void getObj(URL endereco) {
        new GetApi2().execute(endereco);
    }


    /*
        Classe que se comunica com a API e Atualiza o MainActivity.
     */
    class GetApi2 extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            try {
                Log.v("MSB", "Entrou na classe");
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
            // Transforma a String de resposta em um JSonObject
            try {
                jObect = new JSONObject(json);
                Log.v("MSB", "passou da conversão");
                Log.v("MSB", jObect.getString("led"));
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
                valorEstadoText.setText(obj.getString("led"));
            } catch (JSONException e) {
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
            case R.id.ligarButton:

                //acessa o endPoint que envia o parametro LIGAR para o led
                apiUrl = getString(R.string.web_service_url_lg);
                URL url = createURL(apiUrl);
                if (url != null) {
                    getObj(url);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.desligarButton:

                //acessa o endPoint que envia o parametro DESLIGAR  para o led
                apiUrl = getString(R.string.web_service_url_des);
                URL urlD = createURL(apiUrl);
                if (urlD != null) {
                    getObj(urlD);
                } else {
                    Toast.makeText(MainActivity.this, "Falha ao consultar API", Toast.LENGTH_SHORT).show();
                }
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

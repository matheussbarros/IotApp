package br.com.iotapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView valorHumidadeText;
    private TextView valorLuminosidadeText;
    private TextView valorEstadoText;

    private Button ligarButton;
    private Button desligarButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String endPoint = getString(
                R.string.web_service_url);
        obtemPrevisoes(endPoint);


    }

    public void obtemPrevisoes(String endereco){
        new ObtemMedicoes().execute(endereco);
    }

    class ObtemMedicoes extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder("");
                String linha = null;
                while((linha = reader.readLine()) != null)
                    sb.append(linha);
                return sb.toString();

            }catch(IOException e){
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(MainActivity.this,s.toString(), Toast.LENGTH_SHORT).show();
        }
    }




}

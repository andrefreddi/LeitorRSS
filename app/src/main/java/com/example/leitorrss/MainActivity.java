package com.example.leitorrss;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Log.d(TAG, "onCreate: Iniciando as AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG, "onCreate: Terminou");
    }

    private class DownloadData extends AsyncTask<String, Void, String>{
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: o parametro recebido eh " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: recebeu : " + strings[0]);
            String contets = downloadContents(strings[0]);
            if (contets == null) {
                Log.e(TAG, "doInBackground: Erro baixando dados");
            }
            return contets;
        }

        private String downloadContents(String urlPath) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "downloadContents: O codigo de resposta foi: " + responseCode);
      //          InputStream inputStream = connection.getInputStream();
      //          InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
      //          BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];

                while (true){
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0 ) {
                        break;
                    }
                    if (charsRead > 0) {
                        result.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return result.toString();
            } catch (MalformedURLException ex) {
                Log.d(TAG, "downloadContents: URL Invalida" + ex.getMessage());
            } catch (IOException ex) {
                Log.d(TAG, "downloadContents: IOException ao ler os dados: " + ex.getMessage());
            } catch (SecurityException ex) {
                Log.d(TAG, "downloadContents: Execucao de seguranca, falta permicao? " + ex.getMessage());
            }

            return null;
        }
    }
}
package com.example.controluz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView textViewIntensity;

    // Dirección IP del ESP32, asegúrate de cambiarla con la IP real del ESP32
    private String esp32IP = "http://192.168.1.100/set_brightness?intensity=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        textViewIntensity = findViewById(R.id.textViewIntensity);

        // Actualiza el valor de la intensidad cuando el SeekBar cambia
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewIntensity.setText("Intensidad: " + progress);
                // Llama a la tarea asincrónica para enviar el valor al ESP32
                new ControlarLEDTask().execute(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Tarea asincrónica para enviar el valor de intensidad al ESP32
    private class ControlarLEDTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected String doInBackground(Integer... intensidades) {
            try {
                // Construye la URL con el valor de la intensidad
                String urlString = esp32IP + intensidades[0];
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000); // Tiempo de espera para la conexión
                connection.setReadTimeout(5000);    // Tiempo de espera para la lectura
                connection.connect();

                // Obtiene el código de respuesta (200 OK o 400 Bad Request)
                int responseCode = connection.getResponseCode();
                connection.disconnect();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Comando enviado con éxito";
                } else {
                    return "Error al conectar con el ESP32";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Error de conexión";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Aquí podrías mostrar un mensaje o notificación con el resultado
        }
    }
}

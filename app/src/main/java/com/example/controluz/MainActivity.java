package com.example.controluz;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBar;
    private TextView intensityValue;
    private Button buttonUpdate;

    // Dirección IP del ESP32 (asegúrate de poner la correcta que aparece en tu pantalla OLED)
    private static final String ESP32_IP = "http://192.168.1.100"; // Cambia por la IP de tu ESP32
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        intensityValue = findViewById(R.id.intensityValue);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        client = new OkHttpClient();

        // Actualizar el valor de la intensidad cuando se mueva el SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                intensityValue.setText("Intensidad: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Botón para actualizar la intensidad en el ESP32
        buttonUpdate.setOnClickListener(v -> {
            int intensity = seekBar.getProgress();
            updateIntensityOnESP32(intensity);
        });
    }

    // Función para actualizar la intensidad de la luz en el ESP32
    private void updateIntensityOnESP32(int intensity) {
        String url = ESP32_IP + "/update?intensidad=" + intensity;

        new Thread(() -> {
            try {
                // Crear la solicitud HTTP
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                // Ejecutar la solicitud
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Intensidad actualizada", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Error al actualizar la intensidad", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}

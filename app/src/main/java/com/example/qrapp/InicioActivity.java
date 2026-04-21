package com.example.qrapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.button.MaterialButton;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        
        // Botones del menú
        Button btnEscanear = findViewById(R.id.btnEscanear);
        Button btnGenerar = findViewById(R.id.btnGenerar);
        Button btnBusqueda = findViewById(R.id.btnBusqueda);
        Button btnCrearItem = findViewById(R.id.btnCrearItem);
        MaterialButton btnTema = findViewById(R.id.btnTema);

        // Listeners para cada uno de los botones
        btnEscanear.setOnClickListener(v -> {
            Intent intent = new Intent(this, EscanearActivity.class);
            startActivity(intent);
        });

        btnGenerar.setOnClickListener(v -> {
            Intent intent = new Intent(this, GenerarQRActivity.class);
            startActivity(intent);
        });

        btnBusqueda.setOnClickListener(v -> {
            Intent intent = new Intent(this, BusquedaActivity.class);
            startActivity(intent);
        });

        btnCrearItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearItemActivity.class);
            startActivity(intent);
        });

        // Lógica para cambiar el tema
        btnTema.setOnClickListener(v -> {
            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });
    }
}
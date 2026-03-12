package com.example.qrapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        //botoes del menu
        Button btnEscanear = findViewById(R.id.btnEscanear);
        Button btnGenerar = findViewById(R.id.btnGenerar);
        Button btnBusqueda = findViewById(R.id.btnBusqueda);
        Button btrCreatArticulo = findViewById(R.id.btnCrearItem);

        //Listeners para cada uno de los botones
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

        btrCreatArticulo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CrearItemActivity.class);
            startActivity(intent);
        });
    }
}
package com.example.qrapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class EscanearActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_escanear);

        findViewById(R.id.btnEscanearCamara).setOnClickListener(v -> iniciarEscaneo());
    }

    private void iniciarEscaneo() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Escanea un código QR");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show();
            } else {
                //Buscar el numSerie en la base de datos
                DatabaseHelper databaseHelper = new DatabaseHelper(this);;
                GlobalData globalData = (GlobalData) getApplication();
                globalData.setNumSerie(result.getContents());
                //buscamos el articulo por id en la db
                Articulo articulo = databaseHelper.consultarPorNumSerie(result.getContents());
                //Pasamos el objeto a otra activity
                Intent intent = new Intent(this, InfoArticuloActivity.class);
                intent.putExtra("articulo", articulo);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
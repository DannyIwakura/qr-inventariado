package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrearItemActivity extends AppCompatActivity {

    TextInputEditText etInventario, etExpediente, etNumSerie, etArticulo, etMarca, etModelo, etDescEspacio, etDestino, etSubsede, etPabellon, etPlanta, etEspacio, etFamilia, etProveedor, etPropietario, etObservaciones;
    Spinner spinnerEstado;
    View paso1, paso2, paso3, paso4;
    MaterialButton btnSiguiente, btnAnterior;
    int pasoActual = 1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_item);

        db = new DatabaseHelper(this);

        paso1 = findViewById(R.id.paso1);
        paso2 = findViewById(R.id.paso2);
        paso3 = findViewById(R.id.paso3);
        paso4 = findViewById(R.id.paso4);

        etInventario = findViewById(R.id.etInventario);
        etExpediente = findViewById(R.id.etExpediente);
        etNumSerie = findViewById(R.id.etNumSerie);
        etArticulo = findViewById(R.id.etArticulo);
        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        etSubsede = findViewById(R.id.etSubsede);
        etPabellon = findViewById(R.id.etPabellon);
        etPlanta = findViewById(R.id.etPlanta);
        etEspacio = findViewById(R.id.etEspacio);
        etFamilia = findViewById(R.id.etFamilia);
        etProveedor = findViewById(R.id.etProveedor);
        etPropietario = findViewById(R.id.etPropietario);
        etObservaciones = findViewById(R.id.etObservaciones);

        spinnerEstado = findViewById(R.id.spinnerEstado);
        String[] opciones = {"Activo", "Inactivo", "Stock"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnAnterior.setEnabled(false);

        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        btnSiguiente.setOnClickListener(v -> {
            if (pasoActual < 4) {
                irAPaso(pasoActual + 1);
            } else {
                guardarArticulo();
            }
        });

        btnAnterior.setOnClickListener(v -> {
            if (pasoActual > 1) {
                irAPaso(pasoActual - 1);
            }
        });
    }

    private void irAPaso(int nuevoPaso) {
        paso1.setVisibility(View.GONE);
        paso2.setVisibility(View.GONE);
        paso3.setVisibility(View.GONE);
        paso4.setVisibility(View.GONE);

        pasoActual = nuevoPaso;
        switch (pasoActual) {
            case 1:
                paso1.setVisibility(View.VISIBLE);
                btnAnterior.setEnabled(false);
                btnSiguiente.setText("Siguiente");
                break;
            case 2:
                paso2.setVisibility(View.VISIBLE);
                btnAnterior.setEnabled(true);
                btnSiguiente.setText("Siguiente");
                break;
            case 3:
                paso3.setVisibility(View.VISIBLE);
                btnAnterior.setEnabled(true);
                btnSiguiente.setText("Siguiente");
                break;
            case 4:
                paso4.setVisibility(View.VISIBLE);
                btnAnterior.setEnabled(true);
                btnSiguiente.setText("Guardar");
                break;
        }
    }

    private void guardarArticulo() {
        Articulo a = new Articulo();
        a.setInventario(etInventario.getText().toString().trim());
        a.setExpediente(etExpediente.getText().toString().trim());
        a.setNumSerie(etNumSerie.getText().toString().trim());
        a.setArticulo(etArticulo.getText().toString().trim());
        a.setEstado(spinnerEstado.getSelectedItem().toString());
        a.setMarca(etMarca.getText().toString().trim());
        a.setModelo(etModelo.getText().toString().trim());
        a.setSubsede(etSubsede.getText().toString().trim());
        a.setPabellon(etPabellon.getText().toString().trim());
        a.setPlanta(etPlanta.getText().toString().trim());
        a.setEspacio(etEspacio.getText().toString().trim());
        a.setFamilia(etFamilia.getText().toString().trim());
        a.setProveedor(etProveedor.getText().toString().trim());
        a.setPropietario(etPropietario.getText().toString().trim());
        a.setUsuario("Usuario"); // Asignación automática
        a.setObservaciones(etObservaciones.getText().toString().trim());
        
        a.setVerificadoCAU(new Date());

        db.insertarArticuloCompleto(a);
        Toast.makeText(this, "Artículo guardado", Toast.LENGTH_SHORT).show();
        finish();
    }
}

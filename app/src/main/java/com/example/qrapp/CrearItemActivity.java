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

    TextInputEditText etInventario, etExpediente, etNumSerie, etArticulo, etMarca, etModelo, etDescEspacio, etDestino, etSubsede, etPabellon, etPlanta, etEspacio, etFamilia, etSubfamilia, etSubtipo, etProveedor, etIdPatrimonial, etPrestamos, etFinGarantia, etPropietario, etObservaciones;
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
        etFamilia = findViewById(R.id.etFamilia);
        etSubfamilia = findViewById(R.id.etSubfamilia);
        etSubtipo = findViewById(R.id.etSubtipo);
        etProveedor = findViewById(R.id.etProveedor);
        etSubsede = findViewById(R.id.etSubsede);
        etPabellon = findViewById(R.id.etPabellon);
        etPlanta = findViewById(R.id.etPlanta);
        etEspacio = findViewById(R.id.etEspacio);
        etDescEspacio = findViewById(R.id.etDescEspacio);
        etDestino = findViewById(R.id.etDestino);
        etIdPatrimonial = findViewById(R.id.etIdPatrimonial);
        etPrestamos = findViewById(R.id.etPrestamos);
        etFinGarantia = findViewById(R.id.etFinGarantia);
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

    private boolean validarCampos() {
        if (etInventario.getText().toString().trim().isEmpty()) return false;
        if (etExpediente.getText().toString().trim().isEmpty()) return false;
        if (etNumSerie.getText().toString().trim().isEmpty()) return false;
        if (etArticulo.getText().toString().trim().isEmpty()) return false;
        if (etMarca.getText().toString().trim().isEmpty()) return false;
        if (etModelo.getText().toString().trim().isEmpty()) return false;
        if (etFamilia.getText().toString().trim().isEmpty()) return false;
        if (etSubfamilia.getText().toString().trim().isEmpty()) return false;
        if (etSubtipo.getText().toString().trim().isEmpty()) return false;
        if (etProveedor.getText().toString().trim().isEmpty()) return false;
        if (etSubsede.getText().toString().trim().isEmpty()) return false;
        if (etPabellon.getText().toString().trim().isEmpty()) return false;
        if (etPlanta.getText().toString().trim().isEmpty()) return false;
        if (etEspacio.getText().toString().trim().isEmpty()) return false;
        if (etDescEspacio.getText().toString().trim().isEmpty()) return false;
        if (etDestino.getText().toString().trim().isEmpty()) return false;
        if (etIdPatrimonial.getText().toString().trim().isEmpty()) return false;
        if (etPrestamos.getText().toString().trim().isEmpty()) return false;
        if (etFinGarantia.getText().toString().trim().isEmpty()) return false;
        if (etPropietario.getText().toString().trim().isEmpty()) return false;
        if (etObservaciones.getText().toString().trim().isEmpty()) return false;
        return true;
    }

    private void guardarArticulo() {
        if (!validarCampos()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Articulo a = new Articulo();
        a.setInventario(etInventario.getText().toString().trim());
        a.setExpediente(etExpediente.getText().toString().trim());
        a.setNumSerie(etNumSerie.getText().toString().trim());
        a.setArticulo(etArticulo.getText().toString().trim());
        a.setEstado(spinnerEstado.getSelectedItem().toString());
        a.setMarca(etMarca.getText().toString().trim());
        a.setModelo(etModelo.getText().toString().trim());
        a.setFamilia(etFamilia.getText().toString().trim());
        a.setSubfamilia(etSubfamilia.getText().toString().trim());
        a.setSubtipo(etSubtipo.getText().toString().trim());
        a.setProveedor(etProveedor.getText().toString().trim());
        a.setSubsede(etSubsede.getText().toString().trim());
        a.setPabellon(etPabellon.getText().toString().trim());
        a.setPlanta(etPlanta.getText().toString().trim());
        a.setEspacio(etEspacio.getText().toString().trim());
        a.setDescripcionEspacio(etDescEspacio.getText().toString().trim());
        a.setDestinoDotacion(etDestino.getText().toString().trim());
        a.setIdPatrimonial(etIdPatrimonial.getText().toString().trim());
        a.setPrestamosReservas(etPrestamos.getText().toString().trim());
        a.setfFinGarantia(etFinGarantia.getText().toString().trim());
        a.setPropietario(etPropietario.getText().toString().trim());
        a.setObservaciones(etObservaciones.getText().toString().trim());
        
        a.setUsuario("Usuario"); // Asignación automática
        a.setVerificadoCAU(new Date());
        a.setFechaBaja(null);

        db.insertarArticuloCompleto(a);
        Toast.makeText(this, "Artículo guardado", Toast.LENGTH_SHORT).show();
        finish();
    }
}

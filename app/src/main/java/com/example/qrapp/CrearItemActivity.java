package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrearItemActivity extends AppCompatActivity {

    TextInputEditText etNumSerie, etArticulo, etCentro, etSubsede, etPabellon, etPlanta, etAula, etMarca, etModelo;

    Spinner spinnerEstado;

    View paso1, paso2, paso3;
    MaterialButton btnSiguiente, btnAnterior;;

    Button btnVolver;

    int pasoActual = 1;

    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_item);

        // Pasos
        paso1 = findViewById(R.id.paso1);
        paso2 = findViewById(R.id.paso2);
        paso3 = findViewById(R.id.paso3);

        // Campos de texto
        etNumSerie = findViewById(R.id.etNumSerie);
        etArticulo = findViewById(R.id.etArticulo);
        etCentro = findViewById(R.id.etCentro);
        etSubsede = findViewById(R.id.etSubsede);
        etPabellon = findViewById(R.id.etPabellon);
        etPlanta = findViewById(R.id.etPlanta);
        etAula = findViewById(R.id.etAula);
        etMarca = findViewById(R.id.etMarca);
        etModelo = findViewById(R.id.etModelo);
        Button btnVolver = findViewById(R.id.btnVolver);

        btnVolver.setOnClickListener(v -> {
            finish();
        });
        
        db = new DatabaseHelper(this);

        // Spinner Estado
        spinnerEstado = findViewById(R.id.spinnerEstado); // <--- muy importante
        String[] opciones = {"Activo", "Inactivo", "Stock"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                opciones
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);

        // Botones
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnAnterior = findViewById(R.id.btnAnterior);

        // Primer paso: atrás deshabilitado
        btnAnterior.setEnabled(false);

        // Lógica de pasos
        btnSiguiente.setOnClickListener(v -> {
            if (pasoActual == 1) {
                paso1.setVisibility(View.GONE);
                paso2.setVisibility(View.VISIBLE);
                pasoActual = 2;
                btnAnterior.setEnabled(true);
            } else if (pasoActual == 2) {
                paso2.setVisibility(View.GONE);
                paso3.setVisibility(View.VISIBLE);
                pasoActual = 3;
                btnSiguiente.setText("Guardar"); // último paso
            } else if (pasoActual == 3) {
                guardarArticulo(); // método que guarda en DB
            }
        });

        btnAnterior.setOnClickListener(v -> {
            if (pasoActual == 2) {
                paso2.setVisibility(View.GONE);
                paso1.setVisibility(View.VISIBLE);
                pasoActual = 1;
                btnAnterior.setEnabled(false);
                btnSiguiente.setText("Siguiente");
            } else if (pasoActual == 3) {
                paso3.setVisibility(View.GONE);
                paso2.setVisibility(View.VISIBLE);
                pasoActual = 2;
                btnSiguiente.setText("Siguiente");
            }
        });

    }

    private void siguientePaso(){
        switch(pasoActual){
            case 1:
                paso1.setVisibility(View.GONE);
                paso2.setVisibility(View.VISIBLE);
                pasoActual++;
                break;
            case 2:
                paso2.setVisibility(View.GONE);
                paso3.setVisibility(View.VISIBLE);
                btnSiguiente.setText("Guardar");
                pasoActual++;
                break;
            case 3:
                guardarArticulo();
                break;
        }
    }

    private void guardarArticulo(){
        // Leer todos los campos de la pantalla
        String numSerie = etNumSerie.getText().toString().trim();
        String articulo = etArticulo.getText().toString().trim();
        String estado = spinnerEstado.getSelectedItem().toString();
        String centro = etCentro.getText().toString().trim();
        String subsede = etSubsede.getText().toString().trim();
        String pabellon = etPabellon.getText().toString().trim();
        String planta = etPlanta.getText().toString().trim();
        String aula = etAula.getText().toString().trim();
        String marca = etMarca.getText().toString().trim();
        String modelo = etModelo.getText().toString().trim();

        // Verificado CAU con fecha actual
        String verificadoCAU = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        // Ahora puedes pasar estos valores a tu método para insertar en la DB
        db.insertarArticuloCompleto(
                numSerie,
                articulo,
                estado,
                centro,
                subsede,
                pabellon,
                planta,
                aula,
                marca,
                modelo,
                verificadoCAU
        );
        finish();
    }
}
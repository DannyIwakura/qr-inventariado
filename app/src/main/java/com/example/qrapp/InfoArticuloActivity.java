package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoArticuloActivity extends AppCompatActivity {

    TextView tvArticulo;
    TextView tvEstado;
    TextView tvCentro;
    TextView tvSubsede;
    TextView tvPabellon;
    TextView tvPlanta;
    TextView tvAula;
    TextView tvMarca;
    TextView tvModelo;
    DatabaseHelper db;
    Articulo articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_articulo);
        db = new DatabaseHelper(this);
        articulo = (Articulo) getIntent().getSerializableExtra("articulo");

        //invicio de cada tv
        tvArticulo = findViewById(R.id.tvArticulo);
        tvEstado = findViewById(R.id.tvEstado);
        tvCentro = findViewById(R.id.tvCentro);
        tvSubsede = findViewById(R.id.tvSubsede);
        tvPabellon = findViewById(R.id.tvPabellon);
        tvPlanta = findViewById(R.id.tvPlanta);
        tvAula = findViewById(R.id.tvAula);
        tvMarca = findViewById(R.id.tvMarca);
        tvModelo = findViewById(R.id.tvModelo);

        if (articulo != null){
            tvArticulo.setText("Articulo: " + articulo.getArticulo());
            tvEstado.setText("Estado: " + articulo.getEstado());
            tvCentro.setText("Centro: " + articulo.getCentro());
            tvSubsede.setText("Subsede: " + articulo.getSubsede());
            tvPabellon.setText("Pabellón: " + articulo.getPabellon());
            tvPlanta.setText("Planta: " + articulo.getPlanta());
            tvAula.setText("Aula: " + articulo.getAula());
            tvMarca.setText("Marca: " + articulo.getMarca());
            tvModelo.setText("Modelo: " + articulo.getModelo());
        } else {
            tvArticulo.setText("No se ha podido encontrar un articulo que coincidad con ese número de serie");

        }
    }

    public void verificarArticulo(View view){
        //comprobar que no viene vacio, en tal caso terminamod la ejecucion
        if (articulo == null) return;

        // fecha actual
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // actualizar en la BD
        db.actualizarVerificadoCAU(articulo.getNumSerie(), fechaActual);

        //mostrar dialogo confirmacion
        new AlertDialog.Builder(this)
                .setTitle("Verificado")
                .setMessage("El articulo ha sido verificado correctamente")
                .show();
    }

    public void mostrarDialogoUbicacion(View view){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_formulario, null);
        EditText etPabellon = dialogView.findViewById(R.id.etPabellon);
        EditText etPlanta = dialogView.findViewById(R.id.etPlanta);
        EditText etAula = dialogView.findViewById(R.id.etAula);

        // Pre-rellenar con datos actuales si es necesario
        etPabellon.setText(articulo.getPabellon());
        etPlanta.setText(articulo.getPlanta());
        etAula.setText(articulo.getAula());

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String pabellon = etPabellon.getText().toString();
                    String planta = etPlanta.getText().toString();
                    String aula = etAula.getText().toString();

                    // Lógica para guardar los cambios...
                    actualizarDatosEnServidor(pabellon, planta, aula);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void actualizarDatosEnServidor(String pabellon, String planta, String aula) {
        //comprobar que no viene vacio, en tal caso terminamod la ejecucion
        if (pabellon == null || planta == null || aula == null) return;

        //actualizarmos en la base de datos
        db.actualizarUbicacion(articulo.getNumSerie(), pabellon, planta, aula);

        //actualizamos el articulo localmente para que se refleje en la interfaz
        articulo.setPabellon(pabellon);
        articulo.setPlanta(planta);
        articulo.setAula(aula);

        //los mostramos en los tvs
        tvPabellon.setText("Pabellón: " + articulo.getPabellon());
        tvPlanta.setText("Planta: " + articulo.getPlanta());
        tvAula.setText("Aula: " + articulo.getAula());
    }

}
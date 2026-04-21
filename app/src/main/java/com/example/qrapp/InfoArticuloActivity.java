package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    private LinearLayout containerInfo;
    private DatabaseHelper db;
    private Articulo articulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_info_articulo);
        db = new DatabaseHelper(this);
        articulo = (Articulo) getIntent().getSerializableExtra("articulo");

        containerInfo = findViewById(R.id.containerInfo);

        if (articulo != null){
            actualizarUI();
        } else {
            TextView tv = new TextView(this);
            tv.setText("No se ha podido encontrar un articulo");
            containerInfo.addView(tv);
        }
    }

    private void actualizarUI() {
        containerInfo.removeAllViews();
        
        addField("Inventario", articulo.getInventario());
        addField("Expediente", articulo.getExpediente());
        addField("Nº Serie", articulo.getNumSerie());
        addField("Estado", articulo.getEstado());
        addField("Artículo", articulo.getArticulo());
        addField("Marca", articulo.getMarca());
        addField("Descripción Espacio", articulo.getDescripcionEspacio());
        addField("Modelo", articulo.getModelo());
        addField("Destino Dotación", articulo.getDestinoDotacion());
        addField("Subsede", articulo.getSubsede());
        addField("Pabellón", articulo.getPabellon());
        addField("Planta", articulo.getPlanta());
        addField("Espacio", articulo.getEspacio());
        addField("Familia", articulo.getFamilia());
        addField("Subfamilia", articulo.getSubfamilia());
        addField("Subtipo", articulo.getSubtipo());
        addField("Proveedor", articulo.getProveedor());
        addField("Id Patrimonial", articulo.getIdPatrimonial());
        addField("Prestamos/Reservas", articulo.getPrestamosReservas());
        addField("F. Fin Garantía", articulo.getfFinGarantia());
        addField("Fecha Baja", articulo.getFechaBaja());
        
        String fecha = "No verificado";
        if (articulo.getVerificadoCAU() != null) {
            fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(articulo.getVerificadoCAU());
        }
        addField("Verificación CAU", fecha);
        
        addField("Propietario", articulo.getPropietario());
        addField("Usuario", articulo.getUsuario());
        addField("Observaciones", articulo.getObservaciones());
    }

    private void addField(String label, String value) {
        TextView tv = new TextView(this);
        tv.setText(label + ": " + (value != null ? value : ""));
        tv.setPadding(0, 8, 0, 8);
        if (label.equals("Artículo") || label.equals("Nº Serie")) {
            tv.setTextSize(16);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
        }
        containerInfo.addView(tv);
    }

    public void verificarArticulo(View view){
        if (articulo == null) return;

        String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        db.actualizarVerificadoCAU(articulo.getNumSerie(), fechaActual);
        
        try {
            articulo.setVerificadoCAU(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaActual));
        } catch (Exception e) {}
        
        actualizarUI();
        new AlertDialog.Builder(this).setTitle("Verificado").setMessage("El articulo ha sido verificado").show();
    }

    public void mostrarDialogoUbicacion(View view){
        // Due to the large number of fields, it's better to implement a comprehensive update or just a few key fields.
        // For this task, I will update the dialog layout to include MORE fields as requested.
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_formulario, null);
        
        // Find all fields (I will need to update dialog_formulario.xml next)
        EditText etNumSerie = dialogView.findViewById(R.id.etNumSerie);
        EditText etArticulo = dialogView.findViewById(R.id.etArticulo);
        EditText etEstado = dialogView.findViewById(R.id.etEstado);
        EditText etMarca = dialogView.findViewById(R.id.etMarca);
        EditText etModelo = dialogView.findViewById(R.id.etModelo);
        EditText etSubsede = dialogView.findViewById(R.id.etSubsede);
        EditText etPabellon = dialogView.findViewById(R.id.etPabellon);
        EditText etPlanta = dialogView.findViewById(R.id.etPlanta);
        EditText etEspacio = dialogView.findViewById(R.id.etEspacio);

        // Pre-fill
        etNumSerie.setText(articulo.getNumSerie());
        etArticulo.setText(articulo.getArticulo());
        etEstado.setText(articulo.getEstado());
        etMarca.setText(articulo.getMarca());
        etModelo.setText(articulo.getModelo());
        etSubsede.setText(articulo.getSubsede());
        etPabellon.setText(articulo.getPabellon());
        etPlanta.setText(articulo.getPlanta());
        etEspacio.setText(articulo.getEspacio());

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    String oldSerie = articulo.getNumSerie();
                    articulo.setNumSerie(etNumSerie.getText().toString());
                    articulo.setArticulo(etArticulo.getText().toString());
                    articulo.setEstado(etEstado.getText().toString());
                    articulo.setMarca(etMarca.getText().toString());
                    articulo.setModelo(etModelo.getText().toString());
                    articulo.setSubsede(etSubsede.getText().toString());
                    articulo.setPabellon(etPabellon.getText().toString());
                    articulo.setPlanta(etPlanta.getText().toString());
                    articulo.setEspacio(etEspacio.getText().toString());
                    
                    db.insertarArticuloCompleto(articulo); // Replaces if exists
                    actualizarUI();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void volverAEscnear(View view) {
        finish();
    }
}

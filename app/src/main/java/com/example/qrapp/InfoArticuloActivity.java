package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar verificación")
                .setMessage("¿Estás seguro de que quieres marcar este equipo como verificado?")
                .setPositiveButton("Verificar", (dialog, which) -> {
                    String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    db.actualizarVerificadoCAU(articulo.getNumSerie(), fechaActual);
                    
                    try {
                        articulo.setVerificadoCAU(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fechaActual));
                    } catch (Exception e) {}
                    
                    actualizarUI();
                    Toast.makeText(this, "Artículo verificado correctamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void darDeBajaArticulo(View view) {
        if (articulo == null) return;

        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirmar baja")
                .setMessage("¿Estás seguro de que quieres dar de baja este artículo?")
                .setPositiveButton("Dar de baja", (dialog, which) -> {
                    String fechaActual = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                    db.actualizarFechaBaja(articulo.getNumSerie(), fechaActual);
                    
                    articulo.setFechaBaja(fechaActual);
                    actualizarUI();
                    Toast.makeText(this, "Artículo dado de baja correctamente", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void mostrarDialogoUbicacion(View view){
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_formulario, null);
        
        EditText etInventario = dialogView.findViewById(R.id.etInventario);
        EditText etExpediente = dialogView.findViewById(R.id.etExpediente);
        EditText etNumSerie = dialogView.findViewById(R.id.etNumSerie);
        EditText etArticulo = dialogView.findViewById(R.id.etArticulo);
        EditText etEstado = dialogView.findViewById(R.id.etEstado);
        EditText etMarca = dialogView.findViewById(R.id.etMarca);
        EditText etModelo = dialogView.findViewById(R.id.etModelo);
        EditText etSubsede = dialogView.findViewById(R.id.etSubsede);
        EditText etPabellon = dialogView.findViewById(R.id.etPabellon);
        EditText etPlanta = dialogView.findViewById(R.id.etPlanta);
        EditText etEspacio = dialogView.findViewById(R.id.etEspacio);
        EditText etDescEspacio = dialogView.findViewById(R.id.etDescEspacio);
        EditText etDestino = dialogView.findViewById(R.id.etDestino);
        EditText etFamilia = dialogView.findViewById(R.id.etFamilia);
        EditText etSubfamilia = dialogView.findViewById(R.id.etSubfamilia);
        EditText etSubtipo = dialogView.findViewById(R.id.etSubtipo);
        EditText etProveedor = dialogView.findViewById(R.id.etProveedor);
        EditText etIdPatrimonial = dialogView.findViewById(R.id.etIdPatrimonial);
        EditText etPrestamos = dialogView.findViewById(R.id.etPrestamos);
        EditText etFinGarantia = dialogView.findViewById(R.id.etFinGarantia);
        EditText etPropietario = dialogView.findViewById(R.id.etPropietario);
        EditText etObservaciones = dialogView.findViewById(R.id.etObservaciones);

        etInventario.setText(articulo.getInventario());
        etExpediente.setText(articulo.getExpediente());
        etNumSerie.setText(articulo.getNumSerie());
        etArticulo.setText(articulo.getArticulo());
        etEstado.setText(articulo.getEstado());
        etMarca.setText(articulo.getMarca());
        etModelo.setText(articulo.getModelo());
        etSubsede.setText(articulo.getSubsede());
        etPabellon.setText(articulo.getPabellon());
        etPlanta.setText(articulo.getPlanta());
        etEspacio.setText(articulo.getEspacio());
        etDescEspacio.setText(articulo.getDescripcionEspacio());
        etDestino.setText(articulo.getDestinoDotacion());
        etFamilia.setText(articulo.getFamilia());
        etSubfamilia.setText(articulo.getSubfamilia());
        etSubtipo.setText(articulo.getSubtipo());
        etProveedor.setText(articulo.getProveedor());
        etIdPatrimonial.setText(articulo.getIdPatrimonial());
        etPrestamos.setText(articulo.getPrestamosReservas());
        etFinGarantia.setText(articulo.getfFinGarantia());
        etPropietario.setText(articulo.getPropietario());
        etObservaciones.setText(articulo.getObservaciones());

        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("Actualizar", (dialog, which) -> {
                    if (validarCamposDialog(etInventario, etExpediente, etNumSerie, etArticulo, etEstado, etMarca, etModelo, etSubsede, etPabellon, etPlanta, etEspacio, etDescEspacio, etDestino, etFamilia, etSubfamilia, etSubtipo, etProveedor, etIdPatrimonial, etPrestamos, etFinGarantia, etPropietario, etObservaciones)) {
                        articulo.setInventario(etInventario.getText().toString().trim());
                        articulo.setExpediente(etExpediente.getText().toString().trim());
                        articulo.setNumSerie(etNumSerie.getText().toString().trim());
                        articulo.setArticulo(etArticulo.getText().toString().trim());
                        articulo.setEstado(etEstado.getText().toString().trim());
                        articulo.setMarca(etMarca.getText().toString().trim());
                        articulo.setModelo(etModelo.getText().toString().trim());
                        articulo.setSubsede(etSubsede.getText().toString().trim());
                        articulo.setPabellon(etPabellon.getText().toString().trim());
                        articulo.setPlanta(etPlanta.getText().toString().trim());
                        articulo.setEspacio(etEspacio.getText().toString().trim());
                        articulo.setDescripcionEspacio(etDescEspacio.getText().toString().trim());
                        articulo.setDestinoDotacion(etDestino.getText().toString().trim());
                        articulo.setFamilia(etFamilia.getText().toString().trim());
                        articulo.setSubfamilia(etSubfamilia.getText().toString().trim());
                        articulo.setSubtipo(etSubtipo.getText().toString().trim());
                        articulo.setProveedor(etProveedor.getText().toString().trim());
                        articulo.setIdPatrimonial(etIdPatrimonial.getText().toString().trim());
                        articulo.setPrestamosReservas(etPrestamos.getText().toString().trim());
                        articulo.setfFinGarantia(etFinGarantia.getText().toString().trim());
                        articulo.setPropietario(etPropietario.getText().toString().trim());
                        articulo.setObservaciones(etObservaciones.getText().toString().trim());
                        
                        db.insertarArticuloCompleto(articulo);
                        actualizarUI();
                        Toast.makeText(this, "Artículo actualizado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean validarCamposDialog(EditText... edits) {
        for (EditText et : edits) {
            if (et.getText().toString().trim().isEmpty()) return false;
        }
        return true;
    }

    public void volverAEscnear(View view) {
        finish();
    }
}

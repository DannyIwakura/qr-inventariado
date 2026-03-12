package com.example.qrapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.qrapp.model.Articulo;
import com.google.android.material.card.MaterialCardView;

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

        if (articulo == null) return;

        // fecha actual
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // actualizar en la BD
        db.actualizarVerificadoCAU(articulo.getNumSerie(), fechaActual);

    }
}
package com.example.qrapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BusquedaActivity extends AppCompatActivity {

    Button btnVolver;
    private TextView mensajeConf;
    private ActivityResultLauncher<String[]> selectCSV;
    private Uri csvURI;
    private DatabaseHelper databaseHelper;
    private MaterialButton busquedaButton;
    private TextInputEditText busquedaInput;
    private TextInputLayout inputLayoutSerie;
    private MaterialCardView cardResultados;
    private TextView tvArticulo;
    private TextView tvEstado;
    private TextView tvCentro;
    private TextView tvSubsede;
    private TextView tvPabellon;
    private TextView tvPlanta;
    private TextView tvAula;
    private TextView tvMarca;
    private TextView tvModelo;
    private TextView tvVerificacionCAU;
    //Sacar fecha actual
    Date fecha = new Date();

    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    String fechaActual = formato.format(fecha);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busqueda);
        mensajeConf = findViewById(R.id.mensajeConf);
        busquedaButton = findViewById(R.id.busquedaButton);
        busquedaInput = findViewById(R.id.busquedaInput);
        inputLayoutSerie = findViewById(R.id.inputLayoutSerie);
        cardResultados = findViewById(R.id.cardResultados);
        tvArticulo = findViewById(R.id.tvArticulo);
        tvEstado = findViewById(R.id.tvEstado);
        tvCentro = findViewById(R.id.tvCentro);
        tvSubsede = findViewById(R.id.tvSubsede);
        tvPabellon = findViewById(R.id.tvPabellon);
        tvPlanta = findViewById(R.id.tvPlanta);
        tvAula = findViewById(R.id.tvAula);
        tvMarca = findViewById(R.id.tvMarca);
        tvModelo = findViewById(R.id.tvModelo);
        tvVerificacionCAU = findViewById(R.id.tvConfirmacionCAU);
        btnVolver = findViewById(R.id.btnVolver);
        busquedaInput.setVisibility(View.GONE);
        busquedaButton.setVisibility(View.GONE);

        btnVolver.setOnClickListener(v -> {
            finish();
        });

        //registramos el selector de csv al inicio
        selectCSV = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        csvURI = uri;

                        mensajeConf.setText("CSV Cargado Correctamente. Pulse en importar para cargarlo en memmoria y poder realizar búsquedas.");
                    }
                }
        );

        //iniviamos la intancia de la base de datos
        databaseHelper = new DatabaseHelper(this);
    }

    public void SelectCSV(View view) {
        selectCSV.launch(
                new String[]{
                        "text/*",
                        "application/vnd.ms-excel\""
        });
    }

    public void importarCSV (View view) {
        if (csvURI == null) {
            mensajeConf.setText("Seleccione primero un CSV.");
            return;
        }

        int filasInsertadas = 0;

        try {
            InputStream inputStream = getContentResolver().openInputStream(csvURI);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String linea;
            boolean primeraLinea = true;

            while ((linea = reader.readLine()) != null) {

                // Saltar cabecera
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] columnas = linea.split(";", -1);

                if (columnas.length < 25) continue;

                String numSerie = columnas[2].trim();
                String estado = columnas[3].trim();
                String articulo = columnas[4].trim();
                String marca = columnas[5].trim();
                String modelo = columnas[7].trim();
                String centro = columnas[8].trim();
                String subsede = columnas[9].trim();
                String pabellon = columnas[10].trim();
                String planta = columnas[11].trim();
                String aula = columnas[12].trim();
                String verificadoCAU = columnas[21].trim();

                databaseHelper.insertarArticuloCompleto(
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
                filasInsertadas++;
            }

            reader.close();

            mensajeConf.setText("Importación completada correctamente.");
            inputLayoutSerie.setVisibility(View.VISIBLE);
            inputLayoutSerie.setEnabled(true);
            inputLayoutSerie.setHintEnabled(true);
            inputLayoutSerie.requestLayout();

            busquedaButton.setVisibility(View.VISIBLE);
            busquedaInput.setVisibility(View.VISIBLE);
            busquedaInput.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeConf.setText("Error al importar CSV.");
        }
    }

    public void buscar(View view) {

            //Recojo el numero de serie que escribe el usuario y lo ponemos en mayusculas
            String numSerie = busquedaInput.getText().toString().toUpperCase();

            //Convoco la consultar y me traigo el articulo encontrado
            Articulo articuloRecuperado = databaseHelper.consultarPorNumSerie(numSerie);

            if (articuloRecuperado != null) {
                mostrarArticulo(articuloRecuperado);
            } else {
                mensajeConf.setText("No se encontró ningún resultado");
            }


    }

    private void mostrarArticulo(Articulo articulo) {

        tvArticulo.setText("Artículo: " + articulo.getArticulo());
        tvEstado.setText("Estado: " + articulo.getEstado());
        tvCentro.setText("Centro: " + articulo.getCentro());
        tvSubsede.setText("Subsede: " + articulo.getSubsede());
        tvPabellon.setText("Pabellón: " + articulo.getPabellon());
        tvPlanta.setText("Planta: " + articulo.getPlanta());
        tvAula.setText("Aula: " + articulo.getAula());
        tvMarca.setText("Marca: " + articulo.getMarca());
        tvModelo.setText("Modelo: " + articulo.getModelo());
        Date fecha = articulo.getVerificadoCAU();

        if (fecha != null) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Log.d("CAU"," Fecha CAU: " + fecha);
            tvVerificacionCAU.setText("Verificación CAU: " + formato.format(fecha));
        } else {
            tvVerificacionCAU.setText("Verificación CAU: No verificado");
        }

        cardResultados.setVisibility(View.VISIBLE);
    }

}
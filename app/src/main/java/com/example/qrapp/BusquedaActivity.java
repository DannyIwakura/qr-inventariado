package com.example.qrapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private LinearLayout containerResultados;

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
        containerResultados = findViewById(R.id.containerResultados);
        btnVolver = findViewById(R.id.btnVolver);
        
        busquedaInput.setVisibility(View.GONE);
        busquedaButton.setVisibility(View.GONE);

        btnVolver.setOnClickListener(v -> finish());

        selectCSV = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        csvURI = uri;
                        mensajeConf.setText("CSV Cargado Correctamente. Pulse en importar.");
                    }
                }
        );

        databaseHelper = new DatabaseHelper(this);
    }

    public void SelectCSV(View view) {
        selectCSV.launch(new String[]{"text/*", "application/vnd.ms-excel"});
    }

    public void importarCSV (View view) {
        if (csvURI == null) {
            mensajeConf.setText("Seleccione primero un CSV.");
            return;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(csvURI);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String linea;
            boolean primeraLinea = true;
            int filasInsertadas = 0;

            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] col = linea.split(";", -1);
                if (col.length < 25) continue;

                Articulo a = new Articulo();
                a.setInventario(col[0].trim());
                a.setExpediente(col[1].trim());
                a.setNumSerie(col[2].trim());
                a.setEstado(col[3].trim());
                a.setArticulo(col[4].trim());
                a.setMarca(col[5].trim());
                a.setDescripcionEspacio(col[6].trim());
                a.setModelo(col[7].trim());
                a.setDestinoDotacion(col[8].trim());
                a.setSubsede(col[9].trim());
                a.setPabellon(col[10].trim());
                a.setPlanta(col[11].trim());
                a.setEspacio(col[12].trim());
                a.setFamilia(col[13].trim());
                a.setSubfamilia(col[14].trim());
                a.setSubtipo(col[15].trim());
                a.setProveedor(col[16].trim());
                a.setIdPatrimonial(col[17].trim());
                a.setPrestamosReservas(col[18].trim());
                a.setfFinGarantia(col[19].trim());
                a.setFechaBaja(col[20].trim());
                
                String verificadoStr = col[21].trim();
                if (!verificadoStr.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        a.setVerificadoCAU(sdf.parse(verificadoStr));
                    } catch (Exception e) {}
                }
                
                a.setPropietario(col[22].trim());
                a.setUsuario(col[23].trim());
                a.setObservaciones(col[24].trim());

                databaseHelper.insertarArticuloCompleto(a);
                filasInsertadas++;
            }

            reader.close();
            mensajeConf.setText("Importación completada: " + filasInsertadas + " filas.");
            inputLayoutSerie.setVisibility(View.VISIBLE);
            busquedaButton.setVisibility(View.VISIBLE);
            busquedaInput.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            mensajeConf.setText("Error al importar CSV.");
        }
    }

    public void buscar(View view) {
        String numSerie = busquedaInput.getText().toString().toUpperCase();
        Articulo art = databaseHelper.consultarPorNumSerie(numSerie);

        if (art != null) {
            mostrarArticulo(art);
        } else {
            mensajeConf.setText("No se encontró ningún resultado");
            cardResultados.setVisibility(View.GONE);
        }
    }

    private void mostrarArticulo(Articulo a) {
        containerResultados.removeAllViews();
        
        addField("Inventario", a.getInventario());
        addField("Expediente", a.getExpediente());
        addField("Nº Serie", a.getNumSerie());
        addField("Estado", a.getEstado());
        addField("Artículo", a.getArticulo());
        addField("Marca", a.getMarca());
        addField("Descripción Espacio", a.getDescripcionEspacio());
        addField("Modelo", a.getModelo());
        addField("Destino Dotación", a.getDestinoDotacion());
        addField("Subsede", a.getSubsede());
        addField("Pabellón", a.getPabellon());
        addField("Planta", a.getPlanta());
        addField("Espacio", a.getEspacio());
        addField("Familia", a.getFamilia());
        addField("Subfamilia", a.getSubfamilia());
        addField("Subtipo", a.getSubtipo());
        addField("Proveedor", a.getProveedor());
        addField("Id Patrimonial", a.getIdPatrimonial());
        addField("Prestamos/Reservas", a.getPrestamosReservas());
        addField("F. Fin Garantía", a.getfFinGarantia());
        addField("Fecha Baja", a.getFechaBaja());
        
        String fechaCAU = "No verificado";
        if (a.getVerificadoCAU() != null) {
            fechaCAU = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(a.getVerificadoCAU());
        }
        addField("Verificado CAU", fechaCAU);
        
        addField("Propietario", a.getPropietario());
        addField("Usuario", a.getUsuario());
        addField("Observaciones", a.getObservaciones());

        cardResultados.setVisibility(View.VISIBLE);
    }

    private void addField(String label, String value) {
        TextView tv = new TextView(this);
        tv.setText(label + ": " + (value != null ? value : ""));
        tv.setPadding(0, 4, 0, 4);
        containerResultados.addView(tv);
    }
}

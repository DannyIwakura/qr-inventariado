package com.example.qrapp;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BusquedaActivity extends AppCompatActivity {

    private TextView mensajeConf;
    private ActivityResultLauncher<String[]> selectCSV;
    private Uri csvURI;
    private DatabaseHelper databaseHelper;
    private TextInputEditText busquedaInput, filterArticulo, filterSubsede;
    private MaterialCardView cardResultados;
    private LinearLayout containerResultados, extraFiltersContainer;
    private MaterialButton btnShowFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_busqueda);
        
        // Inicialización de vistas
        mensajeConf = findViewById(R.id.mensajeConf);
        busquedaInput = findViewById(R.id.busquedaInput);
        filterArticulo = findViewById(R.id.filterArticulo);
        filterSubsede = findViewById(R.id.filterSubsede);
        cardResultados = findViewById(R.id.cardResultados);
        containerResultados = findViewById(R.id.containerResultados);
        extraFiltersContainer = findViewById(R.id.extraFiltersContainer);
        btnShowFilters = findViewById(R.id.btnShowFilters);
        
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());

        // Mostrar/ocultar filtros avanzados
        btnShowFilters.setOnClickListener(v -> {
            if (extraFiltersContainer.getVisibility() == View.GONE) {
                extraFiltersContainer.setVisibility(View.VISIBLE);
                btnShowFilters.setText("Ocultar filtros");
            } else {
                extraFiltersContainer.setVisibility(View.GONE);
                btnShowFilters.setText("Filtros avanzados");
            }
        });

        selectCSV = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        csvURI = uri;
                        mensajeConf.setText("CSV Cargado: " + uri.getLastPathSegment());
                    }
                }
        );

        databaseHelper = new DatabaseHelper(this);
    }

    public void SelectCSV(View view) {
        selectCSV.launch(new String[]{"text/*", "application/vnd.ms-excel", "text/comma-separated-values"});
    }

    public void importarCSV (View view) {
        if (csvURI == null) {
            Toast.makeText(this, "Seleccione primero un CSV", Toast.LENGTH_SHORT).show();
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
            mensajeConf.setText("Base de datos actualizada: " + filasInsertadas + " registros.");
            Toast.makeText(this, "Importación completada", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            mensajeConf.setText("Error al importar CSV");
        }
    }

    public void buscar(View view) {
        String query = busquedaInput.getText().toString();
        String artNombre = filterArticulo.getText().toString();
        String subsede = filterSubsede.getText().toString();

        List<Articulo> resultados = databaseHelper.buscarArticulos(query, artNombre, subsede);

        containerResultados.removeAllViews();
        // Comprobar si vienen o no resultados
        if (resultados != null && !resultados.isEmpty()) {
            cardResultados.setVisibility(View.VISIBLE);
            for (Articulo a : resultados) {
                agregarTarjetaResultado(a);
            }
        } else {
            cardResultados.setVisibility(View.GONE);
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarTarjetaResultado(Articulo a) {
        View item = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
        TextView text1 = item.findViewById(android.R.id.text1);
        TextView text2 = item.findViewById(android.R.id.text2);

        text1.setText(a.getArticulo() + " (" + a.getNumSerie() + ")");
        text1.setTextColor(getResources().getColor(R.color.azul_marino));
        text1.setTypeface(null, android.graphics.Typeface.BOLD);

        text2.setText("Inv: " + a.getInventario() + " | Sede: " + a.getSubsede() + "\n" + a.getModelo());
        
        item.setPadding(0, 16, 0, 16);
        item.setOnClickListener(v -> {
            // Aquí se podría abrir InfoArticuloActivity pasando el objeto o el ID
            Toast.makeText(this, "Ver detalles de: " + a.getNumSerie(), Toast.LENGTH_SHORT).show();
        });

        containerResultados.addView(item);
        
        // Añadir una línea separadora
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        divider.setBackgroundColor(0xFFEEEEEE);
        containerResultados.addView(divider);
    }
}

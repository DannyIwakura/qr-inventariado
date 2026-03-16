package com.example.qrapp;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrapp.model.Articulo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    public void importarCSV(View view) {
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
                String verificadoCAUStr = columnas[21].trim();

                Date verificadoCAUDate = null;
                if (!verificadoCAUStr.isEmpty()) {
                    try {
                        verificadoCAUDate = formato.parse(verificadoCAUStr);
                    } catch (Exception e) {
                        Log.e("CSV", "Error al parsear fecha: " + verificadoCAUStr);
                    }
                }

                databaseHelper.insertarArticulo(
                        new Articulo(
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
                                verificadoCAUDate
                        )
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

    public void exportarACSV(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar exportación")
                .setMessage("Esto exportará toda la base de datos a CSV, ¿seguro que quieres hacelo?")
                .setPositiveButton("Sí", (dialog, which) -> procederAExportar())
                .setNegativeButton("No", null)
                .show();
    }

    private void procederAExportar() {
        // Recupero artículos de la DB
        List<Articulo> articulos = databaseHelper.obtenerTodosLosArticulos();

        if (articulos == null || articulos.isEmpty()) {
            mensajeConf.setText("No hay artículos para exportar.");
            return;
        }

        // Creamos un nuevo CSV
        try {
            File file = new File(getExternalFilesDir(null), "articulos_exportados.csv");
            StringBuilder csvContent = new StringBuilder();

            // Cabecera (respetando las 25 columnas del importador)
            csvContent.append("Inventario;Expediente;Nє Serie;Estado;Artнculo;Marca;Descripciуn Espacio;Modelo;Destino Dotaciуn;Subsede;Pabellуn;Planta;Espacio;Familia;Subfamilia;Subtipo;Proveedor;Id Patrimonial;Prestamos/Reservas;F. Fin Garantнa;Fecha Baja;Verificado CAU;Propietario;Usuario;Observaciones");

            for (Articulo articulo : articulos) {
                String fechaCau = (articulo.getVerificadoCAU() != null) ? formato.format(articulo.getVerificadoCAU()) : "";

                // Construimos la línea respetando los índices del importador (25 columnas en total)
                String linea = String.format(Locale.getDefault(),
                        "%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s",
                        "", // 0: Inventario
                        "", // 1: Expediente
                        articulo.getNumSerie() != null ? articulo.getNumSerie() : "", // 2: Nє Serie
                        articulo.getEstado() != null ? articulo.getEstado() : "", // 3: Estado
                        articulo.getArticulo() != null ? articulo.getArticulo() : "", // 4: Artнculo
                        articulo.getMarca() != null ? articulo.getMarca() : "", // 5: Marca
                        "", // 6: Descripciуn Espacio
                        articulo.getModelo() != null ? articulo.getModelo() : "", // 7: Modelo
                        articulo.getCentro() != null ? articulo.getCentro() : "", // 8: Destino Dotaciуn
                        articulo.getSubsede() != null ? articulo.getSubsede() : "", // 9: Subsede
                        articulo.getPabellon() != null ? articulo.getPabellon() : "", // 10: Pabellуn
                        articulo.getPlanta() != null ? articulo.getPlanta() : "", // 11: Planta
                        articulo.getAula() != null ? articulo.getAula() : "", // 12: Espacio
                        "", "", "", "", "", "", "", "", // 13-20: Varios campos vacíos
                        fechaCau, // 21: Verificado CAU
                        "", "", "" // 22-24: Propietario, Usuario, Observaciones
                );
                csvContent.append("\n").append(linea);
            }

            // Escribimos el contenido en el archivo
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(csvContent.toString().getBytes(StandardCharsets.UTF_8));
            fos.close();

            mensajeConf.setText("Exportación completada: " + file.getName());
            Toast.makeText(this, "Archivo guardado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            Log.d("CSV", "Archivo exportado en: " + file.getAbsolutePath());

        } catch (Exception e) {
            Log.e("CSV", "Error al exportar CSV", e);
            mensajeConf.setText("Error al exportar CSV.");
        }
    }

}

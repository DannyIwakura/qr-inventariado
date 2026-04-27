package com.example.qrapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import java.util.HashSet;
import java.util.Set;

import android.graphics.pdf.PdfDocument;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GenerarQRActivity extends AppCompatActivity {
    private ActivityResultLauncher<String[]> selectorCSV;
    private TextView tvNombreCSV;
    private Uri csvUri;

    //Variables para crear el PDF con los QRS
    //instancia necesario de PDFDocument
    private PdfDocument pdfDocument;
    //Para guardar la pagina por dodne va el PDF
    private  PdfDocument.Page paginaActual;
    //intancia Canvas para poder dibujar en el PDF
    private Canvas canvas;

    //Variables para saber dodne colocar cada QR
    private int paginaActualNum = 1;
    private int qrPorFila = 3;
    private int qrPorColumna = 8;
    private int contadorQR = 0;
    //variable para guardar y pintar el codigo de serie
    private Paint textPaint;

    //Tamano de la hoja A4
    private int pageWidth = 595;
    private int pageHeight = 842;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tvNombreCSV = findViewById(R.id.nombreCSV);
        //registrar selecciotor al inicio
        selectorCSV = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        String nombreCSV = getFileName(uri);
                        csvUri = uri;
                        Log.d("CSV", "CSV QUE SA HA SELECCIONADO: " + nombreCSV);
                        tvNombreCSV.setText(nombreCSV);
                        tvNombreCSV.setVisibility(View.VISIBLE);
                    }
                }
        );

        // Procesar si venimos de "Compartir"
        manejarIntentCompartido();
    }

    public void seleccionarCSV(View view) {
        //lanzar el selector al pulsar
        selectorCSV.launch(new String[]{
                "text/*",
                "application/vnd.ms-excel"
        });
    }

    public void generarQRs(View view) {
        if (csvUri == null) {
            Toast.makeText(this, "Selecciona un CSV primero", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                //Flujo de lectura y creación del pdf
                //primero creamos el PDF
                crearPDF();
                //Leemos y vamos generando QRs y incluyendolos en el PDF
                leerCSV(csvUri);
                //Una vez que esten todos guardamos el pdf en descargas
                guardarPDF();
                runOnUiThread(() ->
                        Toast.makeText(this, "QRs generados", Toast.LENGTH_LONG).show()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void leerCSV(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String linea;
        boolean encabezado = true;
        String separador = ";";
        int columnaExtraida = 2;

        Set<String> numerosProcesados = new HashSet<>();

        //no leo el acabezado
        while ((linea = reader.readLine()) != null) {
            if (encabezado) {
                encabezado = false;
                continue;
            }
            //separo las columnas por el separador
            String[] columnas = linea.split(separador);
            //filtro la columna que me interesa
            if (columnas.length > columnaExtraida) {
                String numSerie = columnas[columnaExtraida];
                //se genera el QR si noo esta repetido
                if (numerosProcesados.add(numSerie)) {
                    generarQR(numSerie);
                }
            }
        }
        reader.close();
    }

    private void generarQR(String texto) {
        try {
            //tamano del QR
            int tamanoQR = 300;

            //ajustes necesarios del qr
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 1);

            //encodeamos el QR, pasando el texto a codificar y el tamano del QR
            BitMatrix matrix = new MultiFormatWriter().encode(
                    texto,
                    BarcodeFormat.QR_CODE,
                    tamanoQR,
                    tamanoQR,
                    hints
            );

            //Generamos el WR con Bitmap
            Bitmap bitmap = Bitmap.createBitmap(tamanoQR, tamanoQR, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < tamanoQR; x++) {
                for (int y = 0; y < tamanoQR; y++) {
                    bitmap.setPixel(x, y,
                            matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            incluirEnPDF(bitmap, texto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileName(Uri uri) {
        String result = null;

        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver()
                    .query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int index =
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index >= 0) {
                        result = cursor.getString(index);
                    }
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        if (result == null) {
            // Fallback para URIs tipo file://
            result = uri.getLastPathSegment();
        }

        return result;
    }

    private void crearPDF() {
        //Creamos el PDF
        //iniciamos la variable global al crear el pdf
        textPaint = new Paint();
        //color de letra negro
        textPaint.setColor(Color.BLACK);
        //tamano de fuente 18
        textPaint.setTextSize(18);
        //alineamos el texto
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        pdfDocument = new PdfDocument();
        nuevaPagina();
    }

    private void nuevaPagina() {
        //creamos el PDF usando la variable iniciadas al principio
        PdfDocument.PageInfo pageInfo =
                new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, paginaActualNum).create();

        paginaActual = pdfDocument.startPage(pageInfo);
        //inicamos el canvas con la pagina actual para poder pintar sobre ella
        canvas = paginaActual.getCanvas();
        //Variable auxiliar para ir contando el numero de QRs
        contadorQR = 0;
        //Aumentamos el numero de pagina
        paginaActualNum++;
    }

    private void incluirEnPDF(Bitmap bitmap, String texto) {
        //margen de la pagina
        int margen = 20;
        int margenInterno = 10;
        //margen entre cada QR
        int alturaTexto = 35;
        int espacioX = (pageWidth - margen * 2) / qrPorFila;
        int espacioY = (pageHeight - margen * 2) / qrPorColumna;
        //fila y columna para calcular la pocisicón de cada uno
        int fila = contadorQR / qrPorFila;
        int columna = contadorQR % qrPorFila;

        int posX = margen + columna * espacioX;
        int posY = margen + fila * espacioY;
        //espacio entre el QR y el texto

        //los QRs se ven ecogidos y no cadrados perfectos, por ellos metemos este ajuste
        int lado = Math.min(espacioX, espacioY - alturaTexto - margenInterno * 2);

        Bitmap scaled = Bitmap.createScaledBitmap(
                bitmap,
                lado,
                lado,
                true
        );

        // centrar QR
        int offsetX = posX + (espacioX - lado) / 2;
        int offsetY = posY + (espacioY - lado) / 2;

        //colocamos el QR usando las posiciones anteriores
        canvas.drawBitmap(scaled, offsetX, offsetY, null);

        //dibujamos el texto justo debajo del QR
        float textX = posX + espacioX / 2f;
        float textY = offsetY + lado + 25;
        canvas.drawText(texto, textX, textY, textPaint);

        contadorQR++;

        if (contadorQR >= qrPorFila * qrPorColumna) {
            pdfDocument.finishPage(paginaActual);
            nuevaPagina();
        }
    }

    private void guardarPDF() throws IOException {

        pdfDocument.finishPage(paginaActual);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, "QR_pegatinas.pdf");
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        values.put(MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/QRs");

        Uri uri = getContentResolver().insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                values
        );

        OutputStream os = getContentResolver().openOutputStream(uri);
        pdfDocument.writeTo(os);

        os.close();
        pdfDocument.close();
    }

    // Función para importar el CSV cuando se comparte con "Compartir con..."
    private void manejarIntentCompartido() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            // Caso 1: "Compartir con..."
            Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (uri != null) {
                cargarCSVDesdeUri(uri);
            }
        } else if (Intent.ACTION_VIEW.equals(action)) {
            // Caso: Abrir archivo directamente
            Uri uri = intent.getData();
            if (uri != null) {
                cargarCSVDesdeUri(uri);
            }
        }
    }

    private void cargarCSVDesdeUri(Uri uri) {
        this.csvUri = uri;
        String nombreCSV = getFileName(uri);
        tvNombreCSV.setText(nombreCSV);
        tvNombreCSV.setVisibility(View.VISIBLE);
        Toast.makeText(this, "CSV cargado correctamente", Toast.LENGTH_SHORT).show();
    }
}
package com.example.qrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.qrapp.model.Articulo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "inventario.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "articulos";
    public static final String COL_ID = "id";
    public static final String COL_NUMSERIE = "numSerie";
    public static final String COL_ARTICULO = "articulo";
    public static final String COL_ESTADO = "estado";
    public static final String COL_CENTRO = "centro";
    public static final String COL_SUBSEDE = "subsede";
    public static final String COL_PABELLON = "pabellon";
    public static final String COL_PLANTA = "planta";
    public static final String COL_AULA = "aula";
    public static final String COL_MARCA = "marca";
    public static final String COL_MODELO = "modelo";
    public static final String COL_VERIFICADOCAU = "verificadoCAU";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NUMSERIE + " TEXT UNIQUE,"
                + COL_ARTICULO + " TEXT,"
                + COL_ESTADO + " TEXT,"
                + COL_CENTRO + " TEXT,"
                + COL_SUBSEDE + " TEXT,"
                + COL_PABELLON + " TEXT,"
                + COL_PLANTA + " TEXT,"
                + COL_AULA + " TEXT,"
                + COL_MARCA + " TEXT,"
                + COL_MODELO + " TEXT,"
                + COL_VERIFICADOCAU + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * Inserta un objeto Articulo en la base de datos.
     */
    public void insertarArticulo(Articulo articulo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(COL_NUMSERIE, articulo.getNumSerie());
        values.put(COL_ARTICULO, articulo.getArticulo());
        values.put(COL_ESTADO, articulo.getEstado());
        values.put(COL_CENTRO, articulo.getCentro());
        values.put(COL_SUBSEDE, articulo.getSubsede());
        values.put(COL_PABELLON, articulo.getPabellon());
        values.put(COL_PLANTA, articulo.getPlanta());
        values.put(COL_AULA, articulo.getAula());
        values.put(COL_MARCA, articulo.getMarca());
        values.put(COL_MODELO, articulo.getModelo());
        
        String fechaStr = articulo.getVerificadoCAU() != null ? dateFormat.format(articulo.getVerificadoCAU()) : null;
        values.put(COL_VERIFICADOCAU, fechaStr);

        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Articulo consultarPorNumSerie(String nSerie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Articulo articulo = null;

        try (Cursor cursor = db.query(
                TABLE_NAME,
                null,
                COL_NUMSERIE + " = ?",
                new String[]{nSerie},
                null,
                null,
                null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                articulo = mapCursorToArticulo(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error consultando artículo por número de serie", e);
        }

        return articulo;
    }

    private Articulo mapCursorToArticulo(Cursor cursor) {
        Articulo articulo = new Articulo();
        articulo.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
        articulo.setNumSerie(cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMSERIE)));
        articulo.setArticulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICULO)));
        articulo.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)));
        articulo.setCentro(cursor.getString(cursor.getColumnIndexOrThrow(COL_CENTRO)));
        articulo.setSubsede(cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBSEDE)));
        articulo.setPabellon(cursor.getString(cursor.getColumnIndexOrThrow(COL_PABELLON)));
        articulo.setPlanta(cursor.getString(cursor.getColumnIndexOrThrow(COL_PLANTA)));
        articulo.setAula(cursor.getString(cursor.getColumnIndexOrThrow(COL_AULA)));
        articulo.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA)));
        articulo.setModelo(cursor.getString(cursor.getColumnIndexOrThrow(COL_MODELO)));

        String fechaStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_VERIFICADOCAU));
        if (fechaStr != null && !fechaStr.isEmpty()) {
            try {
                articulo.setVerificadoCAU(dateFormat.parse(fechaStr));
            } catch (ParseException e) {
                Log.e(TAG, "Error parseando fecha: " + fechaStr, e);
            }
        }
        return articulo;
    }

    public boolean existeArticulo(String numSerie) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_NUMSERIE + " = ? LIMIT 1";
        try (Cursor cursor = db.rawQuery(query, new String[]{numSerie})) {
            return cursor.moveToFirst();
        }
    }

    public void actualizarVerificadoCAU(String numSerie, Date fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VERIFICADOCAU, dateFormat.format(fecha));

        db.update(TABLE_NAME, values, COL_NUMSERIE + " = ?", new String[]{numSerie});
    }

    public List<Articulo> obtenerTodosLosArticulos() {
        List<Articulo> articulos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME;
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Articulo articulo = mapCursorToArticulo(cursor);
                    articulos.add(articulo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error obteniendo todos los artículos", e);
        }
        return articulos;
    }

}

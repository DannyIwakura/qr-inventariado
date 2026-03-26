package com.example.qrapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qrapp.model.Articulo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

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

    public void insertarArticuloCompleto(
            String numSerie,
            String articulo,
            String estado,
            String centro,
            String subsede,
            String pabellon,
            String planta,
            String aula,
            String marca,
            String modelo,
            String verificadoCAU
    ) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NUMSERIE, numSerie);
        values.put(COL_ARTICULO, articulo);
        values.put(COL_ESTADO, estado);
        values.put(COL_CENTRO, centro);
        values.put(COL_SUBSEDE, subsede);
        values.put(COL_PABELLON, pabellon);
        values.put(COL_PLANTA, planta);
        values.put(COL_AULA, aula);
        values.put(COL_MARCA, marca);
        values.put(COL_MODELO, modelo);
        values.put(COL_VERIFICADOCAU, verificadoCAU);

        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Articulo consultarPorNumSerie(String nSerie) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                "numSerie = ?",
                new String[]{nSerie},
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            // Convertir fecha
            String fechaStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_VERIFICADOCAU));
            Date fechaVerificado = null;

            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    fechaVerificado = formato.parse(fechaStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Articulo articulo = new Articulo(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMSERIE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICULO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_CENTRO)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBSEDE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PABELLON)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PLANTA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_AULA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_MODELO)),
                    fechaVerificado
            );

            cursor.close();
            return articulo;
        }

        cursor.close();
        return null;
    }

    public boolean existeArticulo(String numSerie) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Busacamos el numero de serie que nos pasan
        String query = "SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_NUMSERIE + " = ? LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{numSerie});

        boolean existe = cursor.moveToFirst();

        cursor.close();
        // db.close(); // Generalmente es mejor dejar que el pool gestione esto o cerrar el helper al destruir la actividad

        return existe;
    }

    public void actualizarVerificadoCAU(String numSerie, String fecha){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_VERIFICADOCAU, fecha);

        db.update(TABLE_NAME, values, COL_NUMSERIE + " = ?", new String[]{numSerie});
    }

    public void actualizarUbicacion(String numSerie, String pabellon, String planta, String aula){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_PABELLON, pabellon);
        values.put(COL_PLANTA, planta);
        values.put(COL_AULA, aula);

        db.update(TABLE_NAME, values, COL_NUMSERIE + " = ?", new String[]{numSerie});
    }



}

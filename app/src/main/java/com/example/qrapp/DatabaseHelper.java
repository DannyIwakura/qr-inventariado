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
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "articulos";
    public static final String COL_ID = "id";
    public static final String COL_INVENTARIO = "inventario";
    public static final String COL_EXPEDIENTE = "expediente";
    public static final String COL_NUMSERIE = "numSerie";
    public static final String COL_ESTADO = "estado";
    public static final String COL_ARTICULO = "articulo";
    public static final String COL_MARCA = "marca";
    public static final String COL_DESC_ESPACIO = "descripcionEspacio";
    public static final String COL_MODELO = "modelo";
    public static final String COL_DESTINO = "destinoDotacion";
    public static final String COL_SUBSEDE = "subsede";
    public static final String COL_PABELLON = "pabellon";
    public static final String COL_PLANTA = "planta";
    public static final String COL_ESPACIO = "espacio";
    public static final String COL_FAMILIA = "familia";
    public static final String COL_SUBFAMILIA = "subfamilia";
    public static final String COL_SUBTIPO = "subtipo";
    public static final String COL_PROVEEDOR = "proveedor";
    public static final String COL_ID_PATRIMONIAL = "idPatrimonial";
    public static final String COL_PRESTAMOS = "prestamosReservas";
    public static final String COL_F_FIN_GARANTIA = "fFinGarantia";
    public static final String COL_FECHA_BAJA = "fechaBaja";
    public static final String COL_VERIFICADOCAU = "verificadoCAU";
    public static final String COL_PROPIETARIO = "propietario";
    public static final String COL_USUARIO = "usuario";
    public static final String COL_OBSERVACIONES = "observaciones";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_INVENTARIO + " TEXT,"
                + COL_EXPEDIENTE + " TEXT,"
                + COL_NUMSERIE + " TEXT UNIQUE,"
                + COL_ESTADO + " TEXT,"
                + COL_ARTICULO + " TEXT,"
                + COL_MARCA + " TEXT,"
                + COL_DESC_ESPACIO + " TEXT,"
                + COL_MODELO + " TEXT,"
                + COL_DESTINO + " TEXT,"
                + COL_SUBSEDE + " TEXT,"
                + COL_PABELLON + " TEXT,"
                + COL_PLANTA + " TEXT,"
                + COL_ESPACIO + " TEXT,"
                + COL_FAMILIA + " TEXT,"
                + COL_SUBFAMILIA + " TEXT,"
                + COL_SUBTIPO + " TEXT,"
                + COL_PROVEEDOR + " TEXT,"
                + COL_ID_PATRIMONIAL + " TEXT,"
                + COL_PRESTAMOS + " TEXT,"
                + COL_F_FIN_GARANTIA + " TEXT,"
                + COL_FECHA_BAJA + " TEXT,"
                + COL_VERIFICADOCAU + " TEXT,"
                + COL_PROPIETARIO + " TEXT,"
                + COL_USUARIO + " TEXT,"
                + COL_OBSERVACIONES + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertarArticuloCompleto(Articulo a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INVENTARIO, a.getInventario());
        values.put(COL_EXPEDIENTE, a.getExpediente());
        values.put(COL_NUMSERIE, a.getNumSerie());
        values.put(COL_ESTADO, a.getEstado());
        values.put(COL_ARTICULO, a.getArticulo());
        values.put(COL_MARCA, a.getMarca());
        values.put(COL_DESC_ESPACIO, a.getDescripcionEspacio());
        values.put(COL_MODELO, a.getModelo());
        values.put(COL_DESTINO, a.getDestinoDotacion());
        values.put(COL_SUBSEDE, a.getSubsede());
        values.put(COL_PABELLON, a.getPabellon());
        values.put(COL_PLANTA, a.getPlanta());
        values.put(COL_ESPACIO, a.getEspacio());
        values.put(COL_FAMILIA, a.getFamilia());
        values.put(COL_SUBFAMILIA, a.getSubfamilia());
        values.put(COL_SUBTIPO, a.getSubtipo());
        values.put(COL_PROVEEDOR, a.getProveedor());
        values.put(COL_ID_PATRIMONIAL, a.getIdPatrimonial());
        values.put(COL_PRESTAMOS, a.getPrestamosReservas());
        values.put(COL_F_FIN_GARANTIA, a.getfFinGarantia());
        values.put(COL_FECHA_BAJA, a.getFechaBaja());
        
        if (a.getVerificadoCAU() != null) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            values.put(COL_VERIFICADOCAU, formato.format(a.getVerificadoCAU()));
        } else {
            values.put(COL_VERIFICADOCAU, (String)null);
        }
        
        values.put(COL_PROPIETARIO, a.getPropietario());
        values.put(COL_USUARIO, a.getUsuario());
        values.put(COL_OBSERVACIONES, a.getObservaciones());

        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Articulo consultarPorNumSerie(String nSerie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COL_NUMSERIE + " = ?", new String[]{nSerie}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Articulo a = new Articulo();
            a.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)));
            a.setInventario(cursor.getString(cursor.getColumnIndexOrThrow(COL_INVENTARIO)));
            a.setExpediente(cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPEDIENTE)));
            a.setNumSerie(cursor.getString(cursor.getColumnIndexOrThrow(COL_NUMSERIE)));
            a.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)));
            a.setArticulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_ARTICULO)));
            a.setMarca(cursor.getString(cursor.getColumnIndexOrThrow(COL_MARCA)));
            a.setDescripcionEspacio(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESC_ESPACIO)));
            a.setModelo(cursor.getString(cursor.getColumnIndexOrThrow(COL_MODELO)));
            a.setDestinoDotacion(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESTINO)));
            a.setSubsede(cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBSEDE)));
            a.setPabellon(cursor.getString(cursor.getColumnIndexOrThrow(COL_PABELLON)));
            a.setPlanta(cursor.getString(cursor.getColumnIndexOrThrow(COL_PLANTA)));
            a.setEspacio(cursor.getString(cursor.getColumnIndexOrThrow(COL_ESPACIO)));
            a.setFamilia(cursor.getString(cursor.getColumnIndexOrThrow(COL_FAMILIA)));
            a.setSubfamilia(cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBFAMILIA)));
            a.setSubtipo(cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBTIPO)));
            a.setProveedor(cursor.getString(cursor.getColumnIndexOrThrow(COL_PROVEEDOR)));
            a.setIdPatrimonial(cursor.getString(cursor.getColumnIndexOrThrow(COL_ID_PATRIMONIAL)));
            a.setPrestamosReservas(cursor.getString(cursor.getColumnIndexOrThrow(COL_PRESTAMOS)));
            a.setfFinGarantia(cursor.getString(cursor.getColumnIndexOrThrow(COL_F_FIN_GARANTIA)));
            a.setFechaBaja(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_BAJA)));
            a.setPropietario(cursor.getString(cursor.getColumnIndexOrThrow(COL_PROPIETARIO)));
            a.setUsuario(cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO)));
            a.setObservaciones(cursor.getString(cursor.getColumnIndexOrThrow(COL_OBSERVACIONES)));

            String fechaStr = cursor.getString(cursor.getColumnIndexOrThrow(COL_VERIFICADOCAU));
            if (fechaStr != null && !fechaStr.isEmpty()) {
                try {
                    SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    a.setVerificadoCAU(formato.parse(fechaStr));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
            return a;
        }
        if (cursor != null) cursor.close();
        return null;
    }

    public boolean existeArticulo(String numSerie) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_NAME + " WHERE " + COL_NUMSERIE + " = ? LIMIT 1", new String[]{numSerie});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    public void actualizarVerificadoCAU(String numSerie, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_VERIFICADOCAU, fecha);
        db.update(TABLE_NAME, values, COL_NUMSERIE + " = ?", new String[]{numSerie});
    }

    public void actualizarFechaBaja(String numSerie, String fecha) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FECHA_BAJA, fecha);
        db.update(TABLE_NAME, values, COL_NUMSERIE + " = ?", new String[]{numSerie});
    }
}

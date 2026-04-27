package com.example.qrapp;

import android.app.Application;

public class GlobalData extends Application {

    //Con esta clase hacemos que el numSerie este disponible en toda la aplicacion
    //Esto sera converitod a una clase Discposivo
    private String numSerie;

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }
}
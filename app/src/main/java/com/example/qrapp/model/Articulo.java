package com.example.qrapp.model;

import java.io.Serializable;
import java.util.Date;

public class Articulo implements Serializable {
    private int id;
    private String numSerie;
    private String articulo;
    private String estado;
    private String centro;
    private String subsede;
    private String pabellon;
    private String planta;
    private String aula;
    private String marca;
    private String modelo;

    private Date verfidacoCAU;

    public Articulo() {
    }

    public Articulo(String numSerie, String articulo, String estado,
                    String centro, String subsede, String pabellon,
                    String planta, String aula, String marca, String modelo,
                    Date verfidacoCAU) {

        this.numSerie = numSerie;
        this.articulo = articulo;
        this.estado = estado;
        this.centro = centro;
        this.subsede = subsede;
        this.pabellon = pabellon;
        this.planta = planta;
        this.aula = aula;
        this.marca = marca;
        this.modelo = modelo;
        this.verfidacoCAU = verfidacoCAU;
    }

    public int getId() { return id; }
    public String getNumSerie() { return numSerie; }
    public String getArticulo() { return articulo; }
    public String getEstado() { return estado; }
    public String getCentro() { return centro; }
    public String getSubsede() { return subsede; }
    public String getPabellon() { return pabellon; }
    public String getPlanta() { return planta; }
    public String getAula() { return aula; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public Date getVerificadoCAU() { return verfidacoCAU; }

    public void setId(int id) { this.id = id; }
    public void setNumSerie(String numSerie) { this.numSerie = numSerie; }
    public void setArticulo(String articulo) { this.articulo = articulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setCentro(String centro) { this.centro = centro; }
    public void setSubsede(String subsede) { this.subsede = subsede; }
    public void setPabellon(String pabellon) { this.pabellon = pabellon; }
    public void setPlanta(String planta) { this.planta = planta; }
    public void setAula(String aula) { this.aula = aula; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setVerfidacoCAU(Date verfidacoCAU) { this.verfidacoCAU = verfidacoCAU; }
}

package com.example.qrapp.model;

import java.io.Serializable;
import java.util.Date;

public class Articulo implements Serializable {
    private int id;
    private String inventario;
    private String expediente;
    private String numSerie;
    private String estado;
    private String articulo;
    private String marca;
    private String descripcionEspacio;
    private String modelo;
    private String destinoDotacion;
    private String subsede;
    private String pabellon;
    private String planta;
    private String espacio;
    private String familia;
    private String subfamilia;
    private String subtipo;
    private String proveedor;
    private String idPatrimonial;
    private String prestamosReservas;
    private String fFinGarantia;
    private String fechaBaja;
    private Date verificadoCAU;
    private String propietario;
    private String usuario;
    private String observaciones;

    public Articulo() {
    }

    public Articulo(String inventario, String expediente, String numSerie, String estado, String articulo,
                    String marca, String descripcionEspacio, String modelo, String destinoDotacion,
                    String subsede, String pabellon, String planta, String espacio, String familia,
                    String subfamilia, String subtipo, String proveedor, String idPatrimonial,
                    String prestamosReservas, String fFinGarantia, String fechaBaja, Date verificadoCAU,
                    String propietario, String usuario, String observaciones) {
        this.inventario = inventario;
        this.expediente = expediente;
        this.numSerie = numSerie;
        this.estado = estado;
        this.articulo = articulo;
        this.marca = marca;
        this.descripcionEspacio = descripcionEspacio;
        this.modelo = modelo;
        this.destinoDotacion = destinoDotacion;
        this.subsede = subsede;
        this.pabellon = pabellon;
        this.planta = planta;
        this.espacio = espacio;
        this.familia = familia;
        this.subfamilia = subfamilia;
        this.subtipo = subtipo;
        this.proveedor = proveedor;
        this.idPatrimonial = idPatrimonial;
        this.prestamosReservas = prestamosReservas;
        this.fFinGarantia = fFinGarantia;
        this.fechaBaja = fechaBaja;
        this.verificadoCAU = verificadoCAU;
        this.propietario = propietario;
        this.usuario = usuario;
        this.observaciones = observaciones;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getInventario() { return inventario; }
    public void setInventario(String inventario) { this.inventario = inventario; }

    public String getExpediente() { return expediente; }
    public void setExpediente(String expediente) { this.expediente = expediente; }

    public String getNumSerie() { return numSerie; }
    public void setNumSerie(String numSerie) { this.numSerie = numSerie; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getArticulo() { return articulo; }
    public void setArticulo(String articulo) { this.articulo = articulo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getDescripcionEspacio() { return descripcionEspacio; }
    public void setDescripcionEspacio(String descripcionEspacio) { this.descripcionEspacio = descripcionEspacio; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getDestinoDotacion() { return destinoDotacion; }
    public void setDestinoDotacion(String destinoDotacion) { this.destinoDotacion = destinoDotacion; }

    public String getSubsede() { return subsede; }
    public void setSubsede(String subsede) { this.subsede = subsede; }

    public String getPabellon() { return pabellon; }
    public void setPabellon(String pabellon) { this.pabellon = pabellon; }

    public String getPlanta() { return planta; }
    public void setPlanta(String planta) { this.planta = planta; }

    public String getEspacio() { return espacio; }
    public void setEspacio(String espacio) { this.espacio = espacio; }

    public String getFamilia() { return familia; }
    public void setFamilia(String familia) { this.familia = familia; }

    public String getSubfamilia() { return subfamilia; }
    public void setSubfamilia(String subfamilia) { this.subfamilia = subfamilia; }

    public String getSubtipo() { return subtipo; }
    public void setSubtipo(String subtipo) { this.subtipo = subtipo; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getIdPatrimonial() { return idPatrimonial; }
    public void setIdPatrimonial(String idPatrimonial) { this.idPatrimonial = idPatrimonial; }

    public String getPrestamosReservas() { return prestamosReservas; }
    public void setPrestamosReservas(String prestamosReservas) { this.prestamosReservas = prestamosReservas; }

    public String getfFinGarantia() { return fFinGarantia; }
    public void setfFinGarantia(String fFinGarantia) { this.fFinGarantia = fFinGarantia; }

    public String getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(String fechaBaja) { this.fechaBaja = fechaBaja; }

    public Date getVerificadoCAU() { return verificadoCAU; }
    public void setVerificadoCAU(Date verificadoCAU) { this.verificadoCAU = verificadoCAU; }

    public String getPropietario() { return propietario; }
    public void setPropietario(String propietario) { this.propietario = propietario; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}

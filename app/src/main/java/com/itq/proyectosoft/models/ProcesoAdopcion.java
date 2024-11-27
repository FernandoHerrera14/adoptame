package com.itq.proyectosoft.models;

public class ProcesoAdopcion {
    private String idProceso;
    private String idAdoptante;
    private String idPet;
    private String fechaInicio;
    private String fechaFin;

    public ProcesoAdopcion(){

    }
    public ProcesoAdopcion(String idProceso, String idAdoptante, String idPet, String fechaInicio, String fechaFin) {
        this.idProceso = idProceso;
        this.idAdoptante = idAdoptante;
        this.idPet = idPet;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public String getIdAdoptante() {
        return idAdoptante;
    }

    public void setIdAdoptante(String idAdoptante) {
        this.idAdoptante = idAdoptante;
    }

    public String getIdPet() {
        return idPet;
    }

    public void setIdPet(String idPet) {
        this.idPet = idPet;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }
}



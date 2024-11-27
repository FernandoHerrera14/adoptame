package com.itq.proyectosoft.models;

public class StatusAdopcion {

    private String idStatus;
    private String idProceso;
    private boolean solicitudRealizada;
    private boolean revisionDocumentos;
    private boolean entrevista;
    private boolean citaEntrega;
    private boolean finProceso;
    private String fechaSolicitud;
    private String fechaRevisionDocumentos;
    private String fechaEntrevista;
    private String fechaCitaEntrega;


    public StatusAdopcion(){

    }

    public StatusAdopcion(String idStatus, String idProceso, boolean solicitudRealizada, boolean revisionDocumentos, boolean entrevista, boolean citaEntrega, boolean finProceso, String fechaSolicitud, String fechaRevisionDocumentos, String fechaEntrevista, String fechaCitaEntrega) {
        this.idStatus = idStatus;
        this.idProceso = idProceso;
        this.solicitudRealizada = solicitudRealizada;
        this.revisionDocumentos = revisionDocumentos;
        this.entrevista = entrevista;
        this.citaEntrega = citaEntrega;
        this.finProceso = finProceso;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaRevisionDocumentos = fechaRevisionDocumentos;
        this.fechaEntrevista = fechaEntrevista;
        this.fechaCitaEntrega = fechaCitaEntrega;
    }

    public String getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(String idStatus) {
        this.idStatus = idStatus;
    }

    public String getIdProceso() {
        return idProceso;
    }

    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }

    public boolean isSolicitudRealizada() {
        return solicitudRealizada;
    }

    public void setSolicitudRealizada(boolean solicitudRealizada) {
        this.solicitudRealizada = solicitudRealizada;
    }

    public boolean isRevisionDocumentos() {
        return revisionDocumentos;
    }

    public void setRevisionDocumentos(boolean revisionDocumentos) {
        this.revisionDocumentos = revisionDocumentos;
    }

    public boolean isEntrevista() {
        return entrevista;
    }

    public void setEntrevista(boolean entrevista) {
        this.entrevista = entrevista;
    }

    public boolean isCitaEntrega() {
        return citaEntrega;
    }

    public void setCitaEntrega(boolean citaEntrega) {
        this.citaEntrega = citaEntrega;
    }

    public boolean isFinProceso() {
        return finProceso;
    }

    public void setFinProceso(boolean finProceso) {
        this.finProceso = finProceso;
    }

    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getFechaRevisionDocumentos() {
        return fechaRevisionDocumentos;
    }

    public void setFechaRevisionDocumentos(String fechaRevisionDocumentos) {
        this.fechaRevisionDocumentos = fechaRevisionDocumentos;
    }

    public String getFechaEntrevista() {
        return fechaEntrevista;
    }

    public void setFechaEntrevista(String fechaEntrevista) {
        this.fechaEntrevista = fechaEntrevista;
    }

    public String getFechaCitaEntrega() {
        return fechaCitaEntrega;
    }

    public void setFechaCitaEntrega(String fechaCitaEntrega) {
        this.fechaCitaEntrega = fechaCitaEntrega;
    }
}

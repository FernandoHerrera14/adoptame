package com.itq.proyectosoft.models;

public class Pets {
    private String id;
    private String caracter;
    private String color;
    private String edad;
    private String historia;
    private String id_adopcion;
    private String imagen;
    private String nombre;
    private String sexo;
    private String tamaño;

    public Pets() {
    }

    public Pets(String id, String caracter, String color, String edad, String historia, String id_adopcion, String imagen, String nombre, String sexo, String tamaño) {
        this.id = id;
        this.caracter = caracter;
        this.color = color;
        this.edad = edad;
        this.historia = historia;
        this.id_adopcion = id_adopcion;
        this.imagen = imagen;
        this.nombre = nombre;
        this.sexo = sexo;
        this.tamaño = tamaño;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCaracter() {
        return caracter;
    }

    public void setCaracter(String caracter) {
        this.caracter = caracter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public String getId_adopcion() {
        return id_adopcion;
    }

    public void setId_adopcion(String id_adopcion) {
        this.id_adopcion = id_adopcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTamaño() {
        return tamaño;
    }

    public void setTamaño(String tamaño) {
        this.tamaño = tamaño;
    }
}

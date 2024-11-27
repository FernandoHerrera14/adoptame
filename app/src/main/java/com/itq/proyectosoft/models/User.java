package com.itq.proyectosoft.models;

public class User {
    private String id;
    private String email;
    private String telefono;

    private String userName;
    private String apellidos;

    private String imgProfile;
    private long timestamp;//Para saber cuando se hizo el usuario

    public User() {
    }

    public User(String id, String email, String telefono,String userName, String apellidos, long timestamp, String imgProfile) {
        this.id = id;
        this.email = email;
        this.telefono = telefono;
        this.userName = userName;
        this.apellidos = apellidos;
        this.timestamp = timestamp;
        this.imgProfile = imgProfile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(String imgProfile) {
        this.imgProfile = imgProfile;
    }
}

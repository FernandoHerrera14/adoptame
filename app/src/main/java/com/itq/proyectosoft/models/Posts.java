package com.itq.proyectosoft.models;

public class Posts {

    private String id;
    private String description;
    private String image;
    private String idUser;
    private String userName;

    private long timestamp;

    public Posts() {
        }

    public Posts(String id, String description, String image, String idUser, String userName, long timestamp) {
        this.id = id;
        this.description = description;
        this.image = image;
        this.idUser = idUser;
        this.userName = userName;
        this.timestamp = timestamp;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

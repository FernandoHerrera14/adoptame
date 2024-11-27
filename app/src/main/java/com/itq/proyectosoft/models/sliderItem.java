package com.itq.proyectosoft.models;

public class sliderItem {

    String imgUrl;
    long timeStamp;

    public sliderItem(){

    }
    public sliderItem(String imgUrl, Long timeStamp) {
        this.imgUrl = imgUrl;
        this.timeStamp = timeStamp;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

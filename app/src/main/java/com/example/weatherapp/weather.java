package com.example.weatherapp;

/**
 *
 */
public class weather {
    private int minTemp;
    private int maxTemp;
    private String description;
    private String imageCode;
    private String date;

    public weather(int minTemp, int maxTemp, String description, String imageCode, String date) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.description = description;
        this.imageCode = imageCode;
        this.date = date;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public String getDescription() {
        return description;
    }

    public String getImageCode() {
        return imageCode;
    }

    public String getDate() {
        return date;
    }
}

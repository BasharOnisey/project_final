package com.example.nasa_image_project;

public class SavedImageModel {
    private int id;
    private String title;
    private String url;
    private String date;
    private String feedback;

    // Full constructor (used when retrieving from database)
    public SavedImageModel(int id, String title, String url, String date, String feedback) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.date = date;
        this.feedback = feedback;
    }

    // Constructor without id (used when inserting into database)
    public SavedImageModel(String title, String url, String date, String feedback) {
        this.title = title;
        this.url = url;
        this.date = date;
        this.feedback = feedback;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getFeedback() {
        return feedback;
    }

    // Setter methods (optional if you want to allow updates)
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

package com.example.sus.Activities.Models;

public class Article_Model {

    private String title;
    private String description;
    private String timestamp;
    private String article_by;

    public Article_Model() {
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String gettimestamp() {
        return timestamp;
    }

    public void settimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getarticle_by() {
        return article_by;
    }

    public void setarticle_by(String article_by) {
        this.article_by = article_by;
    }
}

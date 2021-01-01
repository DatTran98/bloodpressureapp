package com.hust.bloddpressure.model.entities;

public class News {
    private int newId;
    private String contentNew;
    private String titleNew;

    public News(int newId, String titleNew, String contentNew) {
        this.newId = newId;
        this.titleNew = titleNew;
        this.contentNew = contentNew;
    }
    public News( String titleNew, String contentNew) {
        this.titleNew = titleNew;
        this.contentNew = contentNew;
    }

    public String getTitleNew() {
        return titleNew;
    }

    public void setTitleNew(String titleNew) {
        this.titleNew = titleNew;
    }

    public int getNewId() {
        return newId;
    }

    public void setNewId(int newId) {
        this.newId = newId;
    }

    public String getContentNew() {
        return contentNew;
    }

    public void setContentNew(String contentNew) {
        this.contentNew = contentNew;
    }
}

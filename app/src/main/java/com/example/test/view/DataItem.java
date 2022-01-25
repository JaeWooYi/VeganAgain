package com.example.test.view;

public class DataItem {
    private String id;
    private String title;
    private String image;

    public DataItem(String id, String title, String image){
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }


}

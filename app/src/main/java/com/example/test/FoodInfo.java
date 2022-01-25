package com.example.test;

import java.io.Serializable;
import java.util.ArrayList;

public class FoodInfo implements Serializable {
    private String foodImage;
    private String foodTitle;
    private ArrayList list_step;
    private ArrayList list_ingredient;
    private String title;
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public void setList_ingredient(ArrayList list_ingredient) {
        this.list_ingredient = list_ingredient;
    }

    public void setList_step(ArrayList list_step) {
        this.list_step = list_step;
    }

    public ArrayList getList_ingredient() {
        return list_ingredient;
    }

    public ArrayList getList_step() {
        return list_step;
    }
}

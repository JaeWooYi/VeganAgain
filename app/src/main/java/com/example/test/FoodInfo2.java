package com.example.test;

public class FoodInfo2 {

    private Long id ;
    private String foodImage;
    private String foodTitle;
    private String ImageType;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodTitle(String foodTitle) {
        this.foodTitle = foodTitle;
    }

    public String getFoodTitle() {
        return foodTitle;
    }

    public void setImageType(String imageType) {
        this.ImageType = imageType;
    }

    public String getImageType() {
        return ImageType;
    }
}

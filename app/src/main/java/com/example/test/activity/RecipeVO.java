package com.example.test.activity;//package com.example.test.activity;

public class RecipeVO {
    private String name;
    private String car;
    private String pro;
    private String fat;
    private String kcal;

    public RecipeVO(String name, String car, String pro, String fat, String kcal) {
        this.name = name;
        this.car = car;
        this.pro = pro;
        this.fat = fat;
        this.kcal = kcal;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCAR() { return car; }

    public void setCar(String Car){ this.car = Car; }

    public String getPRO() { return pro; }

    public void setPRO(String PRO) { this.pro = PRO; }

    public String getFAT() {return fat; }

    public void setFAT(String FAT) { this.fat = FAT; }

    public String getKCAL() { return kcal; }

    public void setKCAL(String KCAL) { this.kcal = KCAL; }
}

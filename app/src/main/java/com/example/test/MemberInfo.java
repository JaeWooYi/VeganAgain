package com.example.test;

public class MemberInfo {

    private String uid;
    private String gender;
    private String birth;
    private String age;
    private String name;
    private String favorite;
    private String photoUrl;

    public MemberInfo(String uid, String gender, String birth, String age, String name, String favorite, String photoUrl) {
        this.uid = uid;
        this.gender = gender;
        this.birth = birth;
        this.age = age;
        this.name = name;
        this.favorite = favorite;
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

//    private String name;
//    private String phoneNumber;
//    private String birthDay;
//    private String address;
//    private String photoUrl;
//
//    public MemberInfo(String name, String phoneNumber, String birthDay, String address, String photoUrl){
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.birthDay = birthDay;
//        this.address = address;
//        this.photoUrl = photoUrl;
//    }
//
//    public String getName(){
//        return this.name;
//    }
//    public void setName(String name){
//        this.name = name;
//    }
//
//    public String getPhoneNumber(){
//        return this.phoneNumber;
//    }
//    public void setPhoneNumber(String phoneNumber){
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getBirthDay(){
//        return this.birthDay;
//    }
//    public void setBirthDay(String birthDay){
//        this.birthDay = birthDay;
//    }
//
//    public String getAddress(){
//        return this.address;
//    }
//    public void setAddress(String address){
//        this.address = address;
//    }
//
//    public String getPhotoUrl(){
//        return this.photoUrl;
//    }
//    public void setPhotoUrl(String photoUrl){
//        this.photoUrl = photoUrl;
//    }
}

package com.example.dopinpan.Model;

public class Favorites {
    String FoodId,userPhone;

    public Favorites() {
    }

    public Favorites(String foodId, String userPhone) {
        FoodId = foodId;
        this.userPhone = userPhone;
    }

    public String getFoodId() {
        return FoodId;
    }

    public void setFoodId(String foodId) {
        FoodId = foodId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}

package com.example.dopinpan.Model;

public class Food {
    private String Name,Image,Descriptions,Price,Discount,MenuId;

    public Food() {
    }

    public Food(String name, String image, String descriptions, String price, String discount, String menuId) {
        Name = name;
        Image = image;
        Descriptions = descriptions;
        Price = price;
        Discount = discount;
        MenuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}

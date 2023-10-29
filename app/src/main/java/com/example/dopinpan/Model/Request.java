package com.example.dopinpan.Model;

import java.util.List;

public class Request {
    private String phone, name, address, total, status, startAt,moment,month,year,day;
    private List<Order> foods;

    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> foods, String status, String startAt) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status = status;
        this.startAt = startAt;
    }

    public Request(String phone, String name, String address, String total, String status, String startAt, String moment, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.startAt = startAt;
        this.moment = moment;
        this.foods = foods;
    }

    public Request(String phone, String name, String address, String total, String status, String startAt, String day, String month, String year, List<Order> foods) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.status = status;
        this.startAt = startAt;
        this.month = month;
        this.year = year;
        this.day = day;
        this.foods = foods;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}

package com.example.dopinpan.Model;

public class Statictical {
    String day,month,year,total;

    public Statictical() {
    }

    public Statictical(String day, String month, String year, String total) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.total = total;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}

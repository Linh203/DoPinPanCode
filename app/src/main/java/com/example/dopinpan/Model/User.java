package com.example.dopinpan.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String Name, PassWord, Phone, Address, Email, IsStaff,secureCode,avatarUser,startAt;

    public User() {
    }

    public User(String name, String passWord, String phone, String address, String email,String secureCode) {
        Name = name;
        PassWord = passWord;
        Phone = phone;
        Address = address;
        Email = email;
        IsStaff = "false";
        this.secureCode=secureCode;

    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public User(String name, String passWord, String phone, String address, String email, String isStaff, String secureCode, String avatarUser,String startAt) {
        Name = name;
        PassWord = passWord;
        Phone = phone;
        Address = address;
        Email = email;
        this.secureCode = secureCode;
        this.avatarUser = avatarUser;
        IsStaff = isStaff;
        this.startAt=startAt;
    }

    public String getAvatarUser() {
        return avatarUser;
    }

    public void setAvatarUser(String avatarUser) {
        this.avatarUser = avatarUser;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }


}

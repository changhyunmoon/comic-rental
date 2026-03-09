package com.programmers.team6.comic_rental.entity;

public class Member {
    int id;
    String name;
    String phone;// 선택
    String regDate;// yyyy-MM-dd

    public Member(){}

    public Member(int id, String name, String phone, String regDate){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.regDate = regDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public String getRegDate() {
        return regDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}


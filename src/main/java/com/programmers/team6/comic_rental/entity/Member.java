package com.programmers.team6.comic_rental.entity;

public class Member {
    Long id;
    String name;
    String phone;// 선택
    String createDate;// yyyy-MM-dd

    public Member(){}

    public Member(Long id, String name, String phone, String createDate){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.createDate = createDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }
}


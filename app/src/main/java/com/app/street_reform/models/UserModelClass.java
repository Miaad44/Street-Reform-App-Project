package com.app.street_reform.models;

public class UserModelClass {
    private String id;
    private String civilNo;
    private String userName;
    private String email;

    public UserModelClass() {}

    public UserModelClass(String id, String civilNo, String userName, String email) {
        this.id = id;
        this.civilNo = civilNo;
        this.userName = userName;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getCivilNo() {
        return civilNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

}
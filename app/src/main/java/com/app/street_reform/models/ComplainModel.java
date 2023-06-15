package com.app.street_reform.models;

import java.util.List;

public class ComplainModel {
    private String id;
    private String userId;
    private String userName;
    private String userEmail;
    private List<String> urlList;
    private String complainType;
    private String description;
    private String severity;
    private double latitude;
    private double longitude;
    private String date;
    private String time;
    private String status;

    public ComplainModel() {}

    public ComplainModel(String id, String userId, String userName, String userEmail, List<String> urlList,
                         String complainType, String description, String severity, double latitude, double longitude,
                         String date, String time, String status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.urlList = urlList;
        this.complainType = complainType;
        this.description = description;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public String getComplainType() {
        return complainType;
    }

    public String getDescription() {
        return description;
    }

    public String getSeverity() {
        return severity;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}

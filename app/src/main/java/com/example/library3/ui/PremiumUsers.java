package com.example.library3.ui;

public class PremiumUsers {
    private String name;
    private String email;
    private String expire;
    private String plane;
    private String start;

    public PremiumUsers() { //always require no argument constructor for model class

    }

    public PremiumUsers(String name, String email, String expire, String plane, String start) {
        this.name = name;
        this.email = email;
        this.expire = expire;
        this.plane = plane;
        this.start = start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getPlane() {
        return plane;
    }

    public void setPlane(String plane) {
        this.plane = plane;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }
}

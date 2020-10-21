package com.example.library3.ui;

import java.util.HashMap;
import java.util.Map;
//getter setter class
public class Users {
//    Map<String, Object> data = new HashMap<String, Object>();
//
//    public void setValue(Map<String, Object> map)
//    {
//        this.data = map;
//    }
//    public Map<String, Object> getValue()
//    {
//        return this.data;
//    }
//
//

    String name;
     String email;
     String uid;

    public Users() {
    }
    public Users(String name,String email,String uid)
    {
        this.name=name;
        this.email=email;
        this.uid=uid;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
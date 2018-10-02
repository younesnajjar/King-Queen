package com.example.younes.hotornot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 9/19/2018.
 */

public class StudentExistence {

    public StudentExistence(){}
    public StudentExistence(String studentName){
        username = studentName;
    }

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String username;
    @SerializedName("existence_count")
    @Expose
    private Boolean userExistence;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getUserExistence() {
        return userExistence;
    }

    public void setUserExistence(Boolean userExistence) {
        this.userExistence = userExistence;
    }
}

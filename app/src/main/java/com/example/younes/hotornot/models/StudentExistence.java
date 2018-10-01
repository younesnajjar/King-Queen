package com.example.younes.hotornot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 9/19/2018.
 */

public class StudentExistence {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String username;
    @SerializedName("existence_count")
    @Expose
    private Boolean existenceCount;

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

    public Boolean getExistenceCount() {
        return existenceCount;
    }

    public void setExistenceCount(Boolean existenceCount) {
        this.existenceCount = existenceCount;
    }
}

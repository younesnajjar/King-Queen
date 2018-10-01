package com.example.younes.hotornot.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by younes on 9/23/2018.
 */

public class NewRatingBody {
    @SerializedName("rated_id")
    @Expose
    private Integer ratedId;
    @SerializedName("rater_id")
    @Expose
    private Integer raterId;
    @SerializedName("rate")
    @Expose
    private Integer rate;

    public Integer getRatedId() {
        return ratedId;
    }

    public void setRatedId(Integer ratedId) {
        this.ratedId = ratedId;
    }

    public Integer getRaterId() {
        return raterId;
    }

    public void setRaterId(Integer raterId) {
        this.raterId = raterId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }
}

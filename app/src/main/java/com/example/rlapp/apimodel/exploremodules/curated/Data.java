package com.example.rlapp.apimodel.exploremodules.curated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("recommendedList")
    @Expose
    private List<Recommended> recommendedList;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public List<Recommended> getRecommendedList() {
        return recommendedList;
    }

    public void setRecommendedList(List<Recommended> recommendedList) {
        this.recommendedList = recommendedList;
    }
}

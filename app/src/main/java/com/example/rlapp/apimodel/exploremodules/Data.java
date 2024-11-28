package com.example.rlapp.apimodel.exploremodules;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("suggestedList")
    @Expose
    private List<Suggested> suggestedList;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Suggested> getSuggestedList() {
        return suggestedList;
    }

    public void setSuggestedList(List<Suggested> suggestedList) {
        this.suggestedList = suggestedList;
    }

}

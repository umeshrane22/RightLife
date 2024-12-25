package com.example.rlapp.ui.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchData {

    @SerializedName("result")
    @Expose
    private List<SearchResult> result;

    public List<SearchResult> getResult() {
        return result;
    }

    public void setResult(List<SearchResult> result) {
        this.result = result;
    }
}

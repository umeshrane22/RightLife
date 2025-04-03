package com.jetsynthesys.rightlife.ui.sleepsounds.models;

import com.google.gson.annotations.SerializedName;

public class SubCategory {
    @SerializedName("_id")
    private String id;
    @SerializedName("title")
    private String title;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
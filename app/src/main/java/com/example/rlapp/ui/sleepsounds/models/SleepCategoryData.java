package com.example.rlapp.ui.sleepsounds.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SleepCategoryData {
    @SerializedName("category")
    private List<Category> category; // Now referencing the top-level Category class

    public List<Category> getCategory() {
        return category;
    }
}

package com.jetsynthesys.rightlife.ui.sleepsounds.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Category {
    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("subCategory")
    private List<SubCategory> subCategory; // Now referencing the top-level SubCategory class

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SubCategory> getSubCategory() {
        return subCategory;
    }
}
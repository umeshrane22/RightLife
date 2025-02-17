package com.example.rlapp.ui.sleepsounds;

import com.example.rlapp.ui.sleepsounds.models.SleepCategoryData;
import com.google.gson.annotations.SerializedName;

public class SleepSoundCategoryResponse {
    @SerializedName("success")
    private boolean success;
    @SerializedName("statusCode")
    private int statusCode;
    @SerializedName("data")
    private SleepCategoryData data; // Now referencing the top-level SleepCategoryData class

    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public SleepCategoryData getData() {
        return data;
    }
}

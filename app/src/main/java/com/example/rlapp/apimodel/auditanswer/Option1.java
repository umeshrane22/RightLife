package com.example.rlapp.apimodel.auditanswer;

import com.google.gson.annotations.SerializedName;

public class Option1 {
    @SerializedName("option")
    private Object option; // Use Object to support both String and Integer types

    // Constructor, getters, and setters
    public Option1(Object option) {
        this.option = option;
    }

    public Object getOption() {
        return option;
    }

    public void setOption(Object option) {
        this.option = option;
    }
}
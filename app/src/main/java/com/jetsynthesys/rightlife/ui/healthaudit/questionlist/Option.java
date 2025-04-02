package com.jetsynthesys.rightlife.ui.healthaudit.questionlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Option implements Serializable {
    @SerializedName("option_txt")
    @Expose
    private String optionText;
    @SerializedName("option")
    @Expose
    private String optionPosition;

    private boolean isSelected;

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionPosition() {
        return optionPosition;
    }

    public void setOptionPosition(String optionPosition) {
        this.optionPosition = optionPosition;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
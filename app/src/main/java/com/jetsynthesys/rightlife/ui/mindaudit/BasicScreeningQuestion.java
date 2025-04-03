package com.jetsynthesys.rightlife.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BasicScreeningQuestion implements Serializable {

    @SerializedName("basicScreeningQuestions")
    @Expose
    private BasicScreeningQuestions basicScreeningQuestions;

    public BasicScreeningQuestions getBasicScreeningQuestions() {
        return basicScreeningQuestions;
    }

    public void setBasicScreeningQuestions(BasicScreeningQuestions basicScreeningQuestions) {
        this.basicScreeningQuestions = basicScreeningQuestions;
    }

}
package com.jetsynthesys.rightlife.ui.healthaudit.questionlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionData {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("questionList")
    @Expose
    private List<Question> questionList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

}
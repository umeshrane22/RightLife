package com.example.rlapp.ui.drawermenu;

import java.util.ArrayList;

public class FAQData {
    private String header;
    private ArrayList<QuestionAns> questionAns;
    private boolean isExpanded = false;

    public FAQData(String header, ArrayList<QuestionAns> questionAns) {
        this.header = header;
        this.questionAns = questionAns;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public ArrayList<QuestionAns> getQuestionAns() {
        return questionAns;
    }

    public void setQuestionAns(ArrayList<QuestionAns> questionAns) {
        this.questionAns = questionAns;
    }
}

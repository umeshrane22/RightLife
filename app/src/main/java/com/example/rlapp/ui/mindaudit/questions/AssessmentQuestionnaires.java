package com.example.rlapp.ui.mindaudit.questions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssessmentQuestionnaires {

    @SerializedName("groups")
    @Expose
    private List<Group> groups;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
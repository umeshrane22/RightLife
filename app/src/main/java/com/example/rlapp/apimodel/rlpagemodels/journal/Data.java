package com.example.rlapp.apimodel.rlpagemodels.journal;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("journalsList")
    @Expose
    private List<Journals> journalsList;

    public List<Journals> getJournalsList() {
        return journalsList;
    }

    public void setJournalsList(List<Journals> journalsList) {
        this.journalsList = journalsList;
    }

}
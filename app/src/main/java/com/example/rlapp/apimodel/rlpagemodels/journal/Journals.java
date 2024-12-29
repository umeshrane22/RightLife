package com.example.rlapp.apimodel.rlpagemodels.journal;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Journals {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("journal")
    @Expose
    private String journal;
    @SerializedName("particularSitutation")
    @Expose
    private String particularSitutation;
    @SerializedName("changedMood")
    @Expose
    private String changedMood;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("_id")
    @Expose
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getParticularSitutation() {
        return particularSitutation;
    }

    public void setParticularSitutation(String particularSitutation) {
        this.particularSitutation = particularSitutation;
    }

    public String getChangedMood() {
        return changedMood;
    }

    public void setChangedMood(String changedMood) {
        this.changedMood = changedMood;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
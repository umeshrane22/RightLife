package com.example.rlapp.apimodel.exploremodules.sleepsounds;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sleepAidConfig")
    @Expose
    private SleepAidConfig sleepAidConfig;
    @SerializedName("episodes")
    @Expose
    private List<Episode> episodes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SleepAidConfig getSleepAidConfig() {
        return sleepAidConfig;
    }

    public void setSleepAidConfig(SleepAidConfig sleepAidConfig) {
        this.sleepAidConfig = sleepAidConfig;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

}
package com.jetsynthesys.rightlife.ui.therledit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteRequest {

    @SerializedName("favourite")
    @Expose
    private Boolean favourite;
    @SerializedName("episodeId")
    @Expose
    private String episodeId;

    public Boolean getFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public String getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(String episodeId) {
        this.episodeId = episodeId;
    }

}
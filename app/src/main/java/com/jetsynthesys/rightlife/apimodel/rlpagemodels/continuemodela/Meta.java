package com.jetsynthesys.rightlife.apimodel.rlpagemodels.continuemodela;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Meta {

    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("sizeBytes")
    @Expose
    private Integer sizeBytes;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Integer sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

}
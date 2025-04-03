package com.jetsynthesys.rightlife.apimodel.rlpagemodels.continuemodela;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Data {

    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("contentDetails")
    @Expose
    private List<ContentDetail> contentDetails;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ContentDetail> getContentDetails() {
        return contentDetails;
    }

    public void setContentDetails(List<ContentDetail> contentDetails) {
        this.contentDetails = contentDetails;
    }

}
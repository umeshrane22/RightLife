package com.jetsynthesys.rightlife.apimodel.userdata;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("productId")
    @Expose
    private String productId;

    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}


package com.jetsynthesys.rightlife.apimodel.newreportfacescan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HealthCamReportByCategory implements Serializable {
    @SerializedName("HEALTH_CAM_GOOD")
    public List<HealthCamItem> healthCamGood;
    @SerializedName("HEALTH_CAM_PAY_ATTENTION")
    public List<HealthCamItem> healthCamPayAttention;
}
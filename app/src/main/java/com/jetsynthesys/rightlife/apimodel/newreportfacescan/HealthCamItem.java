package com.jetsynthesys.rightlife.apimodel.newreportfacescan;

import java.io.Serializable;

public class HealthCamItem implements Serializable {
    public String fieldName;
    public Double value;
    public String parameter;
    public String deffination; // Corrected spelling
    public double lowerRange;
    public double upperRange;
    public String unit;
    public String indicator;
    public String colour;
    public String implication;
    public String homeScreen;
}
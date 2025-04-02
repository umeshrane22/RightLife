package com.jetsynthesys.rightlife.apimodel.newreportfacescan;

import com.google.gson.annotations.SerializedName;

public class Artist {
    public String firstName;
    public String lastName;
    public String profilePicture;
    @SerializedName("_id")
    public String id;
}
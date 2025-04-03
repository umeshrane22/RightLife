package com.jetsynthesys.rightlife.ui.mindaudit.curated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CuratedUserData {

    @SerializedName("context")
    @Expose
    private Context context;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
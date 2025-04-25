package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TaxRate {
    @SerializedName("taxableAmount")
    @Expose
    var taxableAmount: Int? = null

    @SerializedName("cGst")
    @Expose
    private var cGst: Int? = null

    @SerializedName("sGst")
    @Expose
    private var sGst: Int? = null

    @SerializedName("iGst")
    @Expose
    private var iGst: Int? = null

    @SerializedName("gst")
    @Expose
    var gst: Int? = null

    fun getcGst(): Int? {
        return cGst
    }

    fun setcGst(cGst: Int?) {
        this.cGst = cGst
    }

    fun getsGst(): Int? {
        return sGst
    }

    fun setsGst(sGst: Int?) {
        this.sGst = sGst
    }

    fun getiGst(): Int? {
        return iGst
    }

    fun setiGst(iGst: Int?) {
        this.iGst = iGst
    }
}
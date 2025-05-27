package com.jetsynthesys.rightlife.ui.settings.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TaxRate {
    @SerializedName("taxableAmount")
    @Expose
    var taxableAmount: Double? = null

    @SerializedName("cGst")
    @Expose
    private var cGst: Double? = null

    @SerializedName("sGst")
    @Expose
    private var sGst: Double? = null

    @SerializedName("iGst")
    @Expose
    private var iGst: Double? = null

    @SerializedName("gst")
    @Expose
    var gst: Double? = null

    fun getcGst(): Double? {
        return cGst
    }

    fun setcGst(cGst: Double?) {
        this.cGst = cGst
    }

    fun getsGst(): Double? {
        return sGst
    }

    fun setsGst(sGst: Double?) {
        this.sGst = sGst
    }

    fun getiGst(): Double? {
        return iGst
    }

    fun setiGst(iGst: Double?) {
        this.iGst = iGst
    }
}
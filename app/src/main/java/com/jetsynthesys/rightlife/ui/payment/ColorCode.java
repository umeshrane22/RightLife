package com.jetsynthesys.rightlife.ui.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ColorCode {

    @SerializedName("card")
    @Expose
    private String card;
    @SerializedName("promo")
    @Expose
    private String promo;
    @SerializedName("item")
    @Expose
    private String item;

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
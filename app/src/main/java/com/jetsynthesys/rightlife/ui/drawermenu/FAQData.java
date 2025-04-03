package com.jetsynthesys.rightlife.ui.drawermenu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FAQData {

    @SerializedName("faqs")
    @Expose
    private List<Faq> faqs;

    public List<Faq> getFaqs() {
        return faqs;
    }

    public void setFaqs(List<Faq> faqs) {
        this.faqs = faqs;
    }
}

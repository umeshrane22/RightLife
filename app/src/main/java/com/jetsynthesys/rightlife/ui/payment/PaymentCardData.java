package com.jetsynthesys.rightlife.ui.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentCardData {

@SerializedName("result")
@Expose
private PaymentCardResult result;

public PaymentCardResult getResult() {
return result;
}

public void setResult(PaymentCardResult result) {
this.result = result;
}

}
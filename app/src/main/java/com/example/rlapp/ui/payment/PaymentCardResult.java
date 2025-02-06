package com.example.rlapp.ui.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class PaymentCardResult {
   @SerializedName("LIST")
   @Expose
   private List<PaymentCardList> list;
   @SerializedName("SERVICE")
   @Expose
   private Service service;

   public List<PaymentCardList> getList() {
      return list;
   }

   public void setList(List<PaymentCardList> list) {
      this.list = list;
   }

   public Service getService() {
      return service;
   }

   public void setService(Service service) {
      this.service = service;
   }

}


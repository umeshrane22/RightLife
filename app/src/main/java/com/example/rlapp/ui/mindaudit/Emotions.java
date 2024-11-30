package com.example.rlapp.ui.mindaudit;

   public class Emotions   {
      private String emotion;
      private boolean isSelected;

      public Emotions(String emotion, boolean isSelected) {
         this.emotion = emotion;
         this.isSelected = isSelected;
      }

      public String getEmotion() {
         return emotion;
      }

      public void setEmotion(String emotion) {
         this.emotion = emotion;
      }

      public boolean isSelected() {
         return isSelected;
      }

      public void setSelected(boolean selected) {
         isSelected = selected;
      }
   }

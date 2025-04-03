package com.jetsynthesys.rightlife.ui.mindaudit;

import java.util.List;

public class UserEmotions {
   private List<String> emotions;

   public List<String> getEmotions() {
      return emotions;
   }

   public void setEmotions(List<String> emotions) {
      this.emotions = emotions;
   }

   public UserEmotions(List<String> emotions) {
      this.emotions = emotions;
   }
}

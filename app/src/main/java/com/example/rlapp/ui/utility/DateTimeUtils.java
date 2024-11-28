package com.example.rlapp.ui.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
   public static String getDateTime(){
      Date c = Calendar.getInstance().getTime();

      SimpleDateFormat df = new SimpleDateFormat("EEE dd MMMM. hh:mm a", Locale.getDefault());

      return df.format(c);
   }
}

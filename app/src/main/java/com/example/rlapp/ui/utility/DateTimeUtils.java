package com.example.rlapp.ui.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    public static String getDateTime() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("EEE dd MMMM. hh:mm a", Locale.getDefault());

        return df.format(c);
    }

    public static String convertAPIDate(String date) {
        if (date == null)
            return date;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd/MM/yyyy");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convert_ddMMyyyy_toAPIDate(String date) {
        if (date == null)
            return date;
        SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

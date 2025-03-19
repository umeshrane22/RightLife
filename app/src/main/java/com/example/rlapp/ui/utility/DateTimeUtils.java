package com.example.rlapp.ui.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static String getWishingMessage() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        String message = "";

        if (timeOfDay >= 0 && timeOfDay < 12) {
            message = "Morning";
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            message = "Afternoon";
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            message = "Evening";
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            message = "Night";
        }
        return message;
    }


    public static String convertAPIDateMonthFormat(String date) {
        if (date == null)
            return date;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date newDate = spf.parse(date);
            spf = new SimpleDateFormat("dd MMM yyyy");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String convertAPIDateMonthFormatWithTime(String date) {
        if (date == null)
            return date;
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        spf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date newDate = spf.parse(date);
            // Define the desired output format
            spf = new SimpleDateFormat("dd MMMM yyyy | h.mm a", Locale.getDefault());
            spf.setTimeZone(TimeZone.getDefault());
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String formatCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd | hh:mm a", Locale.ENGLISH);
        return dateFormat.format(new Date());
    }
}

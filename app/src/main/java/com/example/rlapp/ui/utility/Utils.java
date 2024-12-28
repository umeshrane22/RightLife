package com.example.rlapp.ui.utility;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.rlapp.R;
import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static final String LOGIN_TYPE_PHONE_NUMBER = "PHONE_NUMBER";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    public static final String BETTER_RIGHT_LIFE_KEY = "ukd5jxlefzxyvgxlq9mbvzre7oxewo0m";
    public static final String BETTER_RIGHT_LIFE_IV = "8PzGKSMLuqSm0MVbviaWHA==";


    public static String getModuleText(String module) {
        String type = "";

        if (module == null) {
            return "";
        }

        switch (module) {
            case AppConstants.EAT_RIGHT:
            case "EAT_RIGHT":
                type = "Eat Right";
                break;

            case AppConstants.THINK_RIGHT:
            case "THINK_RIGHT":
                type = "Think Right";
                break;

            case AppConstants.SLEEP_RIGHT:
            case "SLEEP_RIGHT":
                type = "Sleep Right";
                break;

            case AppConstants.MOVE_RIGHT:
            case "MOVE_RIGHT":
                type = "Move Right";
                break;

            default:
                type = module; // If no match, return the module itself
                break;
        }

        return type;
    }
    public static int getModuleColor(Context context, String module) {
        if (module == null) {
            return ContextCompat.getColor(context, android.R.color.transparent); // Default to transparent if null
        }

        switch (module) {
            case AppConstants.EAT_RIGHT:
            case "EAT_RIGHT":
                return ContextCompat.getColor(context, R.color.eatright);

            case AppConstants.THINK_RIGHT:
            case "THINK_RIGHT":
                return ContextCompat.getColor(context, R.color.thinkright);

            case AppConstants.SLEEP_RIGHT:
            case "SLEEP_RIGHT":
                return ContextCompat.getColor(context, R.color.sleepright);

            case AppConstants.MOVE_RIGHT:
            case "MOVE_RIGHT":
                return ContextCompat.getColor(context, R.color.moveright);

            default:
                // If no match, return a default color (e.g., transparent or a predefined color)
                return ContextCompat.getColor(context, android.R.color.darker_gray);
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}

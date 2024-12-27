package com.example.rlapp.ui.utility;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

public class Utils {
    public static final String LOGIN_TYPE_PHONE_NUMBER = "PHONE_NUMBER";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    public static final String BETTER_RIGHT_LIFE_KEY = "ukd5jxlefzxyvgxlq9mbvzre7oxewo0m";
    public static final String BETTER_RIGHT_LIFE_IV = "8PzGKSMLuqSm0MVbviaWHA==";

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

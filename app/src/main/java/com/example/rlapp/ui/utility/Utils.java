package com.example.rlapp.ui.utility;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import androidx.core.content.ContextCompat;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.therledit.FavouriteErrorResponse;
import com.example.rlapp.ui.therledit.FavouriteRequest;
import com.example.rlapp.ui.therledit.FavouriteResponse;
import com.example.rlapp.ui.therledit.OnFavouriteClickListener;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utils {
    public static final String LOGIN_TYPE_PHONE_NUMBER = "PHONE_NUMBER";
    public static final String BETTER_RIGHT_LIFE_KEY = "ukd5jxlefzxyvgxlq9mbvzre7oxewo0m";
    public static final String BETTER_RIGHT_LIFE_IV = "8PzGKSMLuqSm0MVbviaWHA==";
    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

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
        }
    }

    public static int getModuleDarkColor(Context context, String module) {
        if (module == null) {
            return ContextCompat.getColor(context, android.R.color.transparent); // Default to transparent if null
        }

        switch (module) {
            case AppConstants.EAT_RIGHT:
            case "EAT_RIGHT":
                return ContextCompat.getColor(context, R.color.color_eat_right);

            case AppConstants.THINK_RIGHT:
            case "THINK_RIGHT":
                return ContextCompat.getColor(context, R.color.color_think_right);

            case AppConstants.SLEEP_RIGHT:
            case "SLEEP_RIGHT":
                return ContextCompat.getColor(context, R.color.color_sleep_right);

            case AppConstants.MOVE_RIGHT:
            case "MOVE_RIGHT":
                return ContextCompat.getColor(context, R.color.color_move_right);

            default:
                // If no match, return a default color (e.g., transparent or a predefined color)
                return ContextCompat.getColor(context, android.R.color.darker_gray);
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public static void addToFavourite(Context context, String contentId, FavouriteRequest favouriteRequest, OnFavouriteClickListener onFavouriteClickListener) {
        String authToken = SharedPreferenceManager.getInstance(context).getAccessToken();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseBody> call = apiService.updateFavourite(authToken, contentId, favouriteRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                String jsonString = null;
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        jsonString = response.body().string();
                        Log.d("API_RESPONSE", "Favourite response: " + jsonString);
                        FavouriteResponse favouriteResponse = gson.fromJson(jsonString, FavouriteResponse.class);
                        onFavouriteClickListener.onSuccess(favouriteResponse.getSuccess(), favouriteResponse.getSuccessMessage());

                    } catch (IOException e) {
                        Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.getMessage());
                        onFavouriteClickListener.onError(e.getMessage());
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        jsonString = response.errorBody().string();
                        FavouriteErrorResponse favouriteErrorResponse = gson.fromJson(jsonString, FavouriteErrorResponse.class);
                        onFavouriteClickListener.onError(favouriteErrorResponse.getDisplayMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onFavouriteClickListener.onError(t.getMessage());
            }
        });
    }

    public static String toTitleCase(String string) {

        // Check if String is null
        if (string == null) {

            return null;
        }

        boolean whiteSpace = true;

        StringBuilder builder = new StringBuilder(string); // String builder to store string
        final int builderLength = builder.length();

        // Loop through builder
        for (int i = 0; i < builderLength; ++i) {

            char c = builder.charAt(i); // Get character at builders position

            if (whiteSpace) {

                // Check if character is not white space
                if (!Character.isWhitespace(c)) {

                    // Convert to title case and leave whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c));
                    whiteSpace = false;
                }
            } else if (Character.isWhitespace(c)) {

                whiteSpace = true; // Set character is white space

            } else {

                builder.setCharAt(i, Character.toLowerCase(c)); // Set character to lowercase
            }
        }

        return builder.toString(); // Return builders text
    }
}

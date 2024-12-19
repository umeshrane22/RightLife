package com.example.rlapp.ui.utility;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        okhttp3.Response response = chain.proceed(request);

        // Log request
        Log.d("Retrofit ", "Request: " + request.url());
        Log.d("Retrofit", "Headers: " + request.headers());

        // Log response
    //   Log.d("Retrofit", "Response--: " + response.body().string());
     //   Log.d("Retrofit", "Headers: " + response.headers());

        return response;
    }
}
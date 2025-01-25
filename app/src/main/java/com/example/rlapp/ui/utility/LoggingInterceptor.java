package com.example.rlapp.ui.utility;

import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/*
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
}*/

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Log the request details
        System.out.println("API Request: " + request.method() + " " + request.url());

        // Log headers
        Headers headers = request.headers();
        for (String name : headers.names()) {
            System.out.println("Header: " + name + " = " + headers.get(name));
        }

        // Log body (if applicable)
        if (request.body() != null) {
            System.out.println("Body: " + request.body().toString());
        }

        // Proceed with the request
        Response response = chain.proceed(request);

        // Log the response details
        System.out.println("API Response Code: " + response.code());

        // Peek into the response body
        ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
        System.out.println("API Request : "+ request.url() +"\nAPI Request Response Body: " + responseBody.string());

        return response; // Allow the response to continue
    }
}
package com.jetsynthesys.rightlife.ui.utility;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

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

        if (request.body() != null) {
            RequestBody requestBody = request.body();
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = StandardCharsets.UTF_8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
            }

            Log.d("Retrofit", "API Request Body: " + buffer.readString(charset));
        }
        // Log body (if applicable)
        if (request.body() != null) {
            System.out.println("API Request Body: " + request.body());
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
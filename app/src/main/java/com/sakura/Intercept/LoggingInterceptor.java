package com.sakura.Intercept;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class LoggingInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        long startTime = System.currentTimeMillis();

        Response response = chain.proceed(request);

        Log.d("network", "request took " + (System.currentTimeMillis() - startTime)+ "ms");

        return response;
    }
}

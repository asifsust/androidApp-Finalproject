package com.example.employees.network;

import androidx.annotation.NonNull;

import com.example.employees.others.GlobalValues;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .header("Authorization","Bearer "+ GlobalValues.token)
                .build();

        return chain.proceed(request);

    }
}

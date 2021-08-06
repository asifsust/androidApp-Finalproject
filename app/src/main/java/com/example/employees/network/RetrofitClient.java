package com.example.employees.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static String baseUrl = "http://ec2-18-118-51-167.us-east-2.compute.amazonaws.com";
    public static RetrofitClient retrofitClient;
    public static Retrofit retrofit;

    //for debugging purpose
    public OkHttpClient.Builder builder = new OkHttpClient.Builder();
    public HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    public RetrofitClient(){
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if (retrofitClient == null)
            retrofitClient = new RetrofitClient();
        return retrofitClient;
    }

    public ApiResponse getApi(){
        return  retrofit.create(ApiResponse.class);
    }

}

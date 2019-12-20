package com.vv0z.currencywatcher.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.vv0z.currencywatcher.app.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().setLenient().create();


    private static Retrofit getClient() {

        if (retrofit == null) {

            // Enable requests logging
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // Initialize request okhttp client
            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .addNetworkInterceptor(interceptor)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS);

            // Create instance of retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient.build())
                    .build();
        }

        return retrofit;
    }

    /**
     * Return instance of retrofit
     */
    public static ApiService getApiInterface() {
        return ApiClient.getClient().create(ApiService.class);
    }
}

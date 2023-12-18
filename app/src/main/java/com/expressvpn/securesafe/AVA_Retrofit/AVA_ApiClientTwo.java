package com.expressvpn.securesafe.AVA_Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AVA_ApiClientTwo {

    static String BASE_URLTwo = "https://ipapi.co/";
    private static Retrofit retrofit = null;
    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static Retrofit getClientIp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URLTwo)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES).build())
                    .build();
        }
        return retrofit;
    }

}

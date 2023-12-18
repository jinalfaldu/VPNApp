package com.expressvpn.securesafe.AVA_Api_Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AVA_AdsApiClient {
    static String BASE_URL = "https://www.adcobber.com/DataApi/";
    private static Retrofit retrofit = null;

    static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(httpClient.readTimeout(5, TimeUnit.MINUTES).connectTimeout(5, TimeUnit.MINUTES).build())
                    .build();
        }
        return retrofit;
    }


}

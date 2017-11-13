package com.hosiluan.googlemapdemo.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by User on 11/13/2017.
 */

public class RetrofitClient {
    private static Retrofit sRetrofit;

    public static Retrofit getClient(String baseUrl){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (sRetrofit == null){
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return sRetrofit;
    }
}
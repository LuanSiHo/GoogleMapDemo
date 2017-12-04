package com.hosiluan.googlemapdemo.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hosiluan.googlemapdemo.CoreApplication;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.readystatesoftware.chuck.ChuckInterceptor;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.OkHttpClient;
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
        ChuckInterceptor chuckInterceptor = new ChuckInterceptor(CoreApplication.getInstance().getApplicationContext());
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chuckInterceptor).build();

        if (sRetrofit == null){
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
        return sRetrofit;
    }
}

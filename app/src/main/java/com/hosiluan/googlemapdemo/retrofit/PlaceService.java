package com.hosiluan.googlemapdemo.retrofit;

import com.hosiluan.googlemapdemo.model.MyPojo;
import com.hosiluan.googlemapdemo.model.PlaceModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 11/13/2017.
 */

public interface PlaceService {

    @GET("json")
    Call<MyPojo> getPlaceList(@Query("address") String address);

}

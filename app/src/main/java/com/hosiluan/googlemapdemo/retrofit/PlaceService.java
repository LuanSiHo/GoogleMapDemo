package com.hosiluan.googlemapdemo.retrofit;

import com.hosiluan.googlemapdemo.model.MyPojo;
import com.hosiluan.googlemapdemo.model.PlaceModel;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 11/13/2017.
 */

public interface PlaceService {

//    @GET("json")
//    io.reactivex.Observable<MyPojo> getPlaceList(@Query("query") String address,
//                                                 @Query("key") String key);

    @GET("json")

    Call<MyPojo> getPlaceList(@Query("query") String address,
                              @Query("key") String key);

}

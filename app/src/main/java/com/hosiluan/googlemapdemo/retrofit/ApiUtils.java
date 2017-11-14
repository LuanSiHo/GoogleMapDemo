package com.hosiluan.googlemapdemo.retrofit;

/**
 * Created by User on 11/13/2017.
 */

public class ApiUtils {
    public static final String baseUrl = "https://maps.googleapis.com/maps/api/place/textsearch/";

    public static PlaceService getPlaceService(){
        return RetrofitClient.getClient(baseUrl).create(PlaceService.class);
    }

}

package com.hosiluan.googlemapdemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/13/2017.
 */

public class LocationModel {

    @SerializedName("lat")
    @Expose
    private double mLat;

    @SerializedName("lng")
    @Expose
    private double mLon;


    public LocationModel() {
    }

    public LocationModel(double mLat, double mLon) {
        this.mLat = mLat;
        this.mLon = mLon;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    public double getmLon() {
        return mLon;
    }

    public void setmLon(double mLon) {
        this.mLon = mLon;
    }
}

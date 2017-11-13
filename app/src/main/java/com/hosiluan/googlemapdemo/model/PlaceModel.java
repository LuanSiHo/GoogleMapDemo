package com.hosiluan.googlemapdemo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/13/2017.
 */

public class PlaceModel {

    @SerializedName("formatted_address")
    @Expose
    private String mAddress;

    @SerializedName("location")
    @Expose
    private LocationModel mLocation;


    public PlaceModel() {
    }

    public PlaceModel(String mAddress, LocationModel mLocation) {
        this.mAddress = mAddress;
        this.mLocation = mLocation;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public LocationModel getmLocation() {
        return mLocation;
    }

    public void setmLocation(LocationModel mLocation) {
        this.mLocation = mLocation;
    }
}

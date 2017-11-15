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

    @SerializedName("name")
    @Expose
    private String mName;

    @SerializedName("location")
    @Expose
    private LocationModel mLocation;


    public PlaceModel() {
    }

    public PlaceModel(String mAddress, LocationModel mLocation) {
        this.mAddress = mAddress;
        this.mLocation = mLocation;
    }

    public PlaceModel(String mAddress, String mName, LocationModel mLocation) {
        this.mAddress = mAddress;
        this.mName = mName;
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

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}

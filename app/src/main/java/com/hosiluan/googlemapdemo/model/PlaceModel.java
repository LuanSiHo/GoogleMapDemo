package com.hosiluan.googlemapdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 11/13/2017.
 */

public class PlaceModel implements Parcelable{

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

    protected PlaceModel(Parcel in) {
        mAddress = in.readString();
        mName = in.readString();
    }

    public static final Creator<PlaceModel> CREATOR = new Creator<PlaceModel>() {
        @Override
        public PlaceModel createFromParcel(Parcel in) {
            return new PlaceModel(in);
        }

        @Override
        public PlaceModel[] newArray(int size) {
            return new PlaceModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAddress);
        dest.writeString(mName);
    }
}

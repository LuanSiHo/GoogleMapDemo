package com.hosiluan.googlemapdemo;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

/**
 * Created by User on 11/10/2017.
 */

public class BaseActivity extends AppCompatActivity {


    private final int PERMISSION_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    protected Handler mHandler;

    private double mLon, mLat;

    public double getmLon() {
        return mLon;
    }

    public void setmLon(double mLon) {
        this.mLon = mLon;
    }

    public double getmLat() {
        return mLat;
    }

    public void setmLat(double mLat) {
        this.mLat = mLat;
    }

    /**
     * check permission to get current lon lat
     *
     * @return
     */
    protected boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    /**
     * request permission to get current lon lat
     */
    protected void requestLocationPermission() {
        ActivityCompat.requestPermissions(BaseActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();

                } else {
                    if (!checkLocationPermission()) {
                        AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
                        alertDialog.setTitle("Alert");
                        alertDialog.setMessage("You do not enable location service, some feature won't work");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got it", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                    Toast.makeText(this, "need your location", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void getLocation() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {

                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED) {

                            Location location = LocationServices.FusedLocationApi
                                    .getLastLocation(mGoogleApiClient);

                            if (location != null) {

                                mLat = location.getLatitude();
                                mLon = location.getLongitude();
                            } else {
                                CoreApplication.getInstance().showToast("== null");
                            }

                            Message message = new Message();
                            message.what = 1;
                            mHandler.sendMessage(message);


//                            Thread thread = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Message message = new Message();
//                                    message.what = 1;
//                                    mHandler.sendMessage(message);
//                                }
//                            });
//                            thread.start();

//                            Log.d("Luan", mLat + " / " + mLon);
                        }
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d("Luan", "connection suspended");
                    }
                })
                .addApi(LocationServices.API)
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        CoreApplication.getInstance().showToast("connection fail");
                    }
                })
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


}




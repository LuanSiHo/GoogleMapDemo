package com.hosiluan.googlemapdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends BaseActivity implements OnMapReadyCallback {


    private GoogleMap mGoogleMap;
    View mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCurrentLocation();
        handleMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void createMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);
        mMapView = supportMapFragment.getView();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        Log.d("Luan", getmLat() + " / " + getmLon());
        LatLng latLng = new LatLng(getmLat(), getmLon());

        moveToCurrentLocation(googleMap,latLng);

        if (mMapView != null
                && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentLocation();
                    moveToCurrentLocation(googleMap,new LatLng(getmLat(),getmLon()));
                }
            });


        }
    }


    private void moveToCurrentLocation(GoogleMap googleMap, LatLng latLng) {

        this.mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        mGoogleMap.addMarker(new MarkerOptions()
                .title("Your Location")
                .snippet("JV-IT company")
                .position(latLng)).showInfoWindow();
        CameraUpdate cameraUpdate  = CameraUpdateFactory.newLatLng(latLng);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void handleMessage() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        createMap();
                        break;
                }
                return true;
            }
        });
    }


    public void getCurrentLocation() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkLocationPermission()) {
                    getLocation();
                } else {


                    requestLocationPermission();
                }
            }
        });
        thread.start();
    }

}

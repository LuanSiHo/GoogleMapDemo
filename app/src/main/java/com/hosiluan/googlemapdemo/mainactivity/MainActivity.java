package com.hosiluan.googlemapdemo.mainactivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hosiluan.googlemapdemo.BaseActivity;
import com.hosiluan.googlemapdemo.CoreApplication;
import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.model.PlaceModel;
import com.hosiluan.googlemapdemo.model.Results;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnMapReadyCallback ,
        MainActivityPresenter.MainActivitPresenterListener{


    private GoogleMap mGoogleMap;
    private EditText mSearchPlaceEditText;

    private MainActivityPresenter mMainActivityPresenter;

    private RecyclerView mRecyclerView;
    private ArrayList<Results> mResultsArrayList;
    private RecyclerViewAdapter mRecyclerViewAdapter;

    View mMapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setView();
        setEvent();

        getCurrentLocation();
        handleMessage();

//        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                Log.d("Luan", "Place: " + place.getName());
//
//            }
//
//            @Override
//            public void onError(Status status) {
//
//            }
//        });
    }

    private void setView() {
        mSearchPlaceEditText = (EditText) findViewById(R.id.edt_search_place);
        mMainActivityPresenter = new MainActivityPresenter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_place);

        mResultsArrayList = new ArrayList<>();
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(),mResultsArrayList);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));

    }

    private void setEvent() {

        mSearchPlaceEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mMainActivityPresenter.searchPlace(mSearchPlaceEditText.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
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

        moveToCurrentLocation(googleMap, latLng);

        if (mMapView != null
                && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentLocation();
                    moveToCurrentLocation(googleMap, new LatLng(getmLat(), getmLon()));
                }
            });

        }
    }


    private void moveToCurrentLocation(GoogleMap googleMap, LatLng latLng) {

        this.mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
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


    @Override
    public void onSearchResult(Results[] results) {
        if (results.length > 0){
            CoreApplication.getInstance().logDebug(results[0].getFormatted_address());
            CoreApplication.getInstance().logDebug(results.length + " length");

            for (int i = 0; i < results.length; i ++){
                mResultsArrayList.add(results[i]);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}

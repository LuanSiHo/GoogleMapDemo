package com.hosiluan.googlemapdemo.mainactivity;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class MainActivity extends BaseActivity implements OnMapReadyCallback,
        MainActivityPresenter.MainActivitPresenterListener,RecyclerViewAdapter.RecyclerViewAdapterListener {


    private GoogleMap mGoogleMap;
    private EditText mSearchPlaceEditText;
    private ImageView mSearImageButton;
    private TextView mNumberOfPlaceTextView;

    private MainActivityPresenter mMainActivityPresenter;

    private RecyclerView mRecyclerView;
    private ArrayList<Results> mResultsArrayList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private LinearLayout mLinearLayoutSearch;

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
        mSearImageButton = (ImageView) findViewById(R.id.img_btn_search);
        mNumberOfPlaceTextView = (TextView) findViewById(R.id.tv_number_of_place);

        mMainActivityPresenter = new MainActivityPresenter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_place);

        mResultsArrayList = new ArrayList<>();
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mResultsArrayList,
                this);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));

        mLinearLayoutSearch = (LinearLayout) findViewById(R.id.linearlayout_search);

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


        mSearchPlaceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                int temp = s.length() > 2 ? s.length() : 0;

                if (s.length() > 2 && temp > 0 && (s.length() % temp == 0) ){
                    mLinearLayoutSearch.setVisibility(View.VISIBLE);
                    String text = s.toString().replaceAll("\\s+", " ");
                    if (text.length() > 0 && !text.equals(" ")){
                        mMainActivityPresenter.searchPlace(text);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() <= 0 || s.equals(" ")){
                    mResultsArrayList.clear();
                    mRecyclerViewAdapter.notifyDataSetChanged();
                    mLinearLayoutSearch.setVisibility(View.GONE);
                    mSearchPlaceEditText.setText("");
                }
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
        addMarker(googleMap,"Your location","JV-IT",latLng);

        if (mMapView != null
                && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentLocation();
                    moveToCurrentLocation(googleMap, new LatLng(getmLat(), getmLon()));
                    addMarker(googleMap,"Your location","JV-IT",new LatLng(getmLat(), getmLon()));
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

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(latLng);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.animateCamera(cameraUpdate);
    }

    private void addMarker(GoogleMap  mGoogleMap, String title,String snippet, LatLng latLng){
        mGoogleMap.addMarker(new MarkerOptions()
        .title(title)
        .snippet(snippet)
        .position(latLng)).showInfoWindow();
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
        if (results.length > 0) {
            CoreApplication.getInstance().logDebug(results[0].getFormatted_address());
            CoreApplication.getInstance().logDebug(results.length + " length");

            mResultsArrayList.clear();
            mLinearLayoutSearch.setVisibility(View.VISIBLE);
            mNumberOfPlaceTextView.setText(results.length + " places");

            for (int i = 0; i < results.length; i++) {
                mResultsArrayList.add(results[i]);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(int position) {
        CoreApplication.getInstance().logDebug("item click");

        double lat = Double.parseDouble(mResultsArrayList.get(position).getGeometry().getLocation().getLat());
        double lon = Double.parseDouble(mResultsArrayList.get(position).getGeometry().getLocation().getLng());
        moveToCurrentLocation(mGoogleMap,new LatLng(lat,lon));
        addMarker(mGoogleMap,mResultsArrayList.get(position).getName(),
                mResultsArrayList.get(position).getFormatted_address(),new LatLng(lat,lon));
        mLinearLayoutSearch.setVisibility(View.GONE);
        mSearchPlaceEditText.setText("");
    }
}

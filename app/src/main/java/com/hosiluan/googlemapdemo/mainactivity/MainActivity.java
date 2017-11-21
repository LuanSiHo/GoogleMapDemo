package com.hosiluan.googlemapdemo.mainactivity;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hosiluan.googlemapdemo.BaseActivity;
import com.hosiluan.googlemapdemo.CoreApplication;
import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.common.Common;
import com.hosiluan.googlemapdemo.model.LocationModel;
import com.hosiluan.googlemapdemo.model.PlaceModel;
import com.hosiluan.googlemapdemo.model.Results;
import com.hosiluan.googlemapdemo.retrofit.ApiUtils;
import com.hosiluan.googlemapdemo.retrofit.PlaceService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends BaseActivity implements OnMapReadyCallback, RecyclerViewAdapter.RecyclerViewAdapterListener {

    ArrayList<PlaceModel> mPlaceModel = new ArrayList<>();

    private GoogleMap mGoogleMap;
    private EditText mSearchPlaceEditText;
    private ImageView mSearImageButton;
    private TextView mNumberOfPlaceTextView;


    private RecyclerView mRecyclerView;
    private ArrayList<Results> mResultsArrayList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private LinearLayout mLinearLayoutSearch;
    private ArrayList<PlaceModel> mPlaceArrayList, mSavedPlaceList;
    private PlaceService mPlaceService;


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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_place);

        mResultsArrayList = new ArrayList<>();
        mSavedPlaceList = new ArrayList<>();

//        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mResultsArrayList,
//                this);
        mPlaceArrayList = new ArrayList<>();
        mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mPlaceArrayList,
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
//                    mMainActivityPresenter.searchPlace(mSearchPlaceEditText.getText().toString().trim());

                    mPlaceArrayList.clear();
                    mRecyclerViewAdapter.notifyDataSetChanged();

                    String place = mSearchPlaceEditText.getText().toString().trim();
                    String text = place.replaceAll("\\s+", "%20");

                    new async().execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                            + text + "&key=AIzaSyDtOND-lFNNOYQrsDS-iqVJtW-aFDFCZj4");

                    CoreApplication.getInstance().logDebug("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                            + text + "&key=AIzaSyAmVAh9yhy1g6M0wsiAjwzizJyQOyOa1vk");
                    return true;
                }
                return false;
            }
        });

//
//        mSearchPlaceEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                if (s.length() > 0) {
//
//                    String place = mSearchPlaceEditText.getText().toString().trim();
//                    new async().execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
//                            +place+"&key=AIzaSyB95jx_yE1DGcuTumosZ6XiOJ3Dv1IhCEc");
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (s.length() <= 0 || s.equals(" ")) {
//                    CoreApplication.getInstance().logDebug("clear text");
//                    mResultsArrayList.clear();
//                    mRecyclerViewAdapter.notifyDataSetChanged();
//                    mLinearLayoutSearch.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    public void createMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);
        mMapView = supportMapFragment.getView();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        this.mGoogleMap = googleMap;

        LatLng latLng = new LatLng(getmLat(), getmLon());

        moveToCurrentLocation(googleMap, latLng);
        addMarker(googleMap, "Your location", "JV-IT", latLng);

        // restore all markers after rotate screen
        if (mSavedPlaceList != null) {
            for (int i = 0; i < mSavedPlaceList.size(); i++) {
                double lat = mSavedPlaceList.get(i).getmLocation().getmLat();
                double lon = mSavedPlaceList.get(i).getmLocation().getmLon();
                addMarker(mGoogleMap, mSavedPlaceList.get(i).getmName(), mSavedPlaceList.get(i).getmAddress(),
                        new LatLng(lat, lon));
            }
        }

        if (mMapView != null
                && mMapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1"))
                    .getParent()).findViewById(Integer.parseInt("2"));

            locationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCurrentLocation();
                    moveToCurrentLocation(googleMap, new LatLng(getmLat(), getmLon()));
                    addMarker(googleMap, "Your location", "JV-IT", new LatLng(getmLat(), getmLon()));
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

    private void addMarker(GoogleMap mGoogleMap, String title, String snippet, LatLng latLng) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(title);
        markerOptions.snippet(snippet);
        markerOptions.position(latLng);

        Marker marker = mGoogleMap.addMarker(markerOptions);
        marker.showInfoWindow();

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
    public void onItemClick(int position) {

//        double lat = Double.parseDouble(mResultsArrayList.get(position).getGeometry().getLocation().getLat());
//        double lon = Double.parseDouble(mResultsArrayList.get(position).getGeometry().getLocation().getLng());
//        moveToCurrentLocation(mGoogleMap, new LatLng(lat, lon));
//        addMarker(mGoogleMap, mResultsArrayList.get(position).getName(),
//                mResultsArrayList.get(position).getFormatted_address(), new LatLng(lat, lon));
//        mLinearLayoutSearch.setVisibility(View.GONE);
//        mSearchPlaceEditText.setText("");

        mSavedPlaceList.add(mPlaceArrayList.get(position));

        double lat = mPlaceArrayList.get(position).getmLocation().getmLat();
        double lon = mPlaceArrayList.get(position).getmLocation().getmLon();
        moveToCurrentLocation(mGoogleMap, new LatLng(lat, lon));


        addMarker(mGoogleMap, mPlaceArrayList.get(position).getmName(), mPlaceArrayList.get(position).getmAddress(),
                new LatLng(lat, lon));

//        addMarker(mGoogleMap, mResultsArrayList.get(position).getName(),
//                mResultsArrayList.get(position).getFormatted_address(), new LatLng(lat, lon));

        mPlaceArrayList.clear();
        mRecyclerViewAdapter.notifyDataSetChanged();
        mLinearLayoutSearch.setVisibility(View.GONE);
        mSearchPlaceEditText.setText("");
    }


    class async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            CoreApplication.getInstance().logDebug("on do in background");
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                JSONObject root = new JSONObject(sb.toString());
                JSONArray resultsArray = root.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    String address = jsonObject.getString("formatted_address");
                    String name = jsonObject.getString("name");
                    JSONObject geometry = jsonObject.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lon = location.getDouble("lng");

                    LocationModel locationModel = new LocationModel(lat, lon);
                    PlaceModel placeModel = new PlaceModel(address, name, locationModel);

                    mPlaceArrayList.add(placeModel);

                    CoreApplication.getInstance().logDebug(placeModel.getmLocation().getmLat()
                            + " / " + placeModel.getmLocation().getmLon() + " / "
                            + placeModel.getmAddress());
//                    mMainActivityModelListener.onAsyncTaskResult(mPlaceModel);

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (mPlaceArrayList.size() > 0) {
                if (mPlaceArrayList.size() == 1) {
                    mNumberOfPlaceTextView.setText(mPlaceArrayList.size() + " place");
                } else {
                    mNumberOfPlaceTextView.setText(mPlaceArrayList.size() + " places");
                }
                mLinearLayoutSearch.setVisibility(View.VISIBLE);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
            CoreApplication.getInstance().logDebug("on post");
            CoreApplication.getInstance().logDebug(s);
        }
    }

    public void searchPlace(String place) {

//        new async().execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
//                +place+"&key=AIzaSyAmVAh9yhy1g6M0wsiAjwzizJyQOyOa1vk");
//
//        CoreApplication.getInstance().logDebug("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
//                +place+"&key=AIzaSyAmVAh9yhy1g6M0wsiAjwzizJyQOyOa1vk");

        this.mPlaceService = ApiUtils.getPlaceService();
//        Observable<MyPojo> observable = mPlaceService.getPlaceList(place,
//                CoreApplication.getInstance().getResources().getString(R.string.map_api_key));
//
//        observable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<MyPojo>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        CoreApplication.getInstance().logDebug("subscribe");
//                    }
//
//                    @Override
//                    public void onNext(MyPojo myPojo) {
//                        if (myPojo.getResults().length > 0) {
//                            CoreApplication.getInstance().logDebug(myPojo.getResults()[0].getFormatted_address());
//                            mMainActivityModelListener.onSearchResult(myPojo.getResults());
//                        }else {
//                            CoreApplication.getInstance().showToast("not found");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        CoreApplication.getInstance().logDebug(e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        CoreApplication.getInstance().logDebug("complete");
//                    }
//                });

//        if (myPojoCall != null && myPojoCall.isExecuted()){
//            myPojoCall.cancel();
//        }

//        Call<MyPojo> myPojoCall = mPlaceService.getPlaceList(place,
//                CoreApplication.getInstance().getResources().getString(R.string.map_api_key));
//        CoreApplication.getInstance().logDebug(place);
//
//        myPojoCall.enqueue(new Callback<MyPojo>() {
//            @Override
//            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
//                CoreApplication.getInstance().logDebug(response.toString());
//                if (response.body().getResults().length > 0) {
//                    CoreApplication.getInstance().logDebug(response.body().getResults()[0].getFormatted_address());
//                    mMainActivityModelListener.onSearchResult(response.body().getResults());
//                } else {
//                    CoreApplication.getInstance().logDebug("not found");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyPojo> call, Throwable t) {
//                CoreApplication.getInstance().logDebug("fail" + t.toString());
//            }
//        });


//        mPlaceService.getPlaceList(place,
//                CoreApplication.getInstance().getResources().getString(R.string.map_api_key))
//                .enqueue(new Callback<MyPojo>() {
//            @Override
//            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
//                if (response.body().getResults().length > 0){
//                    CoreApplication.getInstance().logDebug(response.body().getResults()[0].getFormatted_address());
//                    mMainActivityModelListener.onSearchResult(response.body().getResults());
//                }else {
//                    CoreApplication.getInstance().logDebug("not found");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyPojo> call, Throwable t) {
//
//            }
//        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(Common.SAVED_PLACE_LIST, mSavedPlaceList);
        if (mPlaceArrayList.size() > 0) {
            outState.putParcelableArrayList(Common.PLACE_LIST, mPlaceArrayList);
        }
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        //restore marker list
        mSavedPlaceList = savedInstanceState.getParcelableArrayList(Common.SAVED_PLACE_LIST);
        if (mSavedPlaceList != null) {
            CoreApplication.getInstance().logDebug(mSavedPlaceList.size() + " size");
        }


        //restore search result list
        ArrayList<PlaceModel> placeModels = savedInstanceState.getParcelableArrayList(Common.PLACE_LIST);
        if (placeModels != null) {
            for (int i = 0; i < placeModels.size(); i++) {
                mPlaceArrayList.add(placeModels.get(i));
            }
        }

        if (mPlaceArrayList != null && mPlaceArrayList.size() > 0) {
            if (mPlaceArrayList.size() == 1) {
                mNumberOfPlaceTextView.setText(mPlaceArrayList.size() + " place");
            } else if (mPlaceArrayList.size() > 1){
                mNumberOfPlaceTextView.setText(mPlaceArrayList.size() + " places");
            }
            mLinearLayoutSearch.setVisibility(View.VISIBLE);
            mRecyclerViewAdapter.notifyDataSetChanged();
        } else {
            CoreApplication.getInstance().logDebug("nulllllll");
        }
    }
}

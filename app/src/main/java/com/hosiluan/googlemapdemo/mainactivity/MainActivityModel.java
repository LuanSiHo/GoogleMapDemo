package com.hosiluan.googlemapdemo.mainactivity;

import android.os.AsyncTask;

import com.hosiluan.googlemapdemo.CoreApplication;
import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.model.LocationModel;
import com.hosiluan.googlemapdemo.model.MyPojo;
import com.hosiluan.googlemapdemo.model.PlaceModel;
import com.hosiluan.googlemapdemo.model.Results;
import com.hosiluan.googlemapdemo.retrofit.ApiUtils;
import com.hosiluan.googlemapdemo.retrofit.PlaceService;
import com.hosiluan.googlemapdemo.retrofit.RetrofitClient;

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

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 11/13/2017.
 */

public class MainActivityModel {

    private PlaceService mPlaceService;
    private MainActivityModelListener mMainActivityModelListener;

    ArrayList<PlaceModel> mPlaceModel  = new ArrayList<>();

    public MainActivityModel(MainActivityModelListener mMainActivityModelListener) {
        this.mPlaceService = ApiUtils.getPlaceService();
        this.mMainActivityModelListener = mMainActivityModelListener;
    }

    public void searchPlace(String place) {

        new async().execute("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                +place+"&key=AIzaSyAmVAh9yhy1g6M0wsiAjwzizJyQOyOa1vk");

        CoreApplication.getInstance().logDebug("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                +place+"&key=AIzaSyAmVAh9yhy1g6M0wsiAjwzizJyQOyOa1vk");


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
//
//    public void cancelRequest() {
//        if (myPojoCall != null) {
//            myPojoCall.cancel();
//            CoreApplication.getInstance().logDebug("request != null");
//            if (myPojoCall.isExecuted()) {
//                myPojoCall.cancel();
//                CoreApplication.getInstance().logDebug("request is executing");
//            }
//        } else {
//            CoreApplication.getInstance().logDebug("request == null");
//        }
//    }


    interface MainActivityModelListener {
        void onSearchResult(Results[] results);
        void onAsyncTaskResult(ArrayList<PlaceModel> placeModels);
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
                JSONArray resultsArray  = root.getJSONArray("results");
                for (int i = 0 ; i < resultsArray.length(); i ++){
                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    String address = jsonObject.getString("formatted_address");
                    JSONObject geometry = jsonObject.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    double lat = location.getDouble("lat");
                    double lon = location.getDouble("lng");

                    LocationModel locationModel = new LocationModel(lat,lon);
                    PlaceModel placeModel = new PlaceModel(address,locationModel);

                    mPlaceModel.add(placeModel);

                    CoreApplication.getInstance().logDebug(placeModel.getmLocation().getmLat()
                    + " / " + placeModel.getmLocation().getmLon() + " / "
                    + placeModel.getmAddress());

                    mMainActivityModelListener.onAsyncTaskResult(mPlaceModel);

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
            CoreApplication.getInstance().logDebug("on post");
            CoreApplication.getInstance().logDebug(s);
        }
    }
}

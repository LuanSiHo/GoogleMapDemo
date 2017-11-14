package com.hosiluan.googlemapdemo.mainactivity;

import android.os.AsyncTask;

import com.hosiluan.googlemapdemo.CoreApplication;
import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.model.MyPojo;
import com.hosiluan.googlemapdemo.model.Results;
import com.hosiluan.googlemapdemo.retrofit.ApiUtils;
import com.hosiluan.googlemapdemo.retrofit.PlaceService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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

    public MainActivityModel(MainActivityModelListener mMainActivityModelListener) {
        this.mPlaceService = ApiUtils.getPlaceService();
        this.mMainActivityModelListener = mMainActivityModelListener;
    }

    public void searchPlace(String place) {

        CoreApplication.getInstance().logDebug("search place");

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

        mPlaceService.getPlaceList(place,
                CoreApplication.getInstance().getResources().getString(R.string.map_api_key))
                .enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                if (response.body().getResults().length > 0){
                    CoreApplication.getInstance().logDebug(response.body().getResults()[0].getFormatted_address());
                    mMainActivityModelListener.onSearchResult(response.body().getResults());
                }else {
                    CoreApplication.getInstance().logDebug("not found");
                }
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {

            }
        });
    }

    interface MainActivityModelListener {
        void onSearchResult(Results[] results);
    }

    class async extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CoreApplication.getInstance().logDebug(s);
        }
    }
}

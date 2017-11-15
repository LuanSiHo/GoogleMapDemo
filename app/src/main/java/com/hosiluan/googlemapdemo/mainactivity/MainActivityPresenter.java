package com.hosiluan.googlemapdemo.mainactivity;

import com.hosiluan.googlemapdemo.model.PlaceModel;
import com.hosiluan.googlemapdemo.model.Results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/13/2017.
 */

public class MainActivityPresenter implements MainActivityModel.MainActivityModelListener {

    private MainActivityModel mainActivityModel;
    private MainActivitPresenterListener mMainActivitPresenterListener;

    public MainActivityPresenter(MainActivitPresenterListener mMainActivitPresenterListener) {
        this.mMainActivitPresenterListener = mMainActivitPresenterListener;
        mainActivityModel = new MainActivityModel(this);
    }

    public void cancelRequest() {
//        mainActivityModel.cancelRequest();
    }

    public void searchPlace(String place) {
        mainActivityModel.searchPlace(place);
    }

    @Override
    public void onSearchResult(Results[] results) {
        mMainActivitPresenterListener.onSearchResult(results);
    }

    @Override
    public void onAsyncTaskResult(ArrayList<PlaceModel> placeModels) {
        mMainActivitPresenterListener.onAsyncTaskResult(placeModels);
    }


    interface MainActivitPresenterListener {
        void onSearchResult(Results[] results);

        void onAsyncTaskResult(ArrayList<PlaceModel> placeModels);
    }

}

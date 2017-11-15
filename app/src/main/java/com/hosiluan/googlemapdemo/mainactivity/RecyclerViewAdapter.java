package com.hosiluan.googlemapdemo.mainactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.model.PlaceModel;
import com.hosiluan.googlemapdemo.model.Results;

import java.util.ArrayList;

/**
 * Created by User on 11/13/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
//    private ArrayList<Results> mResults;
    private ArrayList<PlaceModel> mPlaceModels;
    private  RecyclerViewAdapterListener mRecyclerViewAdapterListener;

    public RecyclerViewAdapter(Context mContext, ArrayList<PlaceModel> mPlaceModels, RecyclerViewAdapterListener mRecyclerViewAdapterListener) {
        this.mContext = mContext;
        this.mPlaceModels = mPlaceModels;
        this.mRecyclerViewAdapterListener = mRecyclerViewAdapterListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_place, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final  int position) {
            PlaceModel placeModel = mPlaceModels.get(position);
        holder.placeTextView.setText(placeModel.getmName());
        holder.addressTextView.setText(placeModel.getmAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewAdapterListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
                if (mPlaceModels != null) {
            return mPlaceModels.size();
        }
        return 0;
    }

//    public RecyclerViewAdapter(Context mContext, ArrayList<Results> mResults,
//                               RecyclerViewAdapterListener mRecyclerViewAdapterListener) {
//        this.mContext = mContext;
//        this.mResults = mResults;
//        this.mRecyclerViewAdapterListener = mRecyclerViewAdapterListener;
//    }
//
//    @Override
//    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_place,parent,false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        Results results = mResults.get(position);
//        holder.placeTextView.setText(results.getName());
//        holder.addressTextView.setText(results.getFormatted_address());
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mRecyclerViewAdapterListener.onItemClick(position);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        if (mResults != null) {
//            return mResults.size();
//        }
//        return 0;
//    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView placeTextView,addressTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            placeTextView = (TextView) itemView.findViewById(R.id.tv_place);
            addressTextView = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    interface  RecyclerViewAdapterListener{
        void onItemClick(int position);
    }
}

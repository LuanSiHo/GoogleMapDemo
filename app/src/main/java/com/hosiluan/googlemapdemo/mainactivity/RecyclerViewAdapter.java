package com.hosiluan.googlemapdemo.mainactivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hosiluan.googlemapdemo.R;
import com.hosiluan.googlemapdemo.model.Results;

import java.util.ArrayList;

/**
 * Created by User on 11/13/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    private Context mContext;
    private ArrayList<Results> mResults;

    public RecyclerViewAdapter(Context mContext, ArrayList<Results> mResults) {
        this.mContext = mContext;
        this.mResults = mResults;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_place,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Results results = mResults.get(position);
        holder.textView.setText(results.getFormatted_address());
    }

    @Override
    public int getItemCount() {
        if (mResults != null) {
            return mResults.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_place);
        }
    }
}

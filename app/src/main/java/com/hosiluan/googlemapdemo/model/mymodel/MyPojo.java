package com.hosiluan.googlemapdemo.model.mymodel;

/**
 * Created by User on 11/15/2017.
 */


public class MyPojo
{
    private Predictions[] predictions;

    private String status;

    public Predictions[] getPredictions ()
    {
        return predictions;
    }

    public void setPredictions (Predictions[] predictions)
    {
        this.predictions = predictions;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [predictions = "+predictions+", status = "+status+"]";
    }
}
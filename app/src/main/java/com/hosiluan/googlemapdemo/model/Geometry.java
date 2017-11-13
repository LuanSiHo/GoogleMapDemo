package com.hosiluan.googlemapdemo.model;

/**
 * Created by User on 11/13/2017.
 */


public class Geometry
{
    private Viewport viewport;

    private String location_type;

    private Location location;

    public Viewport getViewport ()
    {
        return viewport;
    }

    public void setViewport (Viewport viewport)
    {
        this.viewport = viewport;
    }

    public String getLocation_type ()
    {
        return location_type;
    }

    public void setLocation_type (String location_type)
    {
        this.location_type = location_type;
    }

    public Location getLocation ()
    {
        return location;
    }

    public void setLocation (Location location)
    {
        this.location = location;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [viewport = "+viewport+", location_type = "+location_type+", location = "+location+"]";
    }
}
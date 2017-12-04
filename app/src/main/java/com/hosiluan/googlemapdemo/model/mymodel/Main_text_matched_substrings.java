package com.hosiluan.googlemapdemo.model.mymodel;

/**
 * Created by User on 11/15/2017.
 */


public class Main_text_matched_substrings
{
    private String length;

    private String offset;

    public String getLength ()
    {
        return length;
    }

    public void setLength (String length)
    {
        this.length = length;
    }

    public String getOffset ()
    {
        return offset;
    }

    public void setOffset (String offset)
    {
        this.offset = offset;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [length = "+length+", offset = "+offset+"]";
    }
}


package com.hosiluan.googlemapdemo.model.mymodel;

/**
 * Created by User on 11/15/2017.
 */

public class Predictions
{
    private String id;

    private String place_id;

    private Structured_formatting structured_formatting;

    private Matched_substrings[] matched_substrings;

    private String description;

    private Terms[] terms;

    private String[] types;

    private String reference;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public Structured_formatting getStructured_formatting ()
    {
        return structured_formatting;
    }

    public void setStructured_formatting (Structured_formatting structured_formatting)
    {
        this.structured_formatting = structured_formatting;
    }

    public Matched_substrings[] getMatched_substrings ()
    {
        return matched_substrings;
    }

    public void setMatched_substrings (Matched_substrings[] matched_substrings)
    {
        this.matched_substrings = matched_substrings;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public Terms[] getTerms ()
    {
        return terms;
    }

    public void setTerms (Terms[] terms)
    {
        this.terms = terms;
    }

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    public String getReference ()
    {
        return reference;
    }

    public void setReference (String reference)
    {
        this.reference = reference;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", place_id = "+place_id+", structured_formatting = "+structured_formatting+", matched_substrings = "+matched_substrings+", description = "+description+", terms = "+terms+", types = "+types+", reference = "+reference+"]";
    }
}
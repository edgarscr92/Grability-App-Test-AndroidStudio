package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Category
{
    @SerializedName("attributes")
    private Attributes attributes;

    public Category(Attributes attributes)
    {
        this.attributes = attributes;
    }

    public Attributes getAttributes(){ return attributes; }
}
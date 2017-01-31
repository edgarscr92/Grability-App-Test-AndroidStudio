package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Link
{
    @SerializedName("attributes")
    private Attributes attributes;

    public Link(Attributes attributes)
    {
        this.attributes = attributes;
    }

    public Attributes getAttributes() { return attributes; }
}

package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class ImPrice
{
    @SerializedName("label")
    private String label;
    @SerializedName("attributes")
    private Attributes attributes;

    public ImPrice(String label, Attributes attributes)
    {
        this.label = label;
        this.attributes = attributes;
    }

    public String getLabel() { return label; }
    public Attributes getAttributes() { return attributes; }
}

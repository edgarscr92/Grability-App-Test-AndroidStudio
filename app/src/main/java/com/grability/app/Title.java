package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Title
{
    @SerializedName("label")
    private String label;

    public Title(String label)
    {
        this.label = label;
    }

    public String getLabel() { return label; }
}
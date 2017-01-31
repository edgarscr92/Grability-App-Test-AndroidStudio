package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Rights
{
    @SerializedName("label")
    private String label;

    public Rights(String label)
    {
        this.label = label;
    }

    public String getLabel() { return label; }
}
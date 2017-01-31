package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Author
{
    @SerializedName("name")
    private Name name;
    @SerializedName("uri")
    private Uri uri;

    public Author(Name name, Uri uri)
    {
        this.name = name;
        this.uri = uri;
    }

    public Name getName(){ return name; }
    public Uri getUri() { return  uri; }
}

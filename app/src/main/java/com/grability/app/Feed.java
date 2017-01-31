package com.grability.app;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Feed implements Serializable
{
    @SerializedName("author")
    private Author author;
    @SerializedName("entry")
    private List<Entry> entry = new ArrayList<Entry>();
    @SerializedName("updated")
    private Updated updated;
    @SerializedName("rights")
    private Rights rights;
    @SerializedName("title")
    private Title title;
    @SerializedName("icon")
    private Icon icon;
    @SerializedName("link")
    private List<Link> link = new ArrayList<Link>();
    @SerializedName("id")
    private Id id;

    public Feed (Author author, List<Entry> entry, Updated updated, Rights rights, Title title, Icon icon, List<Link> link, Id id)
    {
        this.author = author;
        this.entry = entry;
        this.updated = updated;
        this.rights = rights;
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.id = id;
    }

    public Author getAuthor(){ return author; }
    public List<Entry> getEntry(){ return entry; }
    public Updated getUpdated(){ return updated; }
    public Rights getRights(){ return rights; }
    public Title getTitle(){ return title; }
    public Icon getIcon(){ return icon; }
    public List<Link> getLink(){ return link; }
    public Id getId(){ return id; }
}
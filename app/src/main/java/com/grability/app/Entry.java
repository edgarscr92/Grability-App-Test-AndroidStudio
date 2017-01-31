package com.grability.app;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Entry
{
    @SerializedName("im:Name")
    private ImName imName;
    @SerializedName("im:image")
    private List<ImImage> imImage = new ArrayList<ImImage>();
    @SerializedName("summary")
    private Summary summary;
    @SerializedName("im:price")
    private ImPrice imPrice;
    @SerializedName("im:contentType")
    private ImContentType imContentType;
    @SerializedName("rights")
    private Rights rights;
    @SerializedName("title")
    private Title title;
    @SerializedName("link")
    private Link link;
    @SerializedName("id")
    private Id id;
    @SerializedName("im:artist")
    private ImArtist imArtist;
    @SerializedName("category")
    private Category category;
    @SerializedName("im:releaseDate")
    private ImReleaseDate imReleaseDate;

    public Entry(ImName imName, List<ImImage> imImage, Summary summary, ImPrice imPrice, ImContentType imContentType, Rights rights, Title title, Link link, Id id, ImArtist imArtist, Category category, ImReleaseDate imReleaseDate)
    {
        this.imName = imName;
        this.imImage = imImage;
        this.summary = summary;
        this.imPrice = imPrice;
        this.imContentType = imContentType;
        this.rights = rights;
        this.title = title;
        this.link = link;
        this.id = id;
        this.imArtist = imArtist;
        this.category = category;
        this.imReleaseDate = imReleaseDate;
    }

    public ImName getImName(){ return imName; }
    public List<ImImage> getImImage(){ return imImage; }
    public Summary getSummary(){ return summary; }
    public ImPrice getImPrice(){ return imPrice; }
    public ImContentType getImContentType(){ return imContentType; }
    public Rights getRights(){ return rights; }
    public Title getTitle(){ return title; }
    public Link getLink(){ return link; }
    public Id getId(){ return id; }
    public ImArtist getImArtist(){ return imArtist; }
    public Category getCategory(){ return category; }
    public ImReleaseDate getImReleaseDate(){ return imReleaseDate; }
}
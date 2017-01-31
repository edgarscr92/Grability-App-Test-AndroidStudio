package com.grability.app;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Edgar C on 23/1/2017.
 * www.Carrera-Brothers.com
 */

public class Attributes
{
    @SerializedName("label")
    private String label;
    @SerializedName("rel")
    private String rel;
    @SerializedName("type")
    private String type;
    @SerializedName("href")
    private String href;
    @SerializedName("schema")
    private String schema;
    @SerializedName("term")
    private String term;
    @SerializedName("im:id")
    private String imId;
    @SerializedName("im:bundleId")
    private String imBundleId;
    @SerializedName("amount")
    private String amount;
    @SerializedName("currency")
    private String currency;
    @SerializedName("height")
    private String height;

    public Attributes(){

    }

    public Attributes(String label, String rel, String type, String href, String schema, String term, String imId, String imBundleId, String amount, String currency, String height) {
        this.label = label;
        this.rel = rel;
        this.type = type;
        this.href = href;
        this.schema = schema;
        this.term = term;
        this.imId = imId;
        this.imBundleId = imBundleId;
        this.amount = amount;
        this.currency = currency;
        this.height = height;
    }

    public String getLabel() { return label; }
    public String getRel() { return rel; }
    public String getType() { return type; }
    public String getHref() { return href; }
    public String getSchema() { return schema; }
    public String getTerm() { return term; }
    public String getImId() { return imId; }
    public String getImBundleId() { return imBundleId; }
    public String getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public String getHeight() { return height; }
}

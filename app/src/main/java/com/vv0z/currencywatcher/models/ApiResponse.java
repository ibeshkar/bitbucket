package com.vv0z.currencywatcher.models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;


public class ApiResponse {

    @SerializedName("base")
    @Expose
    private String base;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("rates")
    @Expose
    private JsonElement rates;

    public String getBase() {
        return base;
    }

    public String getDate() {
        return date;
    }

    public JsonElement getRates() {
        return rates;
    }
}

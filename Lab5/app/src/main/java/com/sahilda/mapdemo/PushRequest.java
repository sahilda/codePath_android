package com.sahilda.mapdemo;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

public class PushRequest {

    public String markerId;
    public String title;
    public String installationId;
    public String snippet = "";
    public LatLng mapLocation;
    public String color;

    public PushRequest(JSONObject data) throws JSONException {
        JSONObject location = data.getJSONObject("location");
        markerId = data.getString("markerId");
        title = data.getString("title");
        snippet = data.optString("snippet", "");
        installationId = data.getString("installationId");
        color = data.getString("color");

        mapLocation = new LatLng(location.getDouble("lat"),
                location.getDouble("long"));

    }

}
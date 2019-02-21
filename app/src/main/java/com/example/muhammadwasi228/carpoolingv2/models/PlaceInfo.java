package com.example.muhammadwasi228.carpoolingv2.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by muhammadwasi228 on 12/4/2017.
 */

public class PlaceInfo {
    private String name;
    private String address;
    private String id;
    private LatLng latLng;

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", id='" + id + '\'' +
                ", latLng=" + latLng +
                '}';
    }

    public PlaceInfo(String name, String address, String id, LatLng latLng) {
        this.name = name;
        this.address = address;
        this.id = id;
        this.latLng = latLng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public PlaceInfo() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

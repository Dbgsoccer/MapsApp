package com.example.derek.mapsapp;

/**
 * Created by Derek on 11/29/2016.
 */

public class Loc {
    String name;
    Double lat;
    Double lon;
    String desc;

    public Loc(String n, Double la, Double lo) {
        name = n;
        lat = la;
        lon = lo;
    }

    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }
}
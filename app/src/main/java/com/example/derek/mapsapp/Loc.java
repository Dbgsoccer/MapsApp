package com.example.derek.mapsapp;

/**
 * Created by Derek on 11/29/2016.
 */

public class Loc {
    String name;
    Double lat;
    Double lon;
    String desc;
    String catag;
    String website;

    public Loc(String n, Double la, Double lo, String des, String cat, String web) {
        name = n;
        lat = la;
        lon = lo;
        desc = des;
        catag = cat;
        website = web;
    }

    public String getName() {return name;}

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getDesc()  {return desc;}

    public String getCatag()  {return catag;}
    public String getWebsite()  {return website;}


}
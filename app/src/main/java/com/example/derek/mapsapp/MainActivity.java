package com.example.derek.mapsapp;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static com.example.derek.mapsapp.R.id.map;
import static com.google.android.gms.R.id.center;


public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback {
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng farmington = new LatLng(44.66899,-70.14638);
        LatLng waterville = new LatLng(44.55194, -69.64584);
        //LatLng resources[] = {farmington, waterville};
        HashMap resources = new HashMap<String, LatLng>();
        resources.put("Farmington", farmington);
        resources.put("Waterville", waterville);

        /*for (int i=0; i<resources.length; i++){
            mMap.addMarker(new MarkerOptions().position(resources[i]).title("Pin"));
        }*/
        Iterator it = resources.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            LatLng temp1 = (LatLng) pair.getValue();
            String temp2 = (String) pair.getKey();
            mMap.addMarker(new MarkerOptions().position(temp1).title(temp2));
        }
        //mMap.addMarker(new MarkerOptions().position(farmington).title("Marker in Farminton"));
        //mMap.addMarker(new MarkerOptions().position(waterville).title("Marker in Waterville"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(farmington));
        mMap.setMinZoomPreference(5);
        //mMap.setMaxZoomPreference(1000);d


    }

}

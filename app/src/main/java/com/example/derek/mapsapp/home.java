package com.example.derek.mapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.MapFragment;

import static com.example.derek.mapsapp.R.id.map;
import static com.example.derek.mapsapp.R.id.resourceCatag;

/**
 * Created by Derek on 12/6/2016.
 */

public class home extends FragmentActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);


    }
    protected void openMap(View v) {
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);

    }
}

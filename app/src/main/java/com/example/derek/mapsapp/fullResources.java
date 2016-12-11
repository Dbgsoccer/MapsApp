package com.example.derek.mapsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.identity.intents.AddressConstants;

/**
 * Created by Derek on 12/9/2016.
 */

public class fullResources extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_full_desc);

        TextView fullResource = (TextView) findViewById(R.id.fullResource);
        //TextView fullResourceAddress = (TextView) findViewById(R.id.fullResourceAddress);
        //TextView fullResourceDesc = (TextView) findViewById(R.id.fullResourceDescription);

        Intent intent = getIntent();

        fullResource.setText(intent.getStringExtra("Title")+"\n"+"\n"+intent.getStringExtra("Description"));
        //fullResourceAddress.setText(intent.getStringExtra("Address"));
        //fullResourceDesc.setText(intent.getStringExtra("Title")+"\n"+intent.getStringExtra("Description"));


    }
}
package com.example.derek.mapsapp;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.derek.mapsapp.R.id.activity_main;
import static com.example.derek.mapsapp.R.id.map;
import static com.example.derek.mapsapp.R.id.popupDesc;
import static com.example.derek.mapsapp.R.id.spinner2;
//import static com.example.derek.mapsapp.R.id.textView2;
import static com.google.android.gms.R.id.center;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.InputStreamReader;



public class viewMap extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private String link = "https://spreadsheets.google.com/tq?key=1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k";
    String json = "";
    Spinner resourceCatag;
    ArrayAdapter<String> adapter;
    ArrayList<Loc> locs;
    ArrayList<String> dataCatag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        downloadSheet();

        dataCatag = new ArrayList<String>();
        locs = new ArrayList<Loc>();
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataCatag);
        resourceCatag = (Spinner) findViewById(R.id.spinner2);
        resourceCatag.setAdapter(adapter);

        // void addHeaderView (resourceCatag, catagory,true);



    }

    public void downloadSheet() {
        try {
            new DownloadThread().execute(new URL(link));

        }
        catch (Exception e) {
            Log.v("url","bad url");
        }
    }


    class DownloadThread extends AsyncTask<URL, Integer, String> {
        private String link = "https://spreadsheets.google.com/tq?key=1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k";
        String result;
        String name, desc, catag, website;
        Double lat, lon;
        String chosenCat = ""; //this is what will change based on what they choose

        protected String doInBackground(URL... urls) {
            return download();
        }


        @Override
        protected void onPostExecute(String result) {

            for(int i=0;i<locs.size();i++) {
                Loc loc = locs.get(i);
                String name = loc.getName();
                Double lat = loc.getLat();
                Double lon = loc.getLon();
                String desc = loc.getDesc();
                String catag = loc.getCatag();
                String website = loc.getWebsite();
                // Iterate through resources to look for different catagories,
                // if a idfferent catagory is found, place it into an arroy kist which will bult up a listview
                if (!dataCatag.contains(catag)){
                    dataCatag.add(catag);
                    //use dataCatag to populate listview
                    Log.d("loc", "info about Categories in Database: " + dataCatag.toString());
                }
                Log.d("loc","info about catag: " + catag + "," + chosenCat);
                Log.d("loc","info about loc: " + name + "," + lat + "," + lon+","+ desc);

            }
            resourceCatag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    chosenCat = dataCatag.get(position);
                    mMap.clear();
                    LatLng cameraLoc = new LatLng(44.66899, -70.14638);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraLoc));
                    mMap.setMaxZoomPreference(8);
                    for (int i = 0; i < locs.size(); i++) {
                        Loc loc = locs.get(i);
                        String name = loc.getName();
                        Double lat = loc.getLat();
                        Double lon = loc.getLon();
                        String desc = loc.getDesc();
                        String catag = loc.getCatag();
                        String website = loc.getWebsite();

                        if (chosenCat.equals(catag)) {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(name).snippet(desc));
                            Log.d("loc", "added loc: " + catag + "," + chosenCat);
                        }
                    }

                }
            });

            adapter.notifyDataSetChanged();
        }

        private String download() {
            InputStream stream = null;
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int responseCode = conn.getResponseCode();
                stream = conn.getInputStream();

                json = getString(stream);
                parseJSON();
                return json;


            } catch (Exception e) {
                Log.v("io", "uh oh 1: " + e.toString());
            } finally {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Exception e) {
                        Log.v("io", "uh oh 2");
                    }
                }
            }
            return "";
        }
        private String getString(InputStream stream) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                Log.v("io","trouble reading lines");
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }

        private void parseJSON() {

            int start = json.indexOf("{", json.indexOf("{") + 1);
            int end = json.lastIndexOf("}");
            String jsonResponse = json.substring(start, end);
            JSONObject table;
            try {
                table = new JSONObject(jsonResponse);
                Log.v("json",table.toString());
                //{"cols":[{"id":"A","label":"","type":"string"},{"id":"B","label":"","type":"number","pattern":"General"},{"id":"C","label":"","type":"number","pattern":"General"}],"rows":[{"c":[{"v":"UMF"},{"v":44.670029,"f":"44.670029"},{"v":-70.149635,"f":"-70.149635"}]}]}
                //[{"c":[{"v":"UMF"},{"v":44.670029,"f":"44.670029"},{"v":-70.149635,"f":"-70.149635"}]
                JSONArray rows = table.getJSONArray("rows");
                for(int i=0;i<rows.length();i++) {
                    name = rows.getJSONObject(i).getJSONArray("c").getJSONObject(0).getString("v");
                    lat = rows.getJSONObject(i).getJSONArray("c").getJSONObject(1).getDouble("v");
                    lon = rows.getJSONObject(i).getJSONArray("c").getJSONObject(2).getDouble("v");
                    desc = rows.getJSONObject(i).getJSONArray("c").getJSONObject(3).getString("v");
                    catag = rows.getJSONObject(i).getJSONArray("c").getJSONObject(4).getString("v");
                    website = rows.getJSONObject(i).getJSONArray("c").getJSONObject(5).getString("v");
                    Log.d("loc","info about catag: " + name);
                    //this add function isnt working now, it says locs is null
                    locs.add(new Loc(name,lat,lon,desc,catag,website));
                    Log.d("loc","info about catag: " + desc);

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("json","whoopsie");

            }
            Log.v("json","leaving parseJSON()");

        }



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Geocoder geocoder = new Geocoder(this, Locale.US);
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        LatLng cameraLoc = new LatLng(44.66899, -70.14638);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraLoc));
        mMap.setMinZoomPreference(8);

    }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), fullResources.class);
        //pack in info
        for (int i = 0; i < locs.size(); i++) {
            Loc loc = locs.get(i);
            if(marker.getTitle().equals(loc.getName())){
                intent.putExtra("Website",loc.getWebsite());
            }

        }

        intent.putExtra("Title",marker.getTitle());
        intent.putExtra("Address",marker.getPosition());
        intent.putExtra("Description",marker.getSnippet());

        //start activity
        startActivity(intent);



    }



}







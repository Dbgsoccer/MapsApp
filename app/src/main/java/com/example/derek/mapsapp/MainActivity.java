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
import android.util.Log;
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

import static com.example.derek.mapsapp.R.id.map;
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

//AIzaSyCVXCFkOIJkJO3GIBVtSZNMifrPg5XsgPc geocoder api key

public class MainActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private String link = "https://spreadsheets.google.com/tq?key=1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k";
    String json = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        downloadSheet();
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

        //        String json;
        //private String link ="https://docs.google.com/spreadsheets/d/1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k/edit#gid=0";
        private String link = "https://spreadsheets.google.com/tq?key=1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k";
        String result;
        String name, desc, catag;
        Double lat, lon;
        ArrayList<Loc> locs = new ArrayList<Loc>();

        protected String doInBackground(URL... urls) {
            return download();
        }

        //Bellow I need to figure out how to make map markers
        @Override
        protected void onPostExecute(String result) {
            /*
            TextView txt = (TextView) findViewById(R.id.name);
            txt.setText(name); // txt.setText(result);
            TextView la = (TextView) findViewById(R.id.lat);
            la.setText(lat); // txt.setText(result);
            TextView lo = (TextView) findViewById(R.id.lon);
            lo.setText(lon); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
            */
            for(int i=0;i<locs.size();i++) {
                Loc loc = locs.get(i);
                String name = loc.getName();
                Double lat = loc.getLat();
                Double lon = loc.getLon();
                String desc = loc.getDesc();
                String catag = loc.getCatag();

                // down bellow, add "description" onto name, and pull that from the json.
                LatLng resLoc = new LatLng(lat,lon);
                mMap.addMarker(new MarkerOptions().position(resLoc).title(name).snippet(desc));

                Log.v("loc","info about loc: " + name + "," + lat + "," + lon+","+ desc);

            }
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
                // json = parseJSON();

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
                    locs.add(new Loc(name,lat,lon,desc,catag));
                    //double resLoc = lat, lon;
                    //now make a marker based on name/lat/lon and add it to map
                   // HashMap resources = new HashMap<String, LatLng>();
                    //

                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.v("json","whoopsie");

            }
            Log.v("json","leaving parseJSON()");

        }


/*    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }
*/
    /*
    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }
    */
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Geocoder geocoder = new Geocoder(this, Locale.US);
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);




        //HashMap resources = new HashMap<String, LatLng>();


       /* Iterator it = resources.entrySet().iterator();
        while (it.hasNext()){
            //Map.Entry pair = (Map.Entry)it.next();
            LatLng temp1 = (LatLng) pair.getValue();
            String temp2 = (String) pair.getKey();
            mMap.addMarker(new MarkerOptions().position(temp1).title(temp2));
        }*/
        //mMap.addMarker(new MarkerOptions().position(farmington).title("Marker in Farminton"));
        //mMap.addMarker(new MarkerOptions().position(waterville).title("Marker in Waterville"));
        LatLng cameraLoc = new LatLng(44.66899, -70.14638);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraLoc));
        mMap.setMinZoomPreference(8);
        //mMap.setMaxZoomPreference(1000);d
        }
    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info window clicked", Toast.LENGTH_SHORT).show();



    }

       /* catch(IOException e) {
            Log.e("IOException", e.getMessage());
            Toast.makeText(this, "IOException:  " + e.getMessage(), 20).show();
        }*/

    }







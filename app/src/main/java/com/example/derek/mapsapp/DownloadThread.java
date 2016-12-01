package com.example.derek.mapsapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by chris.bennett on 11/17/16.
 */

public class DownloadThread extends AsyncTask<URL, Integer, Void> {

    String json;
    private String link = "https://spreadsheets.google.com/tq?key=1e1uR178i86TmNOslKp_Qhmz1h7fNFp8AH9R6THdRp2k";
    String result;
    protected Void doInBackground(URL... urls) {
        download();
        return null;
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
            return parseJSON();

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
        return null;
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

    private String parseJSON() {

        int start = json.indexOf("{", json.indexOf("{") + 1);
        int end = json.lastIndexOf("}");
        String jsonResponse = json.substring(start, end);
        try {
            JSONObject table = new JSONObject(jsonResponse);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonResponse;

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
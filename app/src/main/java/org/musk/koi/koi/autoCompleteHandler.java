package org.musk.koi.koi;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Ro0ohy on 1/31/2015.
 */
public class autoCompleteHandler {
    private static final String LOG_TAG = "KOI.MUSK.ORG";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyC6U7uDpYQDoZOkLIwYD9juDcJSFS60xZM";

    public static ArrayList<String> autocomplete(String input) {
        Log.e("Ehsan"," input is "+input);
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));


//            LocationManager  lm = (LocationManager)(new MainActivity()).getSystemService(Context.LOCATION_SERVICE);
           URL url = new URL(sb.toString());
//            Criteria criteria = new Criteria(); criteria.setAccuracy(Criteria.ACCURACY_FINE);
//            criteria.setCostAllowed(false);
//            String providerName = lm.getBestProvider(criteria, true);
//            Location loc = lm.getLastKnownLocation(providerName);


            url = new URL("http://koi.cloudapp.net/api/Authentication/GetPlacesFromGoogle?query="+URLEncoder.encode( input)+"&location=33.974415,-118.422573");
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                Log.e("Ehsan", "miadd??? nemiad ");
                jsonResults.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("roohy"," result is "+jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        Log.e("Rooohy","after getting everything in try part, auto comlplete fucktard "+resultList.size());
        return resultList;
    }
}

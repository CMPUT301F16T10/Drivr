/*
 * Copyright 2016 CMPUT301F16T10
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package ca.ualberta.cs.drivr;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;

/**
 * An activity that shows information about a request.
 */
public class RequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView routeText;
    private TextView fareText;
    private GoogleMap map;
    private SupportMapFragment mapFragment;
    private Context mContext;
    private MapController mapController;
    private Place sourcePlace;
    private Place destinationPlace;

    /**
     * Used as a key for passing a request into this activity as an extra to an intent.
     */
    public static final String EXTRA_REQUEST = "ca.ualberta.cs.drivr.RequestActivity.EXTRA_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        routeText = (TextView) findViewById(R.id.request_route_text);
        fareText = (TextView) findViewById(R.id.request_fare_text);
        // map = findViewById(R.id.request_map_fragment);

        String requestString = getIntent().getStringExtra(EXTRA_REQUEST);
        Gson gson = new Gson();
        Request request = gson.fromJson(requestString, Request.class);

        // TODO make a map with these points and the route between them
        mapFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.request_map_fragment);
        mapFragment.getMapAsync(this);

        // sourcePlace = request.getSourcePlace();
        // destinationPlace = request.getDestinationPlace();

        mContext = getApplicationContext();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final MapController mapController = new MapController(map,mContext);
        if(sourcePlace != null && destinationPlace != null) {
            mapController.addRequestOnMap(sourcePlace.getLatLng(), destinationPlace.getLatLng());
        }
    }
}

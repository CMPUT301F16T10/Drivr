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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.MapFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static ca.ualberta.cs.drivr.R.id.map;

/**
 * Setting this up for JavaDocs.
 */
public class RequestActivity extends AppCompatActivity {
    private TextView routeText;
    private TextView fareText;
    private MapFragment map;
    public static final String EXTRA_REQUEST = "ca.ualberta.cs.drivr.RequestActivity.EXTRA_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        routeText = (TextView) findViewById(R.id.request_route_text);
        fareText = (TextView) findViewById(R.id.request_fare_text);
//        map = findViewById(R.id.request_map_fragment);

        String requestString = getIntent().getStringExtra(EXTRA_REQUEST);
        Gson gson = new Gson();
        Request request = gson.fromJson(requestString, Request.class);

//        R.id.request_map_fragment
//        TODO make a map with these points and the route between them
        Place sourcePlace = request.getSourcePlace();
        Place destinationPlace = request.getDestinationPlace();

    }
}

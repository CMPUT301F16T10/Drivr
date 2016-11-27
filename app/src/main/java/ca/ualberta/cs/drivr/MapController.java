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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by justin on 09/11/16.
 *
 * A class for Modifying the google map
 *
 * Uses a open source library to assist with polyline route drawing
 * @see https://github.com/akexorcist/Android-GoogleDirectionLibrary
 * @see DirectionCallback
 *
 */
public class MapController implements DirectionCallback{

    private GoogleMap map;
    private Location myLocation;
    private LatLng destinationLatLng;
    private LatLng pickupLatlng;

    private int zoom = 10;
    private final static float KM = 1000;

    private Double myLatitude;
    private Double myLongitude;
    private Context mContext;
    private ArrayList<LatLng> markers = new ArrayList<LatLng>();
    private LatLng pickup;

    private UserManager userManager = UserManager.getInstance();
    private RequestsListController requestsListController;

    private static final String TAG = "MainActivity";

    private Marker pickupMarker;
    private Marker destinationMarker;
    private Geocoder geocoder;

    private ArrayList<Marker> pickupArrayList = new ArrayList<Marker>();
    private ArrayList<Marker> destinationArrayList = new ArrayList<Marker>();

    private GPSTracker gpsTracker;
    private final String serverKey = "AIzaSyB13lv5FV6dbDRec8NN173qj4HSHuNmPHE";


    /**
     * Instantiates a new map controller.
     * @param map The google map.
     * @param context The activities context.
     */
    public MapController(GoogleMap map, Context context) {
        // Initialize map
        this.mContext = context;
        this.map = map;
        gpsTracker = new GPSTracker(context);
        // Initialize location

        myLocation = gpsTracker.getMyLocation();
        myLatitude = myLocation.getLatitude();
        myLongitude = myLocation.getLongitude();

        geocoder = new Geocoder(mContext);
        requestsListController = new RequestsListController(userManager);

        // Zoom map in to current Location
        zoomToCurrentLocation();
    }

    /**
     * Adds a Destination Marker to the map.
     * @param latLng a point on the map.
     */
    public void addDestination(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Destination");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.draggable(true);

        destinationMarker = map.addMarker(markerOptions);
        pickupArrayList.add(destinationMarker);
    }

    /**
     * Zooms the camera angle on the Map to the User Location.
     */
    public void zoomToCurrentLocation(){
        // Camera
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLatitude, myLongitude), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(myLatitude, myLongitude))      // Sets the center of the map to location user
                .zoom(10)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    public GoogleMap getMap() {
        return map;
    }

    /**
     * Adds a Pickup Marker to the map.
     * @param latLng a point on the map.
     */
    public void addPickUp(LatLng latLng){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Pickup Location");
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        pickupMarker = map.addMarker(markerOptions);
        pickupArrayList.add(pickupMarker);
    }

    /**
     * Queries a route with google direction api to the map.
     * @param pickup point on the map.
     * @param destination pickup on the map.
     */
    public void addRequestOnMap(LatLng pickup, LatLng destination){
        this.pickupLatlng = pickup;
        this.destinationLatLng = destination;
        GoogleDirection.withServerKey(serverKey)
                .from(pickup)
                .to(destination)
                .transitMode(TransportMode.DRIVING)
                .execute(this);
    }

    /**
     * After a click on Map adds a marker to the map.
     * Shows Dialog to confirm pickup and drop off location.
     * @param mapMarker point on the map.
     * @param context context from activity.
     */
    public void addPendingRequest(final LatLng mapMarker, final Context context) {
        if (markers.size() == 0) {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Pickup Location")
                    .setMessage("Is this the Location you want to be picked up from?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addPickUp(mapMarker);
                            pickup = mapMarker;
                            markers.add(mapMarker);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .show();
        }
        // Pickup Location Already Set Up
        else if(markers.size() == 1) {
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Destination Location")
                    .setMessage("Is this the Location you want to be dropped off at?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            addDestination(mapMarker);
                            markers.add(mapMarker);
                            addRequestOnMap(pickup,mapMarker);
                            createRequest(pickup,mapMarker);



                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing
                        }
                    })
                    .show();
        }
        else {
            // Delete
            //map.clear();
            //markers.clear();
        }
    }

    public void createRequest(LatLng pickupLatlng, LatLng destinationLatLng){
        try {
            List<Address> pickupList = geocoder.getFromLocation(pickupLatlng.latitude, pickupLatlng.longitude, 1);
            List<Address> destinationList = geocoder.getFromLocation(destinationLatLng.latitude, destinationLatLng.longitude, 1);

            Address pickupAddress = pickupList.get(0);
            Address destinationAddress = destinationList.get(0);
            ConcretePlace pickupPlace = new ConcretePlace(pickupAddress);
            ConcretePlace destinationPlace = new ConcretePlace(destinationAddress);

            Gson gson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();

            Intent intent = new Intent(mContext, NewRequestActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            String concretePlaceJsonPick = gson.toJson(pickupPlace);
            String concretePlaceJsonDest = gson.toJson(destinationPlace);

            intent.putExtra(NewRequestActivity.EXTRA_PLACE, concretePlaceJsonDest);
            Log.i(TAG, "Place: " + destinationPlace.getName() + ", :" + destinationPlace.getLatLng());

            intent.putExtra("PICK_UP", concretePlaceJsonPick);
            Log.i(TAG, "Place: " + pickupPlace.getName() + ", :" + pickupPlace.getLatLng());

            mContext.startActivity(intent);

           // Request request = new Request(userManager.getUser(), new ConcretePlace(pickupAddress), new ConcretePlace(destinationAddress));
            //requestsListController.addRequest(request);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the Map and allow another request
     */
    public void clearMap(){
        map.clear();
        markers.clear();
    }


    public void requestCenter(LatLng pickup, LatLng destination) {


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(pickup);
        builder.include(destination);

        final LatLngBounds bounds = builder.build();


        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,0);
        final CameraUpdate cameraUpdate1 = CameraUpdateFactory.zoomOut();

        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback(){

            @Override
            public void onMapLoaded() {
                map.moveCamera(cameraUpdate);
                map.animateCamera(cameraUpdate1);
            }
        });


    }

    /**
     * Queries a route with google direction api to the map
     * @see <a href="https://github.com/akexorcist/Android-GoogleDirectionLibrary">Android-GoogleDirectionLibrary</a>.
     */
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            map.addMarker(new MarkerOptions().position(pickupLatlng).title("Pickup").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            map.addMarker(new MarkerOptions().position(destinationLatLng).title("Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            map.addPolyline(DirectionConverter.createPolyline(mContext,directionPositionList,3, Color.RED));
        }
    }

    // If marker can't be added to map
    @Override
    public void onDirectionFailure(Throwable t) { }
}

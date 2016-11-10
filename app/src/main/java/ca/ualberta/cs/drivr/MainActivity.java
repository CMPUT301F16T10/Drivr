/*
 *  Copyright 2016 CMPUT301F16T10
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ca.ualberta.cs.drivr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

//import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
//import static android.Manifest.permission.ACCESS_FINE_LOCATION;


/**
 * Setting this up for JavaDocs.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        DirectionCallback{

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng pickup;
    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleMap mMap;
    private SupportMapFragment mFragment;
    private Activity activity;
    private MapController mapController;
    private ArrayList<LatLng> markers = new ArrayList<LatLng>();
    private Context context;
    LatLng test = new LatLng(53.5232, -113.5263);
    LatLng test2 = new LatLng(53.5225, -113.6242);
    private static final String SERVER_KEY = "AIzaSyB13lv5FV6dbDRec8NN173qj4HSHuNmPHE";

    // Location for User
    double myLatitude = 0;
    double myLongitude = 0;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

//    private  mLatitudeText;
//    private Location currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Looks better without Blue Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        context = getApplicationContext();

        setSupportActionBar(toolbar);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.map);


        // get map
        mFragment.getMapAsync(this);


        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

//        autocompleteFragment.setBoundsBias(new LatLngBounds(latLng, latLng));

//        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
//                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_REGIONS)
//                .build();

//        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                ConcretePlace concretePlace = new ConcretePlace(place);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                Intent intent = new Intent(MainActivity.this, NewRequestActivity.class);
                String concretePlaceJson = gson.toJson(concretePlace);
                intent.putExtra(NewRequestActivity.EXTRA_PLACE, concretePlaceJson);
                Log.i(TAG, "Place: " + place.getName() + ", :" + place.getLatLng());
                startActivity(intent);
                // Checking Button
                //Toast.makeText(context, "Test For Place", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    // Map
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        final MapController mapController = new MapController(mMap, context);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        //test
        mapController.addRequestOnMap(test,test2);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {



                if(markers.size() == 0){
                    mapController.addPickUp(latLng);
                    pickup = latLng;
                    markers.add(latLng);
                }
                else if(markers.size() == 1){
                    mapController.addDestination(latLng);
                    markers.add(latLng);
                    mapController.addRequestOnMap(pickup,latLng);
                }
                else {
                    mMap.clear();
                    markers.clear();
                }


            }
        });
    }




    //https://github.com/akexorcist/Android-GoogleDirectionLibrary
    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this,data);

            } else if(resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this,data);
            }
        } else if (resultCode == RESULT_CANCELED){
            // do nothing
        }
    }


    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewSettings() {
        throw new UnsupportedOperationException();
    }

    public void viewUserProfile() {
        throw new UnsupportedOperationException();
    }

    public void viewRequests() {
        throw new UnsupportedOperationException();
    }

    public void viewRequestHistory() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "permissions check");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "permissions failed");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        final LocationRequest locationRequest = new LocationRequest();
//        locationRequest.snRequest.setInt
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                        mLastLocation = locationResult.getLastLocation();

                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                autocompleteFragment.setBoundsBias(new LatLngBounds(latLng, latLng));
                Log.i(TAG, "updated the location bounds in listener" + latLng.toString());
            }
        });

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

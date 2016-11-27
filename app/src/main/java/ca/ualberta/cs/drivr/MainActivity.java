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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
//import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.model.Direction;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Setting this up for JavaDocs.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, DirectionCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteFragment autocompleteFragment;
    private GoogleMap mMap;
    private SupportMapFragment mFragment;
    private EditText keywordEditText;
    private Context context;
    private UserManager userManager = UserManager.getInstance();
    LatLng test = new LatLng(53.5232, -113.5263);
    private static final String SERVER_KEY = "AIzaSyB13lv5FV6dbDRec8NN173qj4HSHuNmPHE";




    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    /*
    https://github.com/Clans/FloatingActionButton/issues/273
    Author: gwilli on GitHub
    Accessed: November 10, 2016

    Add support for vector drawables in older versions of android
    */
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Looks better without Blue Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        context = getApplicationContext();
        PreferenceManager.setDefaultValues(this, R.xml.pref_driver_mode, false);

        setSupportActionBar(toolbar);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mFragment = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.main_map);
        mFragment.getMapAsync(this);

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                ConcretePlace concretePlace = new ConcretePlace(place);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                if (userManager.getUserMode().equals(UserMode.RIDER)){
                    Intent intent = new Intent(MainActivity.this, NewRequestActivity.class);
                    String concretePlaceJson = gson.toJson(concretePlace);
                    intent.putExtra(NewRequestActivity.EXTRA_PLACE, concretePlaceJson);
                    Log.i(TAG, "Place: " + place.getName() + ", :" + place.getLatLng());
                    startActivity(intent);

                } else if (userManager.getUserMode().equals(UserMode.DRIVER)) {
                    Intent intent = new Intent(MainActivity.this, SearchRequestActivity.class);
                    String concretePlaceJson = gson.toJson(concretePlace);
                    intent.putExtra(SearchRequestActivity.EXTRA_PLACE, concretePlaceJson);
                    intent.putExtra(SearchRequestActivity.EXTRA_KEYWORD, "");
                    Log.i(TAG, "Place: " + place.getName() + ", :" + place.getLatLng());
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

        // Using the floating action button menu system
        final FloatingActionMenu fabMenu = (FloatingActionMenu) findViewById(R.id.main_fab_menu);
        FloatingActionButton fabSettings = (FloatingActionButton) findViewById(R.id.fabSettings);
        FloatingActionButton fabRequests = (FloatingActionButton) findViewById(R.id.main_fab_requests);
        FloatingActionButton fabProfile = (FloatingActionButton) findViewById(R.id.main_fab_profile);
        FloatingActionButton fabHistory = (FloatingActionButton) findViewById(R.id.main_fah_history);
        FloatingActionButton fabLogin = (FloatingActionButton) findViewById(R.id.main_fab_login);
        final FloatingActionButton fabDriver = (FloatingActionButton) findViewById(R.id.main_driver_mode);
        final FloatingActionButton fabRider = (FloatingActionButton) findViewById(R.id.main_rider_mode);

        /*
        Change between user and driver mode. Will probably be replaced with an option in settings.
        For now the visibility of this is set to gone because we should not have too many FABs.
        Having too many FABs may cause confusion and rendering issues on small screens.
        */
        keywordEditText = (EditText) findViewById(R.id.main_keyword_edit_text);
        FloatingActionButton fabMode = (FloatingActionButton) findViewById(R.id.main_fab_mode);
        fabMode.setVisibility(View.GONE);
        if (userManager.getUserMode().equals(UserMode.RIDER)) {
            fabRider.setVisibility(View.GONE);
            keywordEditText.setVisibility(View.GONE);
        } else {
            fabDriver.setVisibility(View.GONE);
            keywordEditText.setVisibility(View.VISIBLE);
        }

        keywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    TextView textView = (TextView) findViewById(R.id.search_keyword_name);
//                    textView.setText(keywordEditText.getText().toString());
                    Intent intent = new Intent(MainActivity.this, SearchRequestActivity.class);
//                    String concretePlaceJson = gson.toJson(concretePlace);
                    intent.putExtra(SearchRequestActivity.EXTRA_PLACE, "");
                    intent.putExtra(SearchRequestActivity.EXTRA_KEYWORD, keywordEditText.getText().toString());
//                    Log.i(TAG, "Place: " + place.getName() + ", :" + place.getLatLng());
                    keywordEditText.setText("");
                    keywordEditText.clearFocus();
                    startActivity(intent);
                }
                return true;
            }
        });

        fabMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Log.i(TAG, "clicked mode fab");
                /*
                Will be able to implement code below once elasticSearch is up and running
                UserManager userManager = null; // Once elasticSearch is working will replace with finding a User
                if (userManager.getUserMode() == UserMode.DRIVER) {
                    userManager.setUserMode(UserMode.RIDER);
                }
                else if (userManager.getUserMode() == UserMode.RIDER) {
                    userManager.setUserMode(UserMode.DRIVER);
                    //Will have to implement a method "FindNearbyRequests" to get requests whose source
                    // is nearby and display it on the map
                    //Intent intent = new Intent((MainActivity.this, FindNearbyRequests.class);)
                    //startActivity(intent);
                }*/
            }
        });
        fabDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.setUserMode(UserMode.DRIVER);
                keywordEditText.setVisibility(View.VISIBLE);
                fabDriver.setVisibility(View.GONE);
                fabRider.setVisibility(View.VISIBLE);
                fabMenu.close(true);

            }
        });

        fabRider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userManager.setUserMode(UserMode.RIDER);
                keywordEditText.setVisibility(View.GONE);
                fabRider.setVisibility(View.GONE);
                fabDriver.setVisibility(View.VISIBLE);
                fabMenu.close(true);
            }
        });

        fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                fabMenu.close(true);
                startActivity(intent);
            }
        });

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked settings fab");
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                fabMenu.close(true);
                startActivity(intent);
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked profile fab");
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                fabMenu.close(true);
                startActivity(intent);
            }
        });
        fabHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked history fab");
                Intent intent = new Intent(MainActivity.this, RequestHistoryActivity.class);
                fabMenu.close(true);
                startActivity(intent);
            }
        });

        fabRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "clicked requests fab");
                Intent intent = new Intent(MainActivity.this, RequestsListActivity.class);
                fabMenu.close(true);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final MapController mapController = new MapController(mMap, context);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mapController.addPendingRequest(latLng, MainActivity.this);
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) { }

            @Override
            public void onMarkerDrag(Marker marker) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) { }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


    @Override
    public void onMarkerDragStart(Marker marker) { }

    @Override
    public void onMarkerDrag(Marker marker) { }

    @Override
    public void onMarkerDragEnd(Marker marker) { }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) { }

    @Override
    public void onDirectionFailure(Throwable t) { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this, data);

            }
            else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            }
        }
        else if (resultCode == RESULT_CANCELED){
            // Do nothing
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "permissions check");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "permissions failed");
            /*
            TODO: Consider calling
               ActivityCompat#requestPermissions
            here to request the missing permissions, and then overriding
              public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults)
            to handle the case where the user grants the permission. See the documentation
            for ActivityCompat#requestPermissions for more details.
            */
            String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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
    public void onConnectionSuspended(int i) { }
}

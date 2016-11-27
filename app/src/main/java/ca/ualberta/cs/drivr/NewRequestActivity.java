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

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.math.BigDecimal;

/**
 * An activity that allows users to create a new item_request.
 */
public class NewRequestActivity extends AppCompatActivity {

    private static final String TAG = "NewRequestActivity";

    /**
     * The key used for passing a Place to this activity as an extra to an intent.
     */
    public static final String EXTRA_PLACE = "ca.ualberta.cs.driver.NewRequestActivity.EXTRA_PLACE";

    private UserManager userManager = UserManager.getInstance();

    private RequestsListController requestsListController;

    private ConcretePlace destinationPlace;
    private ConcretePlace sourcePlace;

    /**
     * This method initializes the activity by deserializing the JSON given to it to get the
     * selected destination place and shows that place on the screen. Listeners are also setup for
     * UI elements.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request);

        // Setup controllers
        requestsListController = new RequestsListController(userManager);

        // Get the intent arguments
        String destinationJson = getIntent().getStringExtra(EXTRA_PLACE);
        if(destinationJson != null) {
            Log.i(TAG, destinationJson);
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                destinationPlace = gson.fromJson(destinationJson, ConcretePlace.class);
            } catch (JsonSyntaxException ex) {
                destinationPlace = null;
            }
        }



        String pickupJson = getIntent().getStringExtra("PICK_UP");
        if(pickupJson != null) {
            Log.i(TAG, pickupJson);
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                sourcePlace = gson.fromJson(pickupJson, ConcretePlace.class);
            } catch (JsonSyntaxException ex) {
                sourcePlace = null;
            }
        }




        // Show the destination information
        updateDestinationPlace(destinationPlace);

        if(sourcePlace != null) {
            updateSourcePlace(sourcePlace);
            estimateFare(destinationPlace,sourcePlace);
        }

        // Setup listener for selecting a new destination
        final PlaceAutocompleteFragment destinationPAF =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.new_request_place_dest_autocomplete);
        destinationPAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                updateDestinationPlace(place);
                estimateFare(sourcePlace,destinationPlace);
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

        // Setup listener for selecting a new source
        final PlaceAutocompleteFragment sourcePAF =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.new_request_place_source_autocomplete);
        sourcePAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                updateSourcePlace(place);
                estimateFare(sourcePlace,destinationPlace);
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

        // Hide the source text until we put something there
        if(sourcePlace == null) {
            findViewById(R.id.new_request_place_source_name).setVisibility(View.GONE);
            findViewById(R.id.new_request_place_source_address).setVisibility(View.GONE);
        }

        // Setup listener for the create button
        final Button createButton =  (Button) findViewById(R.id.new_request_create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateRequest();
            }
        });

        // Setup listeners for changes to the fare
        final EditText fareEditText = (EditText) findViewById(R.id.new_request_fare_edit_text);
        fareEditText.addTextChangedListener(new MoneyTextWatcher(fareEditText));
    }

    /**
     * Updates the text shown on screen for the destination place.
     * @param place The new destination
     */
    private void updateDestinationPlace(Place place) {
        destinationPlace = new ConcretePlace(place);
        final TextView name = (TextView) findViewById(R.id.new_request_place_dest_name);
        name.setText(destinationPlace.getName());
        final TextView address = (TextView) findViewById(R.id.new_request_place_dest_address);
        address.setText(destinationPlace.getAddress());
    }

    /**
     * Updates the text shown on screen for the source (starting) place.
     * @param place The new source
     */
    private void updateSourcePlace(Place place) {
        sourcePlace = new ConcretePlace(place);
        final TextView name = (TextView) findViewById(R.id.new_request_place_source_name);
        name.setText(sourcePlace.getName());
        name.setVisibility(View.VISIBLE);
        final TextView address = (TextView) findViewById(R.id.new_request_place_source_address);
        address.setText(sourcePlace.getAddress());
        address.setVisibility(View.VISIBLE);
    }

    /**
     * Generates a item_request from the input data and places it in the requests list. A successful call
     * to this method will terminate the activity. An unsuccessful call to this method will display
     * a message on the screen to tell the user what went wrong.
     */
    private void generateRequest() {
        if (!placeHasCompleteInformation(sourcePlace)) {
            Toast.makeText(this, "The starting location is not set.", Toast.LENGTH_LONG).show();
            return;
        }
        if (!placeHasCompleteInformation(destinationPlace)) {
            Toast.makeText(this, "The destination location is not set.", Toast.LENGTH_LONG).show();
            return;
        }

        final EditText descriptionEditText = (EditText) findViewById(R.id.new_request_description_edit_text);
        final EditText fareEditText = (EditText) findViewById(R.id.new_request_fare_edit_text);

        final String description = descriptionEditText.getText().toString();
        final String fareString = fareEditText.getText().toString().replaceAll("[$,]", "");

        // Make the request and store it in the model
        User user = userManager.getUser();
        Request request = new Request(user, sourcePlace, destinationPlace);
        request.setDescription(description);
        request.setFareString(fareString);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        String requestString = gson.toJson(request, Request.class);
        Intent intent = new Intent(NewRequestActivity.this, RequestActivity.class);
        intent.putExtra(RequestActivity.EXTRA_REQUEST, requestString);
        // TODO startActivityForResult() confirm if user presses accept or deny
        // startActivityForResult(intent, );
        startActivity(intent);

        //userManager.getRequestsList().add(request);
        //userManager.notifyObservers();
        Log.i(TAG + "kjdfgkjdfhkhj", request.getRequestState().toString());
//        requestsListController.addRequest(request);

        finish();
    }

    /**
     * Determines if a place has enough information to be put in a item_request. To be valid, a place
     * needs to have at least either an address or a latitude/longitude location. This is so it can
     * be identified uniquely on a map.
     * @param place The pace to check
     * @return True when valid, false otherwise
     */
    private static Boolean placeHasCompleteInformation(Place place) {
        if (place == null)
            return false;
        Boolean addressIsValid = place.getAddress() != null && place.getAddress().length() > 0;
        Boolean latLngIsValid = place.getLatLng() != null;
        return addressIsValid || latLngIsValid;
    }

    /**
     * Estimate the fare between two locations and show it on screen.
     * @param dest
     * @param pickUp
     */
    private void estimateFare(Place dest, Place pickUp) {

        Location locationDest = new Location("Dest");
        locationDest.setLongitude(dest.getLatLng().longitude);
        locationDest.setLatitude(dest.getLatLng().latitude);

        Location locationStart = new Location("Start");
        locationStart.setLongitude(pickUp.getLatLng().longitude);
        locationStart.setLatitude(pickUp.getLatLng().latitude);

        float distance = locationDest.distanceTo(locationStart);
        distance = distance / 500; // m to km
        distance = distance + 4; // $3 base cost

        String cost = "$" + String.format("%.2f", distance);

        TextView fareTextView = (TextView) findViewById(R.id.new_request_fare_message);
        EditText editTextFare = (EditText) findViewById(R.id.new_request_fare_edit_text);
        String message = "The estimated cost for the ride is " + cost + " How much would you like to pay?";

        fareTextView.setText(message);
        editTextFare.setText(cost);
    }
}

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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;
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
public class SearchRequestActivity extends AppCompatActivity {

    private static final String TAG = "SearchRequestActivity";

    /**
     * The key used for passing a Place to this activity as an extra to an intent.
     */
    public static final String EXTRA_PLACE = "ca.ualberta.cs.driver.SearchRequestActivity.EXTRA_PLACE";
    public static final String EXTRA_KEYWORD = "ca.ualberta.cs.driver.SearchRequestActivity.EXTRA_KEYWORD";

    private UserManager userManager = UserManager.getInstance();

//    private RequestsListController requestsListController;

//    private ConcretePlace destinationPlace;
//    private ConcretePlace sourcePlace;
    private ConcretePlace searchPlace;
    private String fareString;

    private RangeBar priceRangeBar;
    private RangeBar pricePerRangeBar;
    private TextView priceTextView;
    private TextView pricePerTextView;
    private String minPrice = "0";
    private String maxPrice = "MAX";
    private String minPricePer = "0";
    private String maxPricePer = "MAX";
    private String keyword;

    /**
     * This method initializes the activity by deserializing the JSON given to it to get the
     * selected destination place and shows that place on the screen. Listeners are also setup for
     * UI elements.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_request);

        // Setup controllers
//        requestsListController = new RequestsListController(userManager);

        // Get the intent arguments
        String destinationJson = getIntent().getStringExtra(EXTRA_PLACE);
        if (!destinationJson.isEmpty()) {
            Log.i(TAG, destinationJson);
            try {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                searchPlace = gson.fromJson(destinationJson, ConcretePlace.class);
            }
            catch (JsonSyntaxException ex) {
                searchPlace = null;
            }

            // Show the destination information
//        updateDestinationPlace(destinationPlace);
            updateSearchPlace(searchPlace);
        }

        keyword = getIntent().getStringExtra(EXTRA_KEYWORD);
        final EditText keywordEditText = (EditText) findViewById(R.id.keyword_search_edit_text);
        if (!keyword.isEmpty()) {
//            keywordEditText.setText(keyword);
            TextView textView = (TextView) findViewById(R.id.search_keyword_name);
            textView.setText(keyword);

        }

//        keywordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!keywordEditText.getText().toString().isEmpty()) {
//                    TextView textView = (TextView) findViewById(R.id.search_keyword_name);
//                    textView.setText(keywordEditText.getText().toString());
//                }
//            }
//        });

        keywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    TextView textView = (TextView) findViewById(R.id.search_keyword_name);
                    textView.setText(keywordEditText.getText().toString());
                    keywordEditText.clearFocus();
                }
                return true;
            }
        });


        // Setup listener for selecting a new destination
        final PlaceAutocompleteFragment destinationPAF =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.search_place_autocomplete);
        destinationPAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                updateSearchPlace(place);
//                estimateFare(sourcePlace,destinationPlace);
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

        priceRangeBar = (RangeBar) findViewById(R.id.price_rangebar);
        pricePerRangeBar = (RangeBar) findViewById(R.id.priceper_rangebar);


        priceTextView = (TextView) findViewById(R.id.price_range_text);
        priceTextView.setText("Find Requests between $" + minPrice + " and $" +maxPrice);
        priceRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                minPrice = leftPinValue;
                maxPrice = rightPinValue;
//                TextView textView = (TextView) findViewById(R.id.price_range_text);
                priceTextView.setText("Find Requests between $" + minPrice + " and $" +maxPrice);
            }
        });

        pricePerTextView = (TextView) findViewById(R.id.priceper_range_text);
        pricePerTextView.setText("Find Requests between $" + minPricePer + " and $" + maxPricePer + " per KM");
        pricePerRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                minPricePer = leftPinValue;
                maxPricePer = rightPinValue;
//                TextView textView = (TextView) findViewById(R.id.priceper_range_text);
                pricePerTextView.setText("Find Requests between $" + minPricePer + " and $" + maxPricePer + " per KM");
            }
        });

//        // Setup listener for selecting a new source
//        final PlaceAutocompleteFragment sourcePAF =
//                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.new_request_place_source_autocomplete);
//        sourcePAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                updateSourcePlace(place);
//                estimateFare(sourcePlace,destinationPlace);
//            }
//
//            @Override
//            public void onError(Status status) {
//                // Do nothing
//            }
//        });

//        // Hide the source text until we put something there
//        findViewById(R.id.new_request_place_source_name).setVisibility(View.GONE);
//        findViewById(R.id.new_request_place_source_address).setVisibility(View.GONE);

        // Setup listener for the create button
        final Button createButton =  (Button) findViewById(R.id.request_search_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                generateRequest();
                searchRequest();
            }
        });

//        // Setup listeners for changes to the fare
//        final EditText fareEditText = (EditText) findViewById(R.id.new_request_fare_edit_text);
//        fareEditText.addTextChangedListener(new MoneyTextWatcher(fareEditText));
    }

    /**
     * Updates the text shown on screen for the destination place.
     * @param place The new destination
     */
    private void updateSearchPlace(Place place) {
        searchPlace = new ConcretePlace(place);
        final TextView name = (TextView) findViewById(R.id.search_place_name);
        name.setText(searchPlace.getName());
        final TextView address = (TextView) findViewById(R.id.search_place_address);
        address.setText(searchPlace.getAddress());
    }

//    /**
//     * Updates the text shown on screen for the destination place.
//     * @param place The new destination
//     */
//    private void updateDestinationPlace(Place place) {
//        destinationPlace = new ConcretePlace(place);
//        final TextView name = (TextView) findViewById(R.id.search_place_name);
//        name.setText(destinationPlace.getName());
//        final TextView address = (TextView) findViewById(R.id.new_request_place_dest_address);
//        address.setText(destinationPlace.getAddress());
//    }

//    /**
//     * Updates the text shown on screen for the source (starting) place.
//     * @param place The new source
//     */
//    private void updateSourcePlace(Place place) {
//        sourcePlace = new ConcretePlace(place);
//        final TextView name = (TextView) findViewById(R.id.new_request_place_source_name);
//        name.setText(sourcePlace.getName());
//        name.setVisibility(View.VISIBLE);
//        final TextView address = (TextView) findViewById(R.id.new_request_place_source_address);
//        address.setText(sourcePlace.getAddress());
//        address.setVisibility(View.VISIBLE);
//    }

    private void searchRequest() {
//        Log.i(TAG, minPricePer);
        SearchRequest searchRequest = new SearchRequest(minPrice, maxPrice, minPricePer, maxPricePer, searchPlace, keyword);


//        ConcretePlace concretePlace = new ConcreteP;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
//        Intent intent = new Intent(MainActivity.this, NewRequestActivity.class);
        String searchRequestJson = gson.toJson(searchRequest);

        SearchRequest newSearchRequest = gson.fromJson(searchRequestJson, SearchRequest.class);
        Intent intent = new Intent(SearchRequestActivity.this, SearchRequestsListActivity.class);
        intent.putExtra(SearchRequestsListActivity.EXTRA_SEARCH_REQUEST, searchRequestJson);
//            Log.i(TAG, "Place: " + place.getName() + ", :" + place.getLatLng());
        startActivity(intent);

        finish();
    }

//    /**
//     * Generates a item_request from the input data and places it in the requests list. A successful call
//     * to this method will terminate the activity. An unsuccessful call to this method will display
//     * a message on the screen to tell the user what went wrong.
//     */
//    private void generateRequest() {
//        if (!placeHasCompleteInformation(sourcePlace)) {
//            Toast.makeText(this, "The starting location is not set.", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!placeHasCompleteInformation(destinationPlace)) {
//            Toast.makeText(this, "The destination location is not set.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        final EditText fareEditText = (EditText) findViewById(R.id.new_request_fare_edit_text);
//        final String fareString = fareEditText.getText().toString().replaceAll("[$,.]", "");
//        final BigDecimal fare = new BigDecimal(fareString).setScale(2, BigDecimal.ROUND_FLOOR)
//                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR); // Divide by 100 to get dollars from cents
//
//        // Make the request and store it in the model
//        User user = userManager.getUser();
//        Request request = new Request(user, sourcePlace, destinationPlace);
//        request.setFare(fare);
//        request.setFareString(fareString);
//
//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Uri.class, new UriSerializer())
//                .create();
//        String requestString = gson.toJson(request, Request.class);
//        Intent intent = new Intent(SearchRequestActivity.this, RequestActivity.class);
//        intent.putExtra(RequestActivity.EXTRA_REQUEST, requestString);
//        // TODO startActivityForResult() confirm if user presses accept or deny
//        // startActivityForResult(intent, );
//         startActivity(intent);
//
//        //userManager.getRequestsList().add(request);
//        //userManager.notifyObservers();
//        requestsListController.addRequest(request);
//
//        finish();
//    }

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

//    /**
//     * Estimate the fare between two locations and show it on screen.
//     * @param dest
//     * @param pickUp
//     */
//    private void estimateFare(Place dest, Place pickUp) {
//
//        Location locationDest = new Location("Dest");
//        locationDest.setLongitude(dest.getLatLng().longitude);
//        locationDest.setLatitude(dest.getLatLng().latitude);
//
//        Location locationStart = new Location("Start");
//        locationStart.setLongitude(pickUp.getLatLng().longitude);
//        locationStart.setLatitude(pickUp.getLatLng().latitude);
//
//        float distance = locationDest.distanceTo(locationStart);
//        distance = distance / 1000; // m to km
//        distance = distance + 3; // $3 base cost
//
//        String cost = "$" + String.format("%.2f", distance);
//        fareString = cost;
//
//        TextView fareTextView = (TextView) findViewById(R.id.new_request_fare_message);
//        EditText editTextFare = (EditText) findViewById(R.id.new_request_fare_edit_text);
//        String message = "The estimated cost for the ride is " + cost + " How much would you like to pay?";
//
//        fareTextView.setText(message);
//        editTextFare.setText(cost);
//    }
}

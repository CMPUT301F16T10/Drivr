package ca.ualberta.cs.drivr;

import android.content.Intent;
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
    public static final String EXTRA_PLACE = "ca.ualberta.cs.driver.NewRequestActivity.EXTRA_PLACE";

    private Place destinationPlace;
    private Place sourcePlace;

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

        // Get the intent arguments
        String destinationJson = getIntent().getStringExtra(EXTRA_PLACE);
        Log.i(TAG, destinationJson);
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            destinationPlace = gson.fromJson(destinationJson, ConcretePlace.class);
        }
        catch (JsonSyntaxException ex) {
            destinationPlace = null;
        }

        // Show the destination information
        updateDestinationPlace(destinationPlace);

        // Setup listener for selecting a new destination
        final PlaceAutocompleteFragment destinationPAF =
                (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.new_request_place_dest_autocomplete);
        destinationPAF.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                updateDestinationPlace(place);
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
            }

            @Override
            public void onError(Status status) {
                // Do nothing
            }
        });

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
        TextView name = (TextView) findViewById(R.id.new_request_place_dest_name);
        name.setText(destinationPlace.getName());
        TextView address = (TextView) findViewById(R.id.new_request_place_dest_address);
        address.setText(destinationPlace.getAddress());
    }

    /**
     * Updates the text shown on screen for the source (starting) place.
     * @param place The new source
     */
    private void updateSourcePlace(Place place) {
        sourcePlace = new ConcretePlace(place);
        TextView name = (TextView) findViewById(R.id.new_request_place_source_name);
        name.setText(sourcePlace.getName());
        TextView address = (TextView) findViewById(R.id.new_request_place_source_address);
        address.setText(sourcePlace.getAddress());
    }

    /**
     * Gerenates a item_request from the input data and places it in the requests list. A successful call
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

        final EditText fareEditText = (EditText) findViewById(R.id.new_request_fare_edit_text);
        final String fareString = fareEditText.getText().toString().replaceAll("[$,.]", "");
        final BigDecimal fare = new BigDecimal(fareString).setScale(2, BigDecimal.ROUND_FLOOR)
                .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR); // Divide by 100 to get dollars from cents

        // Make the item_request
        Request request = new Request(null, sourcePlace, destinationPlace);
        request.setFare(fare);
        // TODO: Store the item_request

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
}

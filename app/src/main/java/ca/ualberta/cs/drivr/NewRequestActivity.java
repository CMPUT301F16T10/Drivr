package ca.ualberta.cs.drivr;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class NewRequestActivity extends AppCompatActivity {

    private static final String TAG = "NewRequestActivity";
    public static final String EXTRA_PLACE = "ca.ualberta.cs.driver.NewRequestActivity.EXTRA_PLACE";

    private Place destinationPlace;
    private Place sourcePlace;

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
}

package ca.ualberta.cs.drivr;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class NewRequestActivity extends AppCompatActivity {

    private static final String TAG = "NewRequestActivity";
    public static final String EXTRA_PLACE = "ca.ualberta.cs.driver.NewRequestActivity.EXTRA_PLACE";

    private Place destinationPlace;

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
        if (destinationPlace != null) {
            TextView textView = (TextView) findViewById(R.id.new_request_temp_text);
            textView.setText(
                    "Name: " + destinationPlace.getName() + "\n"
                            + "Address: " + destinationPlace.getAddress() + "\n"
                            + "Latitude: " + destinationPlace.getLatLng().latitude + "\n"
                            + "Longitude: " + destinationPlace.getLatLng().longitude + "\n"
            );
        }
    }
}

package ca.ualberta.cs.drivr;

import android.location.Address;
import android.location.Geocoder;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.robotium.solo.Solo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by justin on 28/11/16.
 */
public class NewRequestActivityTest extends ActivityInstrumentationTestCase2<NewRequestActivity> {
    private Solo solo;
    private Geocoder geocoder;


    public NewRequestActivityTest() {
        super(ca.ualberta.cs.drivr.NewRequestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }



}

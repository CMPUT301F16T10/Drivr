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

    public void testGeoLocationSearch(){
        solo.assertCurrentActivity("Expected NewRequestActivity", NewRequestActivity.class);



            /*LatLng pickupLatLng = new LatLng(53.5232,113.5263);
            LatLng destinationLatLng = new LatLng(53.5232,114.5263);

            Locale locale = new Locale("en","CA");

            Address pickupAddress = new Address(locale);
            pickupAddress.setLongitude(113.5263);
            pickupAddress.setLatitude(53.5232);

            Address destinationAddress =  new Address(locale);
            destinationAddress.setLongitude(114.5263);
             destinationAddress.setLatitude(53.5232);

            ConcretePlace sourcePlace = new ConcretePlace(pickupAddress);
            ConcretePlace destinationPlace = new ConcretePlace(destinationAddress); */


            solo.clickOnButton("CREATE");





    }
}

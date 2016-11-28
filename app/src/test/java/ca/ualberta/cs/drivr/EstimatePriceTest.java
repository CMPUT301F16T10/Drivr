package ca.ualberta.cs.drivr;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.places.Place;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Locale;

/**
 * Created by justin on 28/11/16.
 */
public class EstimatePriceTest {




    @Before
    public void setup() {

    }

    /**
     *
     * UC 6 EstimateFare
     */
    @Test
    public void EstimatePrice(){

        EstimatedPriceCalculator estimatedPriceCalculator = new EstimatedPriceCalculator();

        Locale locale = new Locale("en","CA");


        Address pickupAddress = new Address(locale);
        pickupAddress.setLongitude(113.5263);
        pickupAddress.setLatitude(53.5232);

        Address destinationAddress =  new Address(locale);
        destinationAddress.setLongitude(113.5118);
        destinationAddress.setLatitude(53.5227);

        ConcretePlace sourcePlace = new ConcretePlace(pickupAddress);
        ConcretePlace destinationPlace = new ConcretePlace(destinationAddress);

        float price = estimatedPriceCalculator.estimateFare(sourcePlace,destinationPlace);

        // Check if the price is reasonable
        assertTrue("Reasonable Distance", price < 5.00);
    }


}

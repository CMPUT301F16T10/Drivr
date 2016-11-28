package ca.ualberta.cs.drivr;

import android.location.Location;

import com.google.android.gms.location.places.Place;

/**
 * Created by justin on 28/11/16.
 */
public class EstimatedPriceCalculator {

    public EstimatedPriceCalculator() {
    }

    public float estimateFare(Place dest, Place pickUp) {

        Location locationDest = new Location("Dest");
        locationDest.setLongitude(dest.getLatLng().longitude);
        locationDest.setLatitude(dest.getLatLng().latitude);

        Location locationStart = new Location("Start");
        locationStart.setLongitude(pickUp.getLatLng().longitude);
        locationStart.setLatitude(pickUp.getLatLng().latitude);

        float distance = locationDest.distanceTo(locationStart);
        distance = distance / 500; // m to km
        distance = distance + 4; // $3 base cost

        return distance;
    }

}

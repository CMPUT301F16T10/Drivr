package ca.ualberta.cs.drivr;

import android.location.Location;

import com.google.android.gms.location.places.Place;

/**
 *
 * Calcuate a Fair price between two distances
 */
public class EstimatedPriceCalculator {

    public EstimatedPriceCalculator() {}

    /**
     *
     * @param dest
     * @param pickUp
     * Return the cost of trip
     * @return cost
     */
    public float estimateFare(Place dest, Place pickUp) {
        Location locationDest = new Location("Dest");
        locationDest.setLongitude(dest.getLatLng().longitude);
        locationDest.setLatitude(dest.getLatLng().latitude);

        Location locationStart = new Location("Start");
        locationStart.setLongitude(pickUp.getLatLng().longitude);
        locationStart.setLatitude(pickUp.getLatLng().latitude);

        float distance = locationDest.distanceTo(locationStart);
        float cost = distance / 500; // m to km
        cost = cost + 4; // $3 base cost

        return cost;
    }

}

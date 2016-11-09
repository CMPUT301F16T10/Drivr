package ca.ualberta.cs.drivr;

import android.net.Uri;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

/**
 * A class to get around the fact that com.google.android.gms.location.places.Place is an interface.
 * This allows it to be seriealized.
 */

public class ConcretePlace implements Place {

    private String address = null;
    private String attributions = null;
    private String name = null;
    private String phoneNumber = null;
    private float rating = 0;
    private int priceLevel = 0;
    private LatLng latLng = null;
    private LatLngBounds viewport = null;
    private List<Integer> placeTypes = null;
    private Locale locale = null;
    private String id = null;
    private Uri websiteUri = null;

    public ConcretePlace(Place place) {
        if (place != null) {
            address = place.getAddress() == null ? null : place.getAddress().toString();
            attributions = place.getAttributions() == null ? null : place.getAttributions().toString();
            name = place.getName() == null ? null : place.getName().toString();
            phoneNumber = place.getPhoneNumber() == null ? null : place.getPhoneNumber().toString();
            rating = place.getRating();
            priceLevel = place.getPriceLevel();
            latLng = place.getLatLng();
            viewport = place.getViewport();
            placeTypes = place.getPlaceTypes();
            locale = place.getLocale();
            id = place.getId();
            websiteUri = place.getWebsiteUri();
        }
    }

    @Override
    public CharSequence getAddress() {
        return address;
    }

    @Override
    public CharSequence getAttributions() {
        return attributions;
    }

    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public CharSequence getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public float getRating() {
        return rating;
    }

    @Override
    public int getPriceLevel() {
        return priceLevel;
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public LatLngBounds getViewport() {
        return viewport;
    }

    @Override
    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Uri getWebsiteUri() {
        return websiteUri;
    }

    @Override
    public boolean isDataValid() {
        return true;
    }

    @Override
    public Place freeze() {
        return this;
    }
}

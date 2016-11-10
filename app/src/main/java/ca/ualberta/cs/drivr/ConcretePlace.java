package ca.ualberta.cs.drivr;

import android.net.Uri;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

/**
 * A class to get around the fact Place is an interface. This allows it to be serialized.
 *
 * @see Place
 */
public class ConcretePlace implements Place {

    private String address;
    private String attributions;
    private String name;
    private String phoneNumber;
    private float rating;
    private int priceLevel;
    private LatLng latLng;
    private LatLngBounds viewport;
    private List<Integer> placeTypes;
    private Locale locale;
    private String id;
    private Uri websiteUri;

    /**
     * Instantiates a new ConcretePlace.
     */
    public ConcretePlace() {
        address = null;
        attributions = null;
        name = null;
        phoneNumber = null;
        rating = 0;
        priceLevel = 0;
        latLng = null;
        viewport = null;
        placeTypes = null;
        locale = null;
        id = null;
        websiteUri = null;
    }

    /**
     * Instantiates a new ConcretePlace from an existing Place.
     * @param place The place to copy.
     * @see Place
     */
    public ConcretePlace(Place place) {
        this();
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

    /**
     * Get the address of a place.
     * @return The place's address.
     */
    @Override
    public CharSequence getAddress() {
        return address;
    }

    /**
     * Get the attributions to be shown for a place.
     * @return The place's attributions.
     */
    @Override
    public CharSequence getAttributions() {
        return attributions;
    }

    /**
     * Get the name of a place.
     * @return The place's name.
     */
    @Override
    public CharSequence getName() {
        return name;
    }

    /**
     * Get the phone number of a place.
     * @return The place's phone number.
     */
    @Override
    public CharSequence getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Get the rating of a place.
     * @return The place's rating.
     */
    @Override
    public float getRating() {
        return rating;
    }

    /**
     * Get the price level of a place.
     * @return The place's price level.
     */
    @Override
    public int getPriceLevel() {
        return priceLevel;
    }

    /**
     * Get the location of a place as a latitude and longitude.
     * @return The place's location.
     */
    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     * Get the viewport bounds used to display this place on a map.
     * @return The place's viewport bounds.
     */
    @Override
    public LatLngBounds getViewport() {
        return viewport;
    }

    /**
     * Get the types of places that this place is tagged as.
     * @return The place's types.
     */
    @Override
    public List<Integer> getPlaceTypes() {
        return placeTypes;
    }

    /**
     * Get the locale used for te place data.
     * @return The place's information locale.
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * Get a unique ID of a place.
     * @return The place's ID.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Get the website of a place.
     * @return The place's website.
     */
    @Override
    public Uri getWebsiteUri() {
        return websiteUri;
    }

    /**
     * Used to check whether the data in the place is valid.
     * @return Is always true for this object.
     */
    @Override
    public boolean isDataValid() {
        return true;
    }

    /**
     * Create an immutable copy of the object.
     * @return A copy of the object.
     */
    @Override
    public Place freeze() {
        return this;
    }
}

/*
 * Copyright 2016 CMPUT301F16T10
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

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

    public void setAddress(String address) {
        this.address = address;
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
     * Set the attributions to be shown for a place.
     * @param attributions The attributions.
     */
    public void setAttributions(String attributions) {
        this.attributions = attributions;
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
     * Set the name of a place.
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
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
     * Set the phone number of a place.
     * @param phoneNumber The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * Set the rating of a place.
     * @param rating The rating.
     */
    public void setRating(float rating) {
        this.rating = rating;
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
     * Set the price level of a place.
     * @param priceLevel The price level.
     */
    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
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
     * Set the location of a place as a latitude and longitude.
     * @param latLng The location being set.
     */
    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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
     * Set the viewport bounds used to display this place on a map.
     * @param viewport The bounds.
     */
    public void setViewport(LatLngBounds viewport) {
        this.viewport = viewport;
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
     * Set the types of places that this place is tagged as.
     * @param placeTypes The place's types.
     */
    public void setPlaceTypes(List<Integer> placeTypes) {
        this.placeTypes = placeTypes;
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
     * Set the locale used for te place data.
     * @param locale  The place's information locale.
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
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
     * Set a unique ID of a place.
     * @param id The place's ID.
     */
    public void setId(String id) {
        this.id = id;
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
     * Set the website of a place.
     * @param websiteUri The place's website.
     */
    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
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

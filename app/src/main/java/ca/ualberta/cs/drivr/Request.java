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

import com.google.android.gms.location.places.Place;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class for storing information about a item_request.
 */
public class Request {

    private User rider;
    private ArrayList<Driver> driver;
    private String description;
    private Date date;
    private BigDecimal fare;
    private RequestState requestState;
    private Place source;
    private Place destination;
    private Boolean synced;
    private String id;

    /**
     * Instantiates a new item_request.
     */
    public Request() {
        rider = null;
        driver = null;
        description = "";
        setDate(new Date());
        setFare(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
        requestState = RequestState.PENDING;
        source = new ConcretePlace();
        destination = new ConcretePlace();
        synced = false;
        id = "";
    }

    /**
     * Instantiates a new item_request.
     * @param rider The rider for the item_request.
     * @param source The starting location.
     * @param destination The edning location.
     */
    public Request(User rider, Place source, Place destination) {
        this();
        this.rider = rider;
        this.setDate(new Date());
        this.source = source;
        this.destination = destination;
    }

    /**
     * Get the starting location.
     * @return The starting location.
     */
    public Place getSourcePlace() {
        return source;
    }

    /**
     * Set the source location.
     * @param source
     */
    public void setSourcePlace(Place source) {
        this.source = source;
    }

    /**
     * Get the destination location.
     * @return
     */
    public Place getDestinationPlace() {
        return destination;
    }

    /**
     * Set the ending place.
     * @param destination The destination location.
     */
    public void setDestinationPlace(Place destination) {
        this.destination = destination;
    }

    /**
     * Get the driver for the item_request.
     * @return The driver.
     */
    public ArrayList<Driver> getDriver() {
        return driver;
    }

    /**
     * Set the driver.
     * @param driver The driver.
     */
    public void setDriver(ArrayList<Driver> driver) {
        this.driver = driver;
    }

    /**
     * Get the date the item_request was made.
     * @return The date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date the item_request was made.
     * @param date The date.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * The cost for the ride in dollars.
     * @return The fare for the ride.
     */
    public BigDecimal getFare() {
        return fare;
    }

    /**
     * Set the fare for the ride in dollars.
     * @param fare The fare for the ride.
     */
    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    /**
     * Get the item_request sate.
     * @return The item_request state.
     */
    public RequestState getRequestState() {
        return requestState;
    }

    /**
     * Set the item_request state.
     * @param requestState The item_request state.
     */
    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    /**
     * Get the rider for the item_request.
     * @return The rider.
     */
    public User getRider() {
        return rider;
    }

    /**
     * Set the rider for the item_request.
     * @param rider The rider.
     */
    public void setRider(User rider) {
        this.rider = rider;
    }

    /**
     * Get whether the item_request is synced online or not.
     * @return True when synced, false otherwise.
     */
    public Boolean getSynced() {
        return synced;
    }

    /**
     * Set whther the item_request is synced online or not.
     * @param synced True when synced, false otherwise.
     */
    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

    /**
     * Get the description.
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description.
     * @param description The description.
     */
    public void setDescription(String description) {
        this.description= description;
    }

    /**
     * Get the ID.
     * @return The ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID.
     * @param id The ID.
     */
    public void setId(String id) {
        this.id = id;
    }
}

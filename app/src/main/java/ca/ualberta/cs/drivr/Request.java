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

import android.location.Address;

import com.google.android.gms.location.places.Place;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class for storing information about a item_request.
 */
public class Request {

    private User rider;
    private DriversList drivers;
    private String description;
    private Date date;
    private BigDecimal fare;
    private RequestState requestState;
    private ConcretePlace source;
    private ConcretePlace destination;
    private Boolean synced;
    private String id;
    private double km;

    /**
     * Instantiates a new item_request.
     */
    public Request() {
        rider = null;
        drivers = new DriversList();
        description = "";
        setDate(new Date());
        setFare(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
        requestState = RequestState.CREATED;
        source = new ConcretePlace();
        destination = new ConcretePlace();
        synced = false;
        id = "";
        km = 1;
    }


    /**
     * Instantiates a new item_request.
     * @param rider The rider for the item_request.
     * @param source The starting location.
     * @param destination The edning location.
     */
    public Request(User rider, ConcretePlace source, ConcretePlace destination) {
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
    public void setSourcePlace(ConcretePlace source) {
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
    public void setDestinationPlace(ConcretePlace destination) {
        this.destination = destination;
    }

    /**
     * Gets a description of the route to be taken.
     * @return A string describing the route
     */
    public String getRoute() {
        final String sourceName =
                source.getName() != null ? source.getName().toString() :
                        source.getAddress() != null ? source.getAddress().toString() :
                                "Unknown Address";
        final String destinationName =
                destination.getName() != null ? destination.getName().toString() :
                        destination.getAddress() != null ? destination.getAddress().toString() :
                                "Unknown Address";
        return "Going from " + sourceName + " to " + destinationName;
    }

    /**
     * Set the possible drivers for the request.
     * @param drivers The possible drivers.
     */
    public void setDrivers(DriversList drivers) {
        this.drivers = drivers;
    }

    /**
     * Get the possible drivers for the request.
     * @return The possible drivers.
     */
    public DriversList getDrivers() {
        return drivers;
    }

    /**
     * Adds a driver to the list of possible drivers.
     * @param driver The driver.
     */
    public void addDriver(Driver driver) {
        if (!drivers.contains(driver))
            drivers.add(driver);
    }

    /**
     * Removes a driver from the list of possible drivers.
     * @param driver The driver.
     */
    public void removeDriver(Driver driver) {
        drivers.remove(driver);
    }

    /**
     * Gets a list of all drivers for the request with a given status.
     * @param requestState The state of the driver.
     * @return A list of drivers.
     */
    public ArrayList<Driver> getDriversWithStatus(RequestState requestState) {
        ArrayList<Driver> driversWithStatus = new ArrayList<>();
        for (Driver driver : drivers)
            if (driver.getStatus() == requestState)
                driversWithStatus.add(driver);
        return driversWithStatus;
    }

    /**
     * Determine whether there is a confirmed driver for the request.
     * @return True when there is a confirmed driver, false otherwise.
     */
    public boolean hasConfirmedDriver() {
        return getDriversWithStatus(RequestState.CONFIRMED).size() > 0;
    }

    /**
     * Get the date the request was made.
     * @return The date.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set the date the request was made.
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
     * @return The request state.
     */
    public RequestState getRequestState() {
        return requestState;
    }

    /**
     * Set the request state.
     * @param requestState The request state.
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
     * Set the rider for the request.
     * @param rider The rider.
     */
    public void setRider(User rider) {
        this.rider = rider;
    }

    /**
     * Get whether the request is synced online or not.
     * @return True when synced, false otherwise.
     */
    public Boolean getSynced() {
        return synced;
    }

    /**
     * Set whether the request is synced online or not.
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

    /**
     * Get the kilometers of the request.
     * @return The kilometers of the request.
     */
    public double getKm() {
        return km;
    }

    /**
     * Set the kilometers of the request.
     * @param km The kilometers of the request.
     */
    public void setKm(double km) {
        this.km = km;
    }

    /**
     * Gets the fare as a properly formatted string (ie. DDDD.cc where D is dollars and c is cents).
     * @return The fare
     */
    public String getFareString() { return String.format("%.2f", fare); }

    /**
     * Expects a numeric string in the form DDDD.cc where D is dollars and c is cents.
     * @param fareString
     */
    public void setFareString(String fareString) {
        try {
            fare = new BigDecimal(fareString).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        catch (NumberFormatException e) {
            fare = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }
}

/*
 *  Copyright 2016 CMPUT301F16T10
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ca.ualberta.cs.drivr;

import com.google.android.gms.location.places.Place;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * A class for storing information about a request.
 */
public class Request {

    private User rider;
    private User driver;
    private Date date;
    private BigDecimal fare;
    private RequestState requestState;
    private Place source;
    private Place destination;
    private String requestId;
    private Boolean synced;

    /**
     * Instantiates a new request.
     */
    public Request() {
        rider = null;
        driver = null;
        setDate(new Date());
        setFare(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
        requestState = null;
        source = null;
        destination = null;
        requestId = UUID.randomUUID().toString();
        synced = false;
    }

    /**
     * Instantiates a new request.
     * @param rider The rider for the request.
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
     * Get the driver for the request.
     * @return The driver.
     */
    public User getDriver() {
        return driver;
    }

    /**
     * Set the driver.
     * @param driver The driver.
     */
    public void setDriver(User driver) {
        this.driver = driver;
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
     * Get the request sate.
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
     * Get the rider for the request.
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
     * Get the unique request ID.
     * @return The request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Set the unique request ID.
     * @param requestId The request ID.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Get whether the request is synced online or not.
     * @return True when synced, false otherwise.
     */
    public Boolean getSynced() {
        return synced;
    }

    /**
     * Set whther the request is synced online or not.
     * @param synced True when synced, false otherwise.
     */
    public void setSynced(Boolean synced) {
        this.synced = synced;
    }

}

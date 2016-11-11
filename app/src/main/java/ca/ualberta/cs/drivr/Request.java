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

import android.location.Location;

import java.util.Currency;
import java.util.Date;

/**
 * Created by adam on 2016-10-12.
 */

public class Request {

    private PublicUserInfo rider;
    private PublicUserInfo driver;
    private Date date;
    private Currency cost;
    private RequestState requestState;
    private Location source;
    private Location destination;
    private String requestId;
    private String synced;

    public Request() { throw new UnsupportedOperationException(); }

    public Request(User rider, Location source, Location destination) { throw new UnsupportedOperationException(); }

    public Request(User rider, User driver, Location source, Location destination) { throw new UnsupportedOperationException(); }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public PublicUserInfo getDriver() {
        return driver;
    }

    public void setDriver(PublicUserInfo driver) {
        this.driver = driver;
    }

    public RequestState getRequestState() {
        return requestState;
    }

    public void setRequestState(RequestState requestState) {
        this.requestState = requestState;
    }

    public PublicUserInfo getRider() {
        return rider;
    }

    public void setRider(PublicUserInfo rider) {
        this.rider = rider;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSynced() {
        return synced;
    }

    public void setSynced(String synced) {
        this.synced = synced;
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            * Set the rider for the item_request.
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

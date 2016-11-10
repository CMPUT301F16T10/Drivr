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
 * Created by adam on 2016-10-12.
 */

public class Request {

    private PublicUserInfo rider;
    private PublicUserInfo driver;
    private Date date;
    private BigDecimal fare;
    private RequestState requestState;
    private Place source;
    private Place destination;
    private String requestId;
    private String synced;

    public Request() {
        rider = null;
        driver = null;
        setDate(new Date());
        setFare(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP));
        requestState = null;
        source = null;
        destination = null;
        requestId = UUID.randomUUID().toString();
        synced = "Not synced";
    }

    public Request(PublicUserInfo riderInfo, Place source, Place destination) {
        this();
        this.rider = riderInfo;
        this.setDate(new Date());
        this.source = source;
        this.destination = destination;
    }

    public Place getSource() {
        return source;
    }

    public void setSource(Place source) {
        this.source = source;
    }

    public Place getDestination() {
        return destination;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    public PublicUserInfo getDriver() { return driver; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
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

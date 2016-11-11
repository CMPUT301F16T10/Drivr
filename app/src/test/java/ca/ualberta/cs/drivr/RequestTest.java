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

import android.location.Location;

import org.junit.Test;
import org.xmlpull.v1.sax2.Driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestTest {

    @Test
    public void getDriver() {
        User driver = new User();
        Request request = new Request(null, null, null);
        request.setDriver(driver);
        assertEquals(driver, request.getDriver());
    }

    @Test
    public void getRider() {
        User rider = new User();
        Request request = new Request(rider, null, null);
        assertEquals(rider, request.getRider());
    }

    @Test
    public void driverAcceptedRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.ACCEPTED);
        assertEquals(RequestState.ACCEPTED, request.getRequestState());
    }

    /**
     * UC 1 Rider Specifies Request Locations
     * US 01.01.01 As a Rider, I want to item_request rides between two locations.
     */
    @Test
    public void riderSpecifyLocation() {
        User rider = new User();
        ConcretePlace source = new ConcretePlace();
        ConcretePlace destination = new ConcretePlace();
        Request request = new Request(rider, source,destination);
        assertEquals(source, request.getSourcePlace());
        assertEquals(destination, request.getDestinationPlace());
    }

    /**
     * UC 24 Driver View Locations
     * US 10.02.01 As a Driver, I want to view start and end Geo-locations on a map for a Request.
     */
    @Test
    public void getRiderSourceAndDestination() {
        User rider = new User();
        ConcretePlace source = new ConcretePlace();
        ConcretePlace destination = new ConcretePlace();
        Request request = new Request(rider, source, destination);
        assertEquals(source, request.getSourcePlace());
        assertEquals(destination, request.getDestinationPlace());
    }

}
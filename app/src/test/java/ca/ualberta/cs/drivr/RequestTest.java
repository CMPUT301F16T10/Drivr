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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestTest {

    @Test
    public void getDrivers() {
        DriversList driver = new DriversList();
        Request request = new Request();
        request.setDrivers(driver);
        assertEquals(driver, request.getDrivers());
    }

    @Test
    public void getRider() {
        User rider = new User();
        Request request = new Request();
        request.setRider(rider);
        assertEquals(rider, request.getRider());
    }

    @Test
    public void addDriver() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        Request request = new Request();

        assertFalse(request.getDrivers().contains(driver1));
        request.addDriver(driver1);
        assertTrue(request.getDrivers().contains(driver1));

        assertFalse(request.getDrivers().contains(driver2));
        request.addDriver(driver2);
        assertTrue(request.getDrivers().contains(driver2));
    }

    @Test
    public void removeDriver() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        Request request = new Request();

        request.addDriver(driver1);
        request.addDriver(driver2);
        assertTrue(request.getDrivers().contains(driver1));
        request.removeDriver(driver2);
        assertFalse(request.getDrivers().contains(driver2));
    }

    @Test
    public void getDriversWithStatus() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        Driver driver3 = new Driver();
        driver1.setStatus(RequestState.ACCEPTED);
        driver2.setStatus(RequestState.PENDING);
        driver3.setStatus(RequestState.ACCEPTED);

        DriversList drivers = new DriversList();
        drivers.add(driver1);
        drivers.add(driver2);
        drivers.add(driver3);

        Request request = new Request();
        request.setDrivers(drivers);

        assertEquals(1, request.getDriversWithStatus(RequestState.PENDING).size());
        assertEquals(2, request.getDriversWithStatus(RequestState.ACCEPTED).size());
    }

    @Test
    public void hasConfirmedDriver() {
        Driver driver1 = new Driver();
        Driver driver2 = new Driver();
        Driver driver3 = new Driver();
        driver1.setStatus(RequestState.ACCEPTED);
        driver2.setStatus(RequestState.PENDING);
        driver3.setStatus(RequestState.ACCEPTED);

        Request request = new Request();
        request.addDriver(driver1);
        request.addDriver(driver2);
        request.addDriver(driver3);

        assertFalse(request.hasConfirmedDriver());

        Driver driver4 = new Driver();
        driver4.setStatus(RequestState.CONFIRMED);
        request.addDriver(driver4);

        assertTrue(request.hasConfirmedDriver());
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
        Request request = new Request(rider, source, destination);
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

    @Test
    public void getAndSetFareString() {
        Request request = new Request();
        assertEquals("0.00", request.getFareString());
        request.setFareString("12.34");
        assertEquals("12.34", request.getFareString());
    }

}
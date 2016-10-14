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

import org.junit.Test;
import org.xmlpull.v1.sax2.Driver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestTest {


    // Test a Request can be created with a rider and locations
    @Test
    public void createRequestRider() throws Exception{
        User rider = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");

        Request request = new Request(rider ,source, destination);

        assertTrue(new Request(rider,source,destination) instanceof Request);
    }


    // Test a Request can be created with a driver, rider, and locations
    @Test
    public void createRequestRiderwithDriver() throws Exception{
        User rider = new User();
        User driver = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");


        assertTrue(new Request(rider,driver,source,destination) instanceof Request);
    }


    // Test a Request returns a Driver
    @Test
    public void returnDriver(){
        User rider = new User();
        User driver = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");

        Request request = new Request(rider,driver,source,destination);
        assertEquals(request.getDriver(), driver);
    }

    // Test a Request returns a Rider
    @Test
    public void returnRider(){
        User rider = new User();
        User driver = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");

        Request request = new Request(rider,driver,source,destination);
        assertEquals(request.getRider(), rider);
    }


    // Test the Request can be Accepted
    @Test
    public void driverAcceptedRequest(){
        User rider = new User();
        User driver = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");

        Request request = new Request(rider,driver,source,destination);
        request.setRequestState(RequestState.ACCEPTED);
        assertEquals(request.getRequestState(), RequestState.ACCEPTED);

    }


    // Test All Accepted Requests have drivers
    @Test
    public void driverExistsAcceptedRequest(){
        User rider = new User();
        User driver = new User();
        Location source = new Location("street123");
        Location destination = new Location("street124");

        Request request = new Request(rider,driver,source,destination);
        request.setRequestState(RequestState.ACCEPTED);

        assertEquals(request.getDriver(),null);


    }


}
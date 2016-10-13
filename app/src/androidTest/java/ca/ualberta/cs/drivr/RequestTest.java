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

/**
 * Created by adam on 2016-10-12.
 */

public class RequestTest {
    @Test
    public void thisAlwaysPasses() {
        assertEquals(12, 4 * 3);
    }




    @Test
    public void testRequestDestinationLocationExists(){
        Location location = new Location("City");
        Request request = new Request();
        request.setDestination(location);
        assert (false);

    }

    @Test
    public void testRequestSourceLocationExists(){
        Location location = new Location("City");
        Request request = new Request();
        request.setSource(location);
        assert (false);

    }


    @Test
    public void testRequestCanceled(){
        Request request = new Request();

    }

    @Test
    public void testRequestSingleDriver(){
        User driver = new User();
        User driver2 = new User();
        Request request = new Request();
        request.setDriver(driver);
        request.setDriver(driver2);
        assert(false);

    }


    @Test
    public void testRequestSingleRider(){
        User rider= new User();
        User rider2 = new User();
        Request request = new Request();
        request.setDriver(rider);
        request.setDriver(rider2);
        assert(false);

    }

}

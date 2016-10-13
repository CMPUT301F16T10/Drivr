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

    private User rider;
    private User driver;
    private Date date;
    private Currency cost;
    private RequestState requestState;
    private Location source;
    private Location destination;

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

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }
}

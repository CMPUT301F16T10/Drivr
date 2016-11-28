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

/**
 * This is the object for drivers in ElasticSearch.
 *
 * @author Tiegan Bonowicz
 * @see User
 * @see DriversList
 */

public class Driver extends User {

    /**
     * The state of the driver. Needs to be a string so ElasticSearch can copy the status into
     * Driver.
     */
    private String status;

    /**
     * Constructor for Driver.
     */
    public Driver() {
        super();
        status = RequestState.PENDING.toString();
    }

    /**
     * Copy constructor.
     * @param user The object to copy.
     */
    public Driver(User user) {
        super(user);
        status = RequestState.PENDING.toString();
    }

    /**
     * Copy constructor.
     * @param other The object to copy.
     */
    public Driver(Driver other) {
        this((User) other);
        status = other.getStatus().toString();
    }

    /**
     * Returns the status of driver as a RequestState
     * @return The driver's status.
     */
    public RequestState getStatus() {
        return RequestState.valueOf(status.toUpperCase());
    }

    /**
     * Sets the status of the driver given a string.
     * @param status A status in String form.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the status of the driver given a RequestState.
     * @param status A status in RequestState form.
     */
    public void setStatus(RequestState status) {
        this.status = status.toString();
    }
}

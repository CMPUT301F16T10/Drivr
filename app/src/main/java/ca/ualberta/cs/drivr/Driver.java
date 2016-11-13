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
 * @see ElasticSearchController
 */

public class Driver extends User{
    private String username;
    private RequestState status;

    /**
     * Constructor for Driver.
     */
    public Driver() {
        username = "";
        status = RequestState.PENDING;
    }

    /**
     * Returns username of driver.
     * @return String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username of driver.
     * @param username Driver's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns status of driver.
     * @return RequestState
     */
    public RequestState getStatus() {
        return status;
    }

    /**
     * Sets status of driver.
     * @param status Driver's status.
     */
    public void setStatus(RequestState status) {
        this.status = status;
    }
}

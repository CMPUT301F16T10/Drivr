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
 * Describes what a user manager should do. This is providing access to data classes.
 */
public interface IUserManager {

    /**
     * Get the current user.
     * @return The current user.
     */
    User getUser();

    /**
     * Set the current user.
     * @param user The current user.
     */
    void setUser(User user);

    /**
     * Get the list of requests.
     * @return The requests list.
     */
    RequestsList getRequestsList();

    /**
     * Set the list of requests.
     * @param requestsList The requests list.
     */
    void setRequestsList(RequestsList requestsList);

    /**
     * Get the current user mode.
     * @return The current user mode.
     */
    UserMode getUserMode();

    /**
     * Set the user mode.
     * @param userMode The user mode.
     */
    void setUserMode(UserMode userMode);
}

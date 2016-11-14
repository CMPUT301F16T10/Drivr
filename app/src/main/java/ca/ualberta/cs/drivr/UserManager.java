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

import java.util.Observable;

/**
 * A (singleton) model class for providing access to application data.
 */
public class UserManager extends Observable implements IUserManager {

    /**
     * The current user.
     */
    private User user;

    /**
     * The current user mode.
     */
    private UserMode userMode;

    /**
     * The user's requests.
     */
    private RequestsList requestsList;

    /**
     * A singleton instance.
     */
    private static final UserManager instance = new UserManager();

    /**
     * Get access to the singleton instance.
     * @return The singleton instance.
     */
    public static UserManager getInstance() {
        return instance;
    }

    /**
     * Instantiate a new user manager.
     */
    protected UserManager() {
        user = new User();
        userMode = UserMode.RIDER;
        requestsList = new RequestsList();
    }

    /**
     * Get the current user.
     * @return The current user.
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * Set the current user.
     * @param user The current user.
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the list of requests.
     * @return The requests list.
     */
    @Override
    public RequestsList getRequestsList() {
        return requestsList;
    }

    /**
     * Set the list of requests.
     * @param requestsList The requests list.
     */
    @Override
    public void setRequestsList(RequestsList requestsList) {
        this.requestsList = requestsList;
    }

    /**
     * Get the current user mode.
     * @return The current user mode.
     */
    @Override
    public UserMode getUserMode() {
        return userMode;
    }

    /**
     * Set the user mode.
     * @param userMode The user mode.
     */
    @Override
    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    /**
     * Notifies all observers of this object that the object has changed. This forces the object
     * into a changed state so all observers will see that the object has changed on every call to
     * this method.
     */
    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}

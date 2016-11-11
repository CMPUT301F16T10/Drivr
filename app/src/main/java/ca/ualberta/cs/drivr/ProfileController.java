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
 * A controller for modifying user profile information.
 */
public class ProfileController {

    /**
     * The user manager that does the operations on requests.
     */
    private IUserManager userManager;

    /**
     * Instantiate a new ProfileController.
     * @param userManager The manager to perform operations with.
     */
    public ProfileController(IUserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * Get the name of the current user.
     * @return The name of the user.
     */
    public String getName() {
        return userManager.getUser().getName();
    }

    /**
     * Set the username of the current user.
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        userManager.getUser().setUsername(username);
    }

    /**
     * Get the username of the current user.
     * @return The username of the user.
     */
    public String getUsername() {
        return userManager.getUser().getUsername();
    }

    /**
     * Set the name of the current user.
     * @param name The name of the user.
     */
    public void setName(String name) {
        userManager.getUser().setName(name);
    }

    /**
     * Get the phone number of the current user.
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return userManager.getUser().getPhoneNumber();
    }

    /**
     * Set the phone number of the current user.
     * @param phoneNumber The phone number of the user.
     */
    public void setPhoneNumber(String phoneNumber) {
        userManager.getUser().setPhoneNumber(phoneNumber);
    }

    /**
     * Get the email of the current user.
     * @return The email of the user.
     */
    public String getEmail() {
        return userManager.getUser().getEmail();
    }

    /**
     * Set the email of the current user.
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        userManager.getUser().setEmail(email);
    }
}

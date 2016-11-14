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
 * A data class for storing user information.
 */
public class User {

    private String name;
    private String username;
    private String phoneNumber;
    private String email;

    /**
     * Instantiate a new User.
     */
    public User() {
        name = "";
        username = "";
        phoneNumber = "";
        email = "";
    }

    /**
     * Instantiate a new User.
     * @param name The name of the user.
     * @param username The username of the user.
     */
    public User(String name, String username) {
        this();
        this.name = name;
        this.username = username;
    }

    /**
     * Get the name.
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the username.
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username.
     * @param username The username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the phone number.
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the phone number.
     * @param phoneNumber The phone number.
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Set the email address.
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the email address.
     * @param email The email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}

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

//TODO: I've just put everything in here to keep things simple, but we could just put the new things related to Drivers in the Driver class.
public class User {

    private String name;
    private String username;
    private String phoneNumber;
    private String email;
    private String vehicleDescription;
    private float rating;
    private int totalRatings;

    /**
     * Instantiate a new User.
     */
    public User() {
        name = "";
        username = "";
        phoneNumber = "";
        email = "";
        vehicleDescription = "";
        rating = 0;
        totalRatings = 0;
    }

    /**
     * Copy constructor.
     * @param other The user to copy
     */
    public User(User other) {
        name = other.getName();
        username = other.getUsername();
        phoneNumber = other.getPhoneNumber();
        email = other.getEmail();
        vehicleDescription = other.getVehicleDescription();
        rating = other.getRating();
        totalRatings = other.getTotalRatings();
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
     * Get the email address.
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email address.
     * @param email The email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Get the driver's vehicle description.
     * @return The vehicle description.
     */
    public String getVehicleDescription() {
        return vehicleDescription;
    }

    /**
     * Set the driver's vehicle description.
     * @param vehicleDescription The vehicle description.
     */
    public void setVehicleDescription(String vehicleDescription) {
        this.vehicleDescription = vehicleDescription;
    }

    /**
     * Get the driver's rating.
     * @return The rating.
     */
    public float getRating() {
        return rating;
    }
    
    /**
     * Get the driver's rating.
     * @param rating The rating.
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Get the driver's total ratings.
     * @return The total ratings.
     */
    public int getTotalRatings() {
        return totalRatings;
    }
    
    /**
     * Set the driver's total ratings.
     * @param totalRatings The total ratings.
     */
    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    /**
     * Changes the driver's rating by multiplying it by totalRatings to get his overall sum of
     * ratings, then increases totalRatings by 1, adds the new rating to this rating then
     * divides it by the new totalRatings.
     * 
     * @param rating The rating given to the driver.
     */
    public void changeRating(float rating) {
        this.rating *= totalRatings;
        totalRatings++;
        this.rating += rating;
        this.rating /= totalRatings;
    }
}

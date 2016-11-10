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

/**
 * Created by adam on 2016-10-12.
 */

public class User {

    private String userId;
    private String name;
    private String username;
    private String phoneNumber;
    private String email;
    private String address;
    private String creditCard;
    private int rating;

    public User() {
        userId = "";
        name = "";
        username = "";
        phoneNumber = "";
        email = "";
        address = "";
        creditCard = null;
        rating = 0;
    }

    public User(String name, String username) {
        this();
        this.name = name;
        this.username = username;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getRating(UserMode userMode) {
        return rating;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public void setRating(UserMode userMode, int rating) {
        this.rating = rating;
    }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }
}

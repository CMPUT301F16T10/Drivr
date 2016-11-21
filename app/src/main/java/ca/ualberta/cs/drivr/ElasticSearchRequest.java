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

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * This class is used to hold the document gotten for requests in ElasticSearch before converting
 * it to a proper request.
 *
 * @author Tiegan Bonowicz
 * @see ElasticSearchController
 * @see Request
 */

public class ElasticSearchRequest {

    private String rider;
    private ArrayList<Driver> driver;
    private String description;
    private double fare;
    private String date;
    private String sourceAddress;
    private double []start;
    private String destinationAddress;
    private double []end;
    @JestId
    private String id;

    /**
     * Constructor for ElasticSearchRequest.
     */
    public ElasticSearchRequest() {
        rider = "";
        driver = new ArrayList<Driver>();
        description = "";
        fare = 0;
        date = "";
        sourceAddress = "";
        start = new double[2];
        destinationAddress = "";
        end = new double[2];
    }

    /**
     * Returns the rider username.
     * @return Rider's username
     */
    public String getRider() {
        return rider;
    }

    /**
     * Sets the rider username.
     * @param rider Rider's username
     */
    public void setRider(String rider) {
        this.rider = rider;
    }

    /**
     * Return the ArrayList of driver objects.
     * @return ArrayList containing drivers.
     */
    public ArrayList<Driver> getDrivers() {
        return driver;
    }

    /**
     * Set the ArrayList of driver objects.
     * @param driver ArrayList containing the drivers.
     */
    public void setDrivers(ArrayList<Driver> driver) {
        this.driver = driver;
    }

    /**
     * Get the description of the request.
     * @return Request's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description of the request.
     * @param description Request's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the fare of the request.
     * @return Request's fare.
     */
    public double getFare() {
        return fare;
    }

    /**
     * Set the fare of the request.
     * @param fare Request's fare.
     */
    public void setFare(double fare) {
        this.fare = fare;
    }

    /**
     * Get the date the request was made.
     * @return Request's date
     */
    public String getDate() {
        return date;
    }

    /**
     * Set the date the request was made.
     * @param date Request's date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Set the starting location.
     * @return Double array containing latitude and longitude
     */
    public double[] getStart() {
        return start;
    }

    /**
     * Set the starting location.
     * @param start Double array containing latitude and longitude
     */
    public void setStart(double[] start) {
        this.start = start;
    }

    /**
     * Get the ending location.
     * @return Double array containing latitude and longitude.
     */
    public double[] getEnd() {
        return end;
    }

    /**
     * Set the ending location
     * @param end Double array containing latitude and longitude
     */
    public void setEnd(double[] end) {
        this.end = end;
    }

    /**
     * Get the ID of the request in ElasticSearch
     * @return Request's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set the ID of the request in ElasticSearch
     * @param id Request's ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the source address of the request in ElasticSearch
     * @return The address the rider's picked up from
     */
    public String getSourceAddress() {
        return sourceAddress;
    }

    /**
     * Sets the source address of the request in ElasticSearch
     * @param sourceAddress The address the rider's picked up from
     */
    public void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    /**
     * Gets the destination address of the request in ElasticSearch
     * @return The address the rider's dropped off at
     */
    public String getDestinationAddress() {
        return destinationAddress;
    }

    /**
     * Sets the destination address of the request in ElasticSearch
     * @param destinationAddress The address the rider's dropped off at
     */
    public void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }
}

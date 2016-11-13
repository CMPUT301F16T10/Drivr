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
    private double []start;
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
        start = new double[2];
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
    public ArrayList<Driver> getDriver() {
        return driver;
    }

    /**
     * Set the ArrayList of driver objects.
     * @param driver ArrayList containing the drivers.
     */
    public void setDriver(ArrayList<Driver> driver) {
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
}
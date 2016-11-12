package ca.ualberta.cs.drivr;

import io.searchbox.annotations.JestId;

/**
 * This class is used to hold the document gotten for requests in ElasticSearch before converting
 * it to a proper request.
 *
 * @author Tiegan Bonowicz
 * @see ElasticSearchController
 * @see Request
 */

//TODO: Populate with getters and setters.
public class ElasticSearchRequest {
    private String rider;
    private String driver; //Replace String with Driver Object
    private String description;
    private int fare;
    private String status;
    private String date;
    private double []start;
    private double []end;
    @JestId
    private String id;

    public ElasticSearchRequest() {
        start = new double[2];
        end = new double[2];
    }
}
package ca.ualberta.cs.drivr;

import android.location.Location;

import java.util.ArrayList;

/**
 * Created by colton on 2016-11-23.
 */

public class SearchRequest {
    private String minPrice;
    private String maxPrice;
    private String minPricePer;
    private String maxPricePer;
    private ArrayList<Request> requestList;

    private ConcretePlace location;
    private String Keyword;

    public SearchRequest(String minPrice, String maxPrice, String minPricePer, String maxPricePer, ConcretePlace location, String keyword) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPricePer = minPricePer;
        this.maxPricePer = maxPricePer;
        this.location = location;
        Keyword = keyword;
    }

    public ArrayList<Request> getRequests(){
        //TODO make actual array
        return new ArrayList<>();
//        throw new UnsupportedOperationException();
    }

    private void SearchKeyword() {
        throw new UnsupportedOperationException();
    }
    private void SearchLocation() {
        throw new UnsupportedOperationException();
    }
}

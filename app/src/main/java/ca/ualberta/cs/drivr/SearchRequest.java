package ca.ualberta.cs.drivr;

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * SearchRequest comments.
 */

public class SearchRequest {
    private String minPrice;
    private String maxPrice;
    private String minPricePer;
    private String maxPricePer;
    private ArrayList<Request> requestList;
    private ConnectivityManager connectivityManager;
    private Context context;

    private ConcretePlace location;
    private String keyword;

    public SearchRequest(String minPrice, String maxPrice, String minPricePer, String maxPricePer,
                         ConcretePlace location, String keyword) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPricePer = minPricePer;
        this.maxPricePer = maxPricePer;
        this.location = location;
        this.keyword = keyword;

    }

    public ArrayList<Request> getRequests(Context context){
        this.requestList = new ArrayList<Request>();
        this.context = context;
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!keyword.isEmpty()) {
            SearchKeyword();
        }
        if (location != null) {
            SearchExactLocation();
            SearchNearLocation();
        }
        FilterByPrice();
        FilterByPricePer();
        return this.requestList;
    }

    /**
     * Here, the requests with the given keyword in description are gotten and then added into
     * requestList if they aren't already in requestList.
     */
    private void SearchKeyword() {
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        for (Request searchRequest : elasticSearch.searchRequestByKeyword(this.keyword)) {
            boolean contains = false;
            for(Request currentRequest: requestList) {
                if(currentRequest.getId().equals(searchRequest.getId())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                requestList.add(searchRequest);
            } else {
                Log.i("Search:", "Found a duplicate"); // Just for testing.
            }
        }
    }

    /**
     * Here, the requests with the exact given location are gotten and then added into
     * requestList if they aren't already in requestList.
     */
    private void SearchExactLocation() {
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        for(Request searchRequest:
                elasticSearch.searchRequestByLocation((String) location.getAddress())) {
            boolean contains = false;
            for(Request currentRequest: requestList) {
                if(currentRequest.getId().equals(searchRequest.getId())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                requestList.add(searchRequest);
            } else {
                Log.i("Search:", "Found a duplicate"); // Just for testing.
            }
        }

    }

    /**
     * Here, the requests that are nearby a given location are gotten and then added into
     * requestList if they aren't already in requestList.
     */
    private void SearchNearLocation() {
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        Location geolocation = new Location("");
        geolocation.setLongitude(this.location.getLatLng().longitude);
        geolocation.setLatitude(this.location.getLatLng().latitude);
        for (Request searchRequest : elasticSearch.searchRequestByGeolocation(geolocation)) {
            boolean contains = false;
            for(Request currentRequest: requestList) {
                if(currentRequest.getId().equals(searchRequest.getId())) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                requestList.add(searchRequest);
            } else {
                Log.i("Search:", "Found a duplicate"); // Just for testing.
            }
        }
    }

    /**
     * Here, for each request in requestList, it'll take each request, get the fare and if the
     * fare is less than the minimum price or greater than the maximum price it'll remove it from
     * requestList.
     */
    private void FilterByPrice() {
        for(int i = 0; i < requestList.size(); ++i) {
            double fare = requestList.get(i).getFare().doubleValue();
            if(fare < Double.parseDouble(minPrice) || fare > Double.parseDouble(maxPrice)) {
                requestList.remove(i);
                --i;
            }
        }
    }

    /**
     * Here, for each request in requestList, it'll take each request, if KM isn't 0 then it'll
     * get the value of fare per KM, see if it's less than the minimum price per KM or higher than
     * the maximum price per KM, and if it is remove it from requestList.
     */
    private void FilterByPricePer() {
        for(int i = 0; i < requestList.size(); ++i) {
            if(requestList.get(i).getKm() == 0) {
                continue;
            }

            double farePer = requestList.get(i).getFare().doubleValue()/requestList.get(i).getKm();
            if(farePer < Double.parseDouble(minPricePer)
                    || farePer > Double.parseDouble(maxPricePer)) {
                requestList.remove(i);
                --i;
            }
        }
    }
}

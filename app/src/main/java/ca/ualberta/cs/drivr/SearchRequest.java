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

import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * In SearchRequest, a driver is able to search for open requests based on a few factors. The
 * driver can specify the minimum fare, the maximum fare, the minimum price per KM, the maximum
 * price per KM, the location to search nearby for requests going to and from there and keywords
 * that are contained in the requests description.
 *
 * When the driver specifies what they want to search by, the app will go into any of the four
 * functions (searchLocation, searchKeyword, filterByPrice, filterByPriceKm), get the requests as
 * necessary or take the requests and trim them down based on whether or not they match all given
 * parameters.
 *
 * @author Tiegan Bonowicz
 * @see ElasticSearch
 * @see SearchRequestActivity
 * @see SearchRequestsListActivity
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
    private boolean firstSearch;

    /**
     * Here, the class is instantiated with the given parameters.
     *
     * @param minPrice String of the minimum fare a driver will accept
     * @param maxPrice String of the maximum fare a driver will accept
     * @param minPricePer String of the minimum fare per KM a driver will accept
     * @param maxPricePer String of the maximum fare per KM a driver will accept
     * @param location ConcretePlace of the address and geolocaton a driver wants to search nearby
     * @param keyword String of the keyword the driver wants to search for
     */
    public SearchRequest(String minPrice, String maxPrice, String minPricePer, String maxPricePer,
                         ConcretePlace location, String keyword) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minPricePer = minPricePer;
        this.maxPricePer = maxPricePer;
        this.location = location;
        this.keyword = keyword;
    }

    /**
     * Here, the requests for the given parameters will be gotten by creating a new ArrayList of
     * requests, setting the context and connectivityManager and then going into SearchKeyword(),
     * SearchLocation(), filterByPrice() and filterByPriceKm() if the parameters are filled.
     * After going through those filters and searches, the ArrayList of requests is returned.
     *
     * @param context The context of the program to allow for checking the ConnectivityManager.
     * @return The ArrayList of open requests matching the given parameters.
     */
    public ArrayList<Request> getRequests(Context context){
        requestList = new ArrayList<Request>();
        this.context = context;
        connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        firstSearch = true;
        Log.i("Test", maxPricePer);
        if (!keyword.isEmpty()) {
            SearchKeyword();
            firstSearch = false;
        }
        if (location != null) {
            SearchLocation();
            firstSearch = false;
        }

        if(maxPrice != "MAX" || minPrice != "0") {
            FilterByPrice();
            firstSearch = false;
        }
        if(maxPricePer != "MAX" || minPricePer != "0") {
            FilterByPricePer();
        }

        return requestList;
    }

    /**
     * Here, the requests with the given keyword in description are gotten and then added into
     * requestList. Since this is the first function called, there's no need to check the
     * requestList for duplicates, all of them can be added right away.
     */
    private void SearchKeyword() {
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        ArrayList<Request> gottenRequests = elasticSearch.searchRequestByKeyword(this.keyword);
        requestList.addAll(gottenRequests);
    }

    /**
     * Here, the requests with the given location, either exact or surrounding the location, are
     * gotten and then added into requestList if they aren't already in requestList.
     */
    private void SearchLocation() {
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);

        Location geolocation = new Location("");
        geolocation.setLongitude(this.location.getLatLng().longitude);
        geolocation.setLatitude(this.location.getLatLng().latitude);

        if(firstSearch) {
            requestList.addAll(elasticSearch.searchRequestByGeolocation(geolocation));
            firstSearch = false;
        } else {
            for (int i = 0; i < requestList.size(); ++i) {
                boolean contains = false;
                for(Request searchRequest : elasticSearch.searchRequestByGeolocation(geolocation)) {
                    if (requestList.get(i).getId().equals(searchRequest.getId())) {
                        contains = true;
                        break;
                    }
                }

                if (!contains) {
                    requestList.remove(i);
                    --i;
                }
            }
        }

    }

    /**
     * Here, for each request in requestList, it'll take each request, get the fare and if the
     * fare is less than the minimum price or greater than the maximum price it'll remove it from
     * requestList.
     *
     * If there were no previously gotten requests it'll just get all open requests and filter by
     * price.
     */
    private void FilterByPrice() {
        if(maxPrice.equals("MAX")) {
            maxPrice = "100000";
        }

        if(firstSearch) {
            ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
            requestList = elasticSearch.getOpenRequests();
        }

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
     *
     * If there were no previously gotten requests it'll just get all open requests and filter by
     * price per KM.
     */
    private void FilterByPricePer() {
        if(maxPricePer.equals("MAX")) {
            maxPricePer = "100000";
        }

        if(firstSearch) {
            ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
            requestList = elasticSearch.getOpenRequests();
        }

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

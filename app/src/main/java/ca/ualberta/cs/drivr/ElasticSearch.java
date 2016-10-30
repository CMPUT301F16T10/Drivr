package ca.ualberta.cs.drivr;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by colton on 2016-10-23.
 * Edited by tiegan on 2016-10-30.
 * This is the class that interacts with the elastic search database through RESTFUL commands
 * (so isn't it a controller? Or uses a controller?)
 */

/**
 * For location (by geo):
 * "geo_distance": {
 *    "distance": "50km",
 *    "location": [lat, lon] - gotten from given geo-distance
 * }
 *
 * For location (by search):
 * get lat/lon from something - Google? another function?
 * "geo_distance": {
 *     "distance": "50km"
 *     "location": [lat, lon]
 * }
 * (It's literally the same as above. You just need to do one extra step to see if it's a valid location)
 *
 * For user profile:
 * "username": (given username)
 * (usernames should be unique, should be fine)
 *
 * For requests:
 * "username": (given username)
 * (we're searching for rider/driver's requests, not requests near them, so identifier is just
 * their username)
 * (difference between this and user profile is just where we're getting the info from/what we're returning)
 *
 * Requests:
 * ID - id of request in ES
 * Rider - Name of user who made the request
 * Driver - Name of user who accepted request (initially NULL)
 * Status - status of the request (initially pending)
 * Source Location - Lat/Lon of location rider wants to be picked up from
 * Destination Location - Lat/Lon of location
 * Payment - Amount request will pay out (driver)/be paid (rider)
 * Approx. time - Amount of time request will take
 * Pick-up time - Time of pick-up (NULL if not specified - will just be given as "pick me
 * up right fucking now")
 *
 * User Profile:
 * Username
 * Profile image (ummmm, yeah, have fun with that)
 * Name
 * E-mail
 * Phone #
 * Address
 * Credit card info (stored, not gotten)
 *
 * Offline requests:
 * -Maybe store in something like JSON? (don't want to have to repeat making the request)
 * -Update it each time a call to ES is made (when connected) - basically caching it
 *
 * Offline/Online behaviour is not to be implemented right now (US 8), but will be worked upon
 *
 * Here's just my thinking of how to do this:
 * Use SearchBuilder as the Controller (maybe change the name to better reflect)
 * Use ElasticSearch (this) as Model - store info gotten, send info to ES
 */

public class ElasticSearch {
    private ArrayList<Request> offlineRequests;
    private User user;


    public ArrayList<Request> requestSearch(String searchTerm){
        throw new UnsupportedOperationException();
    }

    //    method to post the requests to the elastic search database
    //    returns the reqeustId so that it can be found in the database
    public void requestPost(Request request){
        throw new UnsupportedOperationException();
        //        if post fails add request to offline requests
    }

    //    if the request.requestId is not null then you update instead of post
    public void requestUpdate(Request request){
        throw new UnsupportedOperationException();
        //        if updating fails due to network connection, post data to offlineRequests
    }

    //    load the request for elastic search if the requestid is known
    public Request loadRequest(String requestId){
        throw new UnsupportedOperationException();
    }

    public void saveUser(User user) {
        throw new UnsupportedOperationException();
        //        if network fails, return null, this.user = user

    }

    //    gets the user from the elastic search database
    public User loadUser(String userID){
        throw new UnsupportedOperationException();
    }

    //    When the user gets network connectivity, all offline cached data will be posted to the data base
    public void onNetworkStateChanged(){
//        ConnectivityManager connectivityManager;
//        if (connectivityManager.getActiveNetworkInfo().isConnected()){
//             //       we have network so push update
//        }
        throw new UnsupportedOperationException();




    }

    public ArrayList<Request> getOfflineRequests() {
        return offlineRequests;
    }

    public void setOfflineRequests(ArrayList<Request> offlineRequests) {
        this.offlineRequests = offlineRequests;
    }
}

package ca.ualberta.cs.drivr;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by colton on 2016-10-23.
 */

//THis is the class that interacts with the elastic search database

public class ElasticSearch {
    private ArrayList<Request> offlineRequests;
    private User user;

    public ArrayList<Request> requestSeach(String searchTerm){
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
//
    }

//    gets the user from the elastic search database
    public User loadUser(String userID){
        throw new UnsupportedOperationException();
    }

//    When the user gets network connectivity, all offline cached data will be posted to the data basee
    public void onNetworkStateChanged(){
        throw new UnsupportedOperationException();

    }
}

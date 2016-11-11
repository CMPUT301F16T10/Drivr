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

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by colton on 2016-10-23.
 * This is the class that interacts with the elastic search databse through RESTFUL commands
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
        //        if post fails add item_request to offline requests
    }

    //    if the item_request.requestId is not null then you update instead of post
    public void requestUpdate(Request request){
        throw new UnsupportedOperationException();
        //        if updating fails due to network connection, post data to offlineRequests
    }

    //    load the item_request for elastic search if the requestid is known
    public Request loadRequest(String requestId){
        throw new UnsupportedOperationException();
    }

    public void saveUser(User user) {
        throw new UnsupportedOperationException();
        // if network fails, return null, this.user = user
    }

    // gets the user from the elastic search database
    public User loadUser(String userID){
        throw new UnsupportedOperationException();
    }

    // When the user gets network connectivity, all offline cached data will be posted to the data base
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

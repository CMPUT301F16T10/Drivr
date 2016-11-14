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
 * In ElasticSearch, we store two classes: Requests and Users. For Users, you can add, update and
 * search for a user. For a request, you can add and update a request. You can also search for a
 * request, either by getting all requests with a specific keyword, all requests within a specified
 * geolocation or all requests associated to that user. This class, while handling all calls to the
 * ElasticSearchController, also handles all cases where the user goes offline by storing the info
 * they gave and then uploading it to ElasticSearch when they get connected to the internet again.
 *
 * @author Tiegan Bonowicz
 * @see ElasticSearchController
 * @see MainActivity
 */

public class ElasticSearch {

    private ArrayList<Request> offlineRequests;
    private ArrayList<Request> requestsMadeOffline;
    private ArrayList<Request> offlineUpdateRequest;
    private User user;
    private ConnectivityManager connectivityManager;
    private boolean newInfo;
    private boolean newUser;

    public ElasticSearch(Context c) {
        newInfo = false;
        newUser = false;
        connectivityManager = (ConnectivityManager)
                c.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * Here, the request is saved by first checking if there's anything made offline that needs to
     * be saved, then seeing if the user is online. If they are, then we add the request in
     * ElasticSearch. Else, we add it into requestsMadeOffline and offlineRequests and set newInfo
     * to true.
     *
     * @param request The request to be saved in ElasticSearch.
     */
    public void saveRequest(Request request) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.AddRequest addRequest = new
                    ElasticSearchController.AddRequest();
            addRequest.execute(request);
        }
        else {
            newInfo = true;
            requestsMadeOffline.add(request);
            offlineRequests.add(request);
        }
    }

    /**
     * Here, the request is updated by first checking if there's anything made offline that needs to
     * be saved, then seeing if the user is online. If they are, then we update the request in
     * ElasticSearch. Else, we add it into offlineUpdateRequest and offlineRequests and set newInfo
     * to true.
     *
     * @param request The request to be updated in ElasticSearch.
     */
    public void updateRequest(Request request) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.UpdateRequest updateRequest = new
                    ElasticSearchController.UpdateRequest();
            updateRequest.execute(request);
        }
        else {
            newInfo = true;
            offlineUpdateRequest.add(request);
            offlineRequests.add(request);
        }
    }

    /**
     * Here, the requests for a user are gotten by first checking if there's anything made offline
     * that needs to be saved, then if the user is online it will get the requests associated to the
     * given username, store them in offlineRequests if successful and return it. Else, if the
     * search was unsuccessful, or the user is offline, it'll just return the previously stored
     * requests.
     *
     * @param username The username of the user.
     * @return The ArrayList of matching requests
     */
    public ArrayList<Request> loadUserRequests(String username) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForRequests requests = new
                    ElasticSearchController.SearchForRequests();
            requests.execute(username);
            try {
                offlineRequests = requests.get();
                return offlineRequests;
            }
            catch (Exception e) {
                return offlineRequests;
            }
        }
        else {
            return offlineRequests;
        }
    }

    /**
     * Here, the requests for a user are gotten by first checking if there's anything made offline
     * that needs to be saved, then if the user is online it will get the requests within 50km of
     * the given geolocation and return the results gotten if successful. Else, if the
     * search was unsuccessful, or the user is offline, it'll return null.
     *
     * @param geolocation The location given by the user.
     * @return The ArrayList of matching requests
     */
    public ArrayList<Request> searchRequestByGeolocation(Location geolocation) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForLocationRequests searchRequest = new
                    ElasticSearchController.SearchForLocationRequests();
            searchRequest.execute(geolocation);
            try {
                return searchRequest.get();
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the user.");
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * Here, the requests for a given keyword are gotten by first checking if there's anything
     * offline that needs to be saved, then if the user is online it will get the requests with the
     * given keyword in description and return the results gotten if successful. Else, if the
     * search was unsuccessful, or the user is offline, it'll return null.
     *
     * @param searchTerm The keyword for the search.
     * @return The ArrayList of matching requests
     */
    public ArrayList<Request> searchRequestByKeyword(String searchTerm) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForKeywordRequests searchRequest = new
                    ElasticSearchController.SearchForKeywordRequests();
            searchRequest.execute(searchTerm);
            try {
                return searchRequest.get();
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the user.");
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * Here, the requests for a given keyword are gotten by first checking if there's anything
     * offline that needs to be saved. Next, if the user is connected to the internet, it will first
     * check to see if the username already exists in ElasticSearch by using GetUser in the
     * controller and then storing it. If the user returned by the serach is null, then it'll save
     * the user given by the user in ElasticSearch and return true. Else, if the username's already
     * stored in ElasticSearch, or the user is offline, it'll return false, with the app saving the
     * user's info for when they come back online.
     *
     * Returning a boolean is necessary since we shouldn't allow user to initially get into the app
     * unless their user info is in ElasticSearch.
     *
     * @param user The user to be stored in ElasticSearch.
     * @return True or False to indicate the state
     */
    public boolean saveUser(User user) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            User inUser;
            ElasticSearchController.GetUser searchUser = new ElasticSearchController.GetUser();
            searchUser.execute(user.getUsername());
            try {
                inUser = searchUser.get();
            }
            catch (Exception e) {
                return false;
            }

            if (inUser == null) {
                ElasticSearchController.AddUser addUser = new ElasticSearchController.AddUser();
                addUser.execute(user);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            newInfo = true;
            newUser = true;
            this.user = user;
            return false;
        }
    }

    /**
     * Here, the info for a user is updated by making a call to add user to overwrite the
     * immutable document stored in ElasticSearch with the new one.
     *
     * @param user The user to be updated in ElasticSearch.
     */
    public void updateUser(User user) {
        onNetworkStateChanged();
        if(connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.AddUser updateUser = new ElasticSearchController.AddUser();
            updateUser.execute(user);
        }
        else {
            newInfo = true;
            this.user = user;
        }
    }

    /**
     * Returns the user the userID is associated to if it is found in ElasticSearch and the
     * app is online, else it returns null.
     *
     * @param username The username given by the user to search for.
     * @return The user gotten
     */
    public User loadUser(String username) {
        onNetworkStateChanged();
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
            getUser.execute(username);
            try {
                return getUser.get();
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the user.");
                return null;
            }
        }
        else {
            return null;
        }
    }

    /**
     * Here, if the network state is connected to the internet and there's new info to put into
     * ElasticSearch, it will go through the User user and ArrayLists if they aren't null and
     * get their respective calls.
     */
    public void onNetworkStateChanged() {
        if (connectivityManager.getActiveNetworkInfo().isConnected() && newInfo) {
            if (user != null) {
                if(newUser)
                    saveUser(user);
                else
                    updateUser(user);
                newUser = false;
                user = null;
            }

            if (requestsMadeOffline != null) {
                for (int i = 0; i < requestsMadeOffline.size(); i++)
                    saveRequest(requestsMadeOffline.get(i));
                requestsMadeOffline = null;
            }

            if (offlineUpdateRequest != null) {
                for(int i = 0; i < offlineUpdateRequest.size(); i++)
                    saveRequest(offlineUpdateRequest.get(i));
                offlineUpdateRequest = null;
            }

            newInfo = false;
        }
    }

    /**
     * Used solely for testing.
     *
     * @return The user stored in ElasticSearch
     */
    public User getUser() {
        return user;
    }
}

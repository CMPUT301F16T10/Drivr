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

import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import java.math.BigDecimal;
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
    private ArrayList<Request> offlineUserRequests;
    private ArrayList<Request> requestsMadeOffline;
    private ArrayList<Request> offlineUpdateRequest;
    private ArrayList<Request> offlineOpenRequests;
    private User user;
    private ConnectivityManager connectivityManager;
    private boolean newInfo;
    private boolean newUser;

    public ElasticSearch(ConnectivityManager connectivityManager) {
        newInfo = false;
        newUser = false;
        offlineUserRequests = new ArrayList<Request>();
        requestsMadeOffline = new ArrayList<Request>();
        offlineUpdateRequest = new ArrayList<Request>();
        offlineOpenRequests = new ArrayList<Request>();
        this.connectivityManager = connectivityManager;
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
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.AddRequest addRequest = new
                    ElasticSearchController.AddRequest();
            addRequest.execute(request);
        }
        else {
            newInfo = true;
            requestsMadeOffline.add(request);
            offlineUserRequests.add(request);
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
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            if(request.getDrivers().hasOnlyAcceptedDrivers()) {
                ElasticSearchController.GetRequest getRequest =
                        new ElasticSearchController.GetRequest();
                getRequest.execute(request.getId());
                try {
                    Request gottenRequest = getRequest.get();
                    Driver driver = request.getDrivers().get(request.getDrivers().size()-1);
                    request.getDrivers().remove(request.getDrivers().size()-1);
                    if(gottenRequest.getDrivers().hasConfirmedDriver()) {
                        request.setDrivers(gottenRequest.getDrivers());
                        driver.setStatus(RequestState.DECLINED);
                    }
                    else if(!request.getDrivers().equals(gottenRequest.getDrivers())) {
                        request.setDrivers(gottenRequest.getDrivers());
                    }
                    request.getDrivers().add(driver);
                } catch (Exception e) {
                    Log.i("Error", "Getting the specified request failed.");
                }
            }

            ElasticSearchController.UpdateRequest updateRequest = new
                    ElasticSearchController.UpdateRequest();
            updateRequest.execute(request);

            for(int i = 0; i < offlineUserRequests.size(); ++i) {
                if(offlineUserRequests.get(i).getId().equals(request.getId())) {
                    offlineUserRequests.remove(i);
                    offlineUserRequests.add(i, request);
                    break;
                }
            }
        } else {
            newInfo = true;
            offlineUpdateRequest.add(request);
            for(int i = 0; i < offlineUserRequests.size(); ++i) {
                if(offlineUserRequests.get(i).getId().equals(request.getId())) {
                    offlineUserRequests.remove(i);
                    offlineUserRequests.add(i, request);
                    break;
                }
            }
        }
    }

    /**
     * Here, all open requests are gotten and put into offlineOpenRequests. This is done so if the
     * user goes offline they can still search for requests that have been stored since the last
     * time the app updated the list.
     */
    public void getAllOpenRequests() {
        if(connectivityManager.getActiveNetworkInfo().isAvailable()) {
            ElasticSearchController.SearchForOpenRequests searchForOpenRequests =
                    new ElasticSearchController.SearchForOpenRequests();
            searchForOpenRequests.execute();
            try {
                offlineOpenRequests = searchForOpenRequests.get();
                putUsersIntoRequests(offlineOpenRequests);
            } catch (Exception e) {
                Log.i("Error", "Unable to get all open requests.");
            }
        } else {
            Log.i("Error", "Unable to connect to the internet");
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
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForRequests requests = new
                    ElasticSearchController.SearchForRequests();
            requests.execute(username);
            try {
                offlineUserRequests = putUsersIntoRequests(requests.get());
                return offlineUserRequests;
            }
            catch (Exception e) {
                return offlineUserRequests;
            }
        }
        else {
            return offlineUserRequests;
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
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForGeolocationRequests searchRequest = new
                    ElasticSearchController.SearchForGeolocationRequests();
            searchRequest.execute(geolocation);
            try {
                return putUsersIntoRequests(searchRequest.get());
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the requests by geolocation.");
                return offlineOpenRequests;
            }
        }
        else {
            return offlineOpenRequests;
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
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForKeywordRequests searchRequest = new
                    ElasticSearchController.SearchForKeywordRequests();
            searchRequest.execute(searchTerm);
            try {
                return putUsersIntoRequests(searchRequest.get());
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the requests by keyword.");
                ArrayList<Request> keywordRequests = new ArrayList<Request>();
                String[] keywords = searchTerm.split("\\s+");
                for(String keyword: keywords) {
                    for(Request request: offlineOpenRequests) {
                        if(request.getDescription().contains(keyword)) {
                            keywordRequests.add(request);
                        }
                    }
                }
                return keywordRequests;
            }
        }
        else {
            ArrayList<Request> keywordRequests = new ArrayList<Request>();
            String[] keywords = searchTerm.split("\\s+");
            for(String keyword: keywords) {
                for(Request request: offlineOpenRequests) {
                    if(request.getDescription().contains(keyword)) {
                        keywordRequests.add(request);
                    }
                }
            }
            return keywordRequests;
        }
    }

    /**
     * Comments.
     *
     * @param location The specified location the driver wants to find the locations of.
     * @return The ArrayList of open requests with that location.
     */
    public ArrayList<Request> searchRequestByLocation(String location) {
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForLocationRequests searchRequest = new
                    ElasticSearchController.SearchForLocationRequests();
            searchRequest.execute(location);
            try {
                return putUsersIntoRequests(searchRequest.get());
            }
            catch (Exception e) {
                Log.i("Error", "Failed to load the requests by location.");
                ArrayList<Request> locationRequests = new ArrayList<Request>();
                for(Request request: offlineOpenRequests) {
                    String gottenLocation = (String) request.getSourcePlace().getAddress();
                    if(gottenLocation.contains(location)) {
                        locationRequests.add(request);
                        continue;
                    }
                    gottenLocation = (String) request.getDestinationPlace().getAddress();
                    if(gottenLocation.contains(location)) {
                        locationRequests.add(request);
                    }
                }
                return locationRequests;
            }
        }
        else {
            ArrayList<Request> locationRequests = new ArrayList<Request>();
            for(Request request: offlineOpenRequests) {
                String gottenLocation = (String) request.getSourcePlace().getAddress();
                if(gottenLocation.contains(location)) {
                    locationRequests.add(request);
                    continue;
                }
                gottenLocation = (String) request.getDestinationPlace().getAddress();
                if(gottenLocation.contains(location)) {
                    locationRequests.add(request);
                }
            }
            return locationRequests;
        }
    }

    /**
     * Deletes the request from ElasticSearch
     * @param requestId The request to be deleted
     */
    public void deleteRequest(String requestId) {
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.DeleteRequest deleteRequest =
                    new ElasticSearchController.DeleteRequest();
            deleteRequest.execute(requestId);
        } else {
            Log.i("Error", "Unable to connect to the internet");
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
        } else {
            return null;
        }
    }

    /**
     * Deletes the user from ElasticSearch
     * @param username The user to be deleted
     */
    public void deleteUser(String username) {
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.DeleteUser deleteUser =
                    new ElasticSearchController.DeleteUser();
            deleteUser.execute(username);
        } else {
            Log.i("Error", "Unable to connect to the internet");
        }
    }

    /**
     * Here, if the network state is connected to the internet and there's new info to put into
     * ElasticSearch, it will go through the User user and ArrayLists if they aren't null and
     * get their respective calls.
     *
     * //TODO: IMPORTANT: Call this first before doing any other methods. Or change it.
     */
    public void onNetworkStateChanged() {
        if (connectivityManager.getActiveNetworkInfo().isConnected() && newInfo) {
            if (user != null) {
                if(newUser) {
                    saveUser(user);
                }
                else {
                    updateUser(user);
                }
                newUser = false;
                user = null;
            }

            if (requestsMadeOffline != null) {
                for (int i = 0; i < requestsMadeOffline.size(); i++) {
                    saveRequest(requestsMadeOffline.get(i));
                }
                requestsMadeOffline = null;
            }

            if (offlineUpdateRequest != null) {
                for(int i = 0; i < offlineUpdateRequest.size(); i++) {
                    saveRequest(offlineUpdateRequest.get(i));
                }
                offlineUpdateRequest = null;
            }

            newInfo = false;
        }
    }

    /**
     * Here, users are gotten and added to the request.
     *
     * @param requests The ArrayList of requests to get the users for.
     * @return The updated ArrayList of requests
     */
    private ArrayList<Request> putUsersIntoRequests(ArrayList<Request> requests) {
        User temp;

        for(int i = 0; i < requests.size(); ++i) {
            temp = loadUser(requests.get(i).getRider().getUsername());

            if(temp != null) {
                requests.get(i).setRider(temp);
            }

            for(int j = 0; j < requests.get(i).getDrivers().size(); ++j) {
                temp = loadUser(requests.get(i).getDrivers().get(j).getUsername());
                if(temp != null) {
                    requests.get(i).getDrivers().get(j).setName(temp.getName());
                    requests.get(i).getDrivers().get(j).setPhoneNumber(temp.getPhoneNumber());
                    requests.get(i).getDrivers().get(j).setEmail(temp.getEmail());
                    requests.get(i).getDrivers().get(j)
                            .setVehicleDescription(temp.getVehicleDescription());
                    requests.get(i).getDrivers().get(j).setRating(temp.getRating());
                    requests.get(i).getDrivers().get(j).setTotalRatings(temp.getTotalRatings());
                }
            }

        }
        return requests;
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

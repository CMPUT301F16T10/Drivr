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
 * Reference 1:
 * http://stackoverflow.com/questions/7899525/how-to-split-a-string-by-space
 * Author: Gaspard Petit
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
     * Here, the request is saved by first checking if the user is online. If they are, then we add
     * the request in ElasticSearch. Else, we add it into requestsMadeOffline ans set newInfo to
     * true. Either way we will also insert it into offlineUserRequests at the beginning.
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
        }
        offlineUserRequests.add(0, request);
    }

    /**
     * Here, the request is updated by seeing if the user is online. If they are, then we update the
     * request in ElasticSearch by first getting that request and seeing if that request has
     * only accepted drivers. If it does, we get that request from ElasticSearch then compare the
     * newly gotten request with the one we're going to insert into ElasticSearch. If nothing's
     * changed, we leave it alone. Else, if something's changed, we set that driver list to our
     * driver list, change the request state and change our driver's state to declined if necessary.
     *
     * After all that, we finally update the request, we insert the updated request into
     * offlineUserRequests in place of the old one if it's in there. If it isn't because it's a
     * driver accepting a request, we just add it in there at the beginning. If the user's offline,
     * we'll store the request in offlineUpdateRequest in addition to inserting it in
     * offlineUserRequests.
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
                        request.setRequestState(gottenRequest.getRequestState());
                        driver.setStatus(RequestState.DECLINED);
                    }
                    else if(!request.getDrivers().equals(gottenRequest.getDrivers())) {
                        request.setRequestState(gottenRequest.getRequestState());
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
        } else {
            newInfo = true;
            offlineUpdateRequest.add(request);
        }

        boolean inUserRequests = false;
        for(int i = 0; i < offlineUserRequests.size(); ++i) {
            if(offlineUserRequests.get(i).getId().equals(request.getId())) {
                offlineUserRequests.remove(i);
                offlineUserRequests.add(i, request);
                inUserRequests = true;
                break;
            }
        }
        if(!inUserRequests) {
            offlineUserRequests.add(0, request);
        }
    }

    /**
     * Here, the requests for a user are gotten by checking if the user is online. If they are, it
     * will get the requests associated to the given username, store them in offlineRequests if
     * successful and return it. Else, if the search was unsuccessful, or the user is offline, it'll
     * just return the previously stored requests.
     *
     * @param username The username of the user.
     * @return The ArrayList of matching requests
     */
    public ArrayList<Request> loadUserRequests(String username) {
        if (connectivityManager.getActiveNetworkInfo() != null) {
            ElasticSearchController.SearchForUserRequests requests = new
                    ElasticSearchController.SearchForUserRequests();
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
     * Here, all open requests are gotten from ElasticSearch by calling getAllOpenRequests() if
     * online and returning the stored ArrayList offlineOpenRequests.
     *
     * @return The ArrayList of open requests
     */
    public ArrayList<Request> getOpenRequests() {
        if(offlineOpenRequests.size() == 0) {
            if(connectivityManager.getActiveNetworkInfo().isConnected()) {
                getAllOpenRequests();
            }
        }
        return offlineOpenRequests;
    }

    /**
     * Here, the requests for a user are gotten by first checking if the user is online. If it is,
     * it will get the requests within 5km of the given geolocation and return the results gotten if
     * successful. Else, if the search was unsuccessful, or the user is offline, it'll just return
     * the offlineOpenRequests ArrayList.
     *
     * @param geolocation The location given by the user.
     * @return The ArrayList of matching requests (or offlineOpenRequests if offline)
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
     * Here, the requests for a given keyword are gotten by first getting all open requests if
     * connected to the internet. After that, it'll split the searchTerm by space, and for the first
     * keyword, it will add in any request which has a description with that keyword. After the
     * first one, if there's any keywords left, it will go through the ArrayList of requests which
     * match the keywords and take out any which don't match. Then it'll return the ArrayList of
     * open requests with the keywords in the description.
     *
     * @param searchTerm The keyword(s) for the search.
     * @return The ArrayList of matching requests
     */
    public ArrayList<Request> searchRequestByKeyword(String searchTerm) {
        if (connectivityManager.getActiveNetworkInfo().isConnected()) {
            getAllOpenRequests();
        }

        ArrayList<Request> keywordRequests = new ArrayList<Request>();
        String[] keywords = searchTerm.trim().split("\\s+");
        boolean first = true;
        for(String keyword: keywords) {
            if (first) {
                for (int i = 0; i < offlineOpenRequests.size(); ++i) {
                    if (offlineOpenRequests.get(i).getDescription().contains(keyword)) {
                        keywordRequests.add(offlineOpenRequests.get(i));
                    }
                }
                first = false;
            } else {
                for (int i = 0; i < keywordRequests.size(); ++i) {
                    if (!keywordRequests.get(i).getDescription().contains(keyword)) {
                        keywordRequests.remove(i);
                        --i;
                    }
                }
            }
        }
        return keywordRequests;
    }

    /**
     * Deletes the specified request from ElasticSearch.
     *
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
     * Here, the user is saved by first checking if they're connected to the internet. If they are,
     * it will first check to see if the username already exists in ElasticSearch by using GetUser
     * in the controller and then storing it. If the user returned by the search is null, then it'll
     * save the user given by the user in ElasticSearch and return true. Else, if the username's
     * already stored in ElasticSearch, or the user is offline, it'll return false, with the app
     * saving the user's info for when they come back online.
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
     * Deletes the specified user from ElasticSearch.
     *
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
                    updateRequest(offlineUpdateRequest.get(i));
                }
                offlineUpdateRequest = null;
            }

            newInfo = false;
        }
    }

    /**
     * Here, all open requests are gotten and put into offlineOpenRequests. This is done so if the
     * user goes offline they can still search for requests that have been stored since the last
     * time the app updated the list.
     */
    public void getAllOpenRequests() {
        if(connectivityManager.getActiveNetworkInfo().isConnected()) {
            ElasticSearchController.SearchForOpenRequests searchForOpenRequests =
                    new ElasticSearchController.SearchForOpenRequests();
            searchForOpenRequests.execute("");
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
     * Here, the users profiles are loaded from ElasticSearch if the user's online and added to the
     * given requests.
     *
     * @param requests The ArrayList of requests to get the users for.
     * @return The updated ArrayList of requests
     */
    private ArrayList<Request> putUsersIntoRequests(ArrayList<Request> requests) {
        User temp;

        for(int i = 0; i < requests.size(); ++i) {
            if(connectivityManager.getActiveNetworkInfo().isConnected()) {
                temp = loadUser(requests.get(i).getRider().getUsername());

                if (temp != null) {
                    requests.get(i).setRider(temp);
                }

                for (int j = 0; j < requests.get(i).getDrivers().size(); ++j) {
                    temp = loadUser(requests.get(i).getDrivers().get(j).getUsername());
                    if (temp != null) {
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

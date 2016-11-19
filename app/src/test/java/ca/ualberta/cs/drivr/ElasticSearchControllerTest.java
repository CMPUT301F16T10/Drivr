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
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;


/**
 * Tests for the ElasticSearch controller to make sure everything works accordingly.
 *
 * @author Tiegan Bonowicz
 */

//TODO: Use a different test unit for this. It is impossible to use JUnit with the Async tasks.

public class ElasticSearchControllerTest {

    private User user;
    private Request request;

    /**
     * Used to set the rider for each test.
     */
    @Before
    public void setUp() {
        user = new User("rider", "rider1");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");

        request = new Request();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.PENDING);
        inDriver.setUsername("driver1");
        drivers.add(inDriver);
        request.setRider(user);
        request.setDrivers(drivers);
        request.setFare(new BigDecimal(555));
        request.setDate(new Date());
        request.setDescription("Go to Rogers Place");

        ConcretePlace temp = new ConcretePlace();
        temp.setLatLng(new LatLng(50, 50));
        request.setSourcePlace(temp);
        temp.setLatLng(new LatLng(55, 55));
        request.setDestinationPlace(temp);
    }

    /**
     * Used to set the request for each test.
     */
//    @Test
//    public void setRequest() {
////        setUser();
//        DriversList drivers = new DriversList();
//        Driver inDriver = new Driver();
//        inDriver.setStatus(RequestState.PENDING);
//        inDriver.setUsername("driver1");
//        drivers.add(inDriver);
//        request.setRider(user);
//        request.setDrivers(drivers);
//        request.setFare(new BigDecimal(555));
//        request.setDate(new Date());
//        request.setDescription("Go to Rogers Place");
//
//        ConcretePlace temp = new ConcretePlace();
//        temp.setLatLng(new LatLng(50, 50));
//        request.setSourcePlace(temp);
//        temp.setLatLng(new LatLng(55, 55));
//        request.setDestinationPlace(temp);
//    }

    /**
     * Test to make sure a request is added and gotten. Uses username search.
     */
    @Test
    public void addAndGetRequest(){
//        setRequest();
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForRequests searchForRequests =
                new ElasticSearchController.SearchForRequests();
        searchForRequests.execute("rider1");
        try {
            gotten = searchForRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        Request gottenRequest = gotten.get(gotten.size()-1);

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a request is updated and gotten. Uses username search.
     */
    @Test
    public void updateAndGetRequest(){
//        setRequest();
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);

        request.setDescription("Easiest thing to change.");
        ElasticSearchController.UpdateRequest updateRequest =
                new ElasticSearchController.UpdateRequest();
        updateRequest.execute(request);

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForRequests searchForRequests =
                new ElasticSearchController.SearchForRequests();
        searchForRequests.execute("rider1");
        try {
            gotten = searchForRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        Request gottenRequest = gotten.get(gotten.size()-1);

        assertEquals(gottenRequest.getDescription(), request.getDescription());
        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a request can be gotten with a keyword.
     */
    @Test
    public void searchRequestWithKeyword(){
//        setRequest();
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForKeywordRequests searchForKeywordRequests =
                new ElasticSearchController.SearchForKeywordRequests();
        searchForKeywordRequests.execute("Rogers");
        try {
            gotten = searchForKeywordRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        Request gottenRequest = gotten.get(gotten.size()-1);

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a request can be gotten with a geolocation.
     */
    @Test
    public void searchRequestWithLocation(){
//        setRequest();
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForLocationRequests searchForLocationRequests =
                new ElasticSearchController.SearchForLocationRequests();
        Location location = new Location("");
        location.setLatitude(50);
        location.setLongitude(50);
        searchForLocationRequests.execute(location);
        try {
            gotten = searchForLocationRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }

        Request gottenRequest = gotten.get(gotten.size()-1);

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a user is added and can be gotten.
     */
    @Test
    public void addAndSearchUser(){
        //Set first async timer here.
//        setUser();
        ElasticSearchController.AddUser addUser = new ElasticSearchController.AddUser();
        addUser.execute(user);
        //End first async timer here.

        //Set second async timer here.
        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("rider1");
        try {
            dup = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }
        //End second async timer here.

        assertEquals(user.getUsername(), dup.getUsername());
    }

    /**
     * Test to make sure updating a user and searching for it works.
     */
    @Test
    public void updateAndSearchUser(){
        //Set first async timer here.
//        setUser();
        ElasticSearchController.AddUser updateUser = new ElasticSearchController.AddUser();
        updateUser.execute(user);
        //End first async timer here.

        //Set second async timer here.
        user.setEmail("test2@test2.test2");;
        updateUser.execute(user);
        //End second async timer here.

        //Set third async timer here.
        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute("test123");
        try {
            dup = getUser.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the user.");
        }
        //End third async timer here.

        assertEquals(user.getEmail(), dup.getEmail());
    }

}

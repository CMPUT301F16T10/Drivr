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
import android.test.mock.MockContext;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertNull;

/**
 * Tests for the methods in the ElasticSearch class.
 *
 * @author Tiegan Bonowicz
 */

//TODO: Grab async testing from StackOverflow, use it, credit it.
//TODO: Use different contexts for online and offline for testing.

public class ElasticSearchTest {

    private User user;
    private Request request;
    private Context context;

    /**
     * Used to set the rider for each test.
     */
    public void setUser() {
        user = new User("rider", "rider1");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");
    }

    /**
     * Used to set the request for each test.
     */
    public void setRequest() {
        setUser();

        DriversList driver = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.PENDING);
        inDriver.setUsername("driver1");
        driver.add(inDriver);
        request.setRider(user);
        request.setDrivers(driver);
        request.setFare(new BigDecimal(555));
        request.setDate(new Date());
        request.setDescription("Go to Rogers Place");

        ConcretePlace temp = new ConcretePlace();
        temp.setLatLng(new LatLng(50, 50));
        request.setSourcePlace(temp);
        temp.setLatLng(new LatLng(55, 55));
        request.setDestinationPlace(temp);
    }

    // ONLINE TESTS

    /**
     * Test for adding a request online and searching for it.
     */
    @Test
    public void addAndGetRequestOnline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for updating a request online and searching for it.
     */
    @Test
    public void updateAndSearchRequestOnline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);
        request.setDescription("Easiest thing to change.");
        elasticSearch.updateRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);

        assertEquals(loadedRequest.getDescription(), request.getDescription());
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for searching for a request online based on location.
     */
    @Test
    public void searchRequestByLocationOnline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);

        Location location = new Location("");
        location.setLatitude(50);
        location.setLongitude(50);

        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByGeolocation(location);
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for searching for a request online based on a keyword.
     */
    @Test
    public void searchRequestByKeywordOnline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByKeyword("Rogers");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for saving a user online and searching for it.
     *
     * This test should fail if the test case is already in ElasticSearch.
     */
    @Test
    public void saveAndSearchUserOnline(){
        context = new MockContext();
        setUser();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser("rider1");
        assertEquals(loadedUser.getUsername(), user.getUsername());
    }

    /**
     * Test for updating a user online and searching for it.
     */
    @Test
    public void updateAndSearchUserOnline(){
        context = new MockContext();
        setUser();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveUser(user);
        user.setEmail("test2@test2.test2");
        elasticSearch.updateUser(user);
        User loadedUser = elasticSearch.loadUser("rider1");
        assertEquals(loadedUser.getEmail(), user.getEmail());
    }

    // OFFLINE TESTS
    /**
     * Test for adding a request offline.
     */
    @Test
    public void addRequestOffline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getRider(), loadedRequest.getRider());
    }

    /**
     * Test for updating a request offline.
     */
    @Test
    public void updateRequestOffline(){
        context = new MockContext();
        setRequest();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.updateRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getRider(), loadedRequest.getRider());
    }

    /**
     * Test for getting requests associated to a user offline.
     */
    @Test
    public void loadRequestsOffline(){
        context = new MockContext();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        assertEquals(loadedRequests.size(), 0);
    }

    /**
     * Test for getting requests by a location offline.
     */
    @Test
    public void searchRequestByLocationOffline(){
        context = new MockContext();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        Location location = new Location("");
        location.setLatitude(50);
        location.setLongitude(50);

        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByGeolocation(location);
        assertNull(loadedRequests);
    }

    /**
     * Test for getting requests by a keyword offline.
     */
    @Test
    public void searchRequestByKeywordOffline(){
        context = new MockContext();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByKeyword("hi");
        assertNull(loadedRequests);
    }

    /**
     * Test for saving a user offline.
     */
    @Test
    public void saveUserOffline(){
        context = new MockContext();
        setUser();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveUser(user);
        assertNotNull(elasticSearch.getUser());
    }

    /**
     * Test for updating a user offline.
     */
    @Test
    public void updateUserOffline(){
        context = new MockContext();
        setUser();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.updateUser(user);
        assertNotNull(elasticSearch.getUser());
    }

    /**
     * Test for getting a user offline.
     */
    @Test
    public void gettingUserOffline(){
        context = new MockContext();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        User user = elasticSearch.loadUser("bob");
        assertNull(user);
    }

    // OFFLINE TO ONLINE TEST
    /**
     * Test for making sure anything added offline gets uploaded online once connected again.
     */
    @Test
    public void onNetworkStateChanged(){
        context = new MockContext();
        ElasticSearch elasticSearch = new ElasticSearch(context);
        elasticSearch.saveRequest(request);
        elasticSearch.onNetworkStateChanged();
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests("rider1");
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }
}

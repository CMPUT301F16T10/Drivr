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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

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

//TODO: Use a different test unit for this. It is impossible to use JUnit with the Mock and Async tasks.

@RunWith(RobolectricTestRunner.class)
public class ElasticSearchTest {

    private User user;
    private Request request;
    private Context context;

    /**
     * Used to set the user and request for each test.
     */
    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        user = new User("rider1", "Jelas");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");

        request = new Request();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.DECLINED);
        inDriver.setUsername("tiegan");
        drivers.add(inDriver);

        Driver inDriver2 = new Driver();
        inDriver2.setStatus(RequestState.ACCEPTED);
        inDriver2.setUsername("danika");
        drivers.add(inDriver2);

        request.setRider(user);
        request.setDrivers(drivers);
        request.setFare(new BigDecimal(555));
        request.setDate(new Date());
        request.setDescription("Go to Rogers Place");

        ConcretePlace temp = new ConcretePlace();
        temp.setLatLng(new LatLng(50, 50));
        temp.setAddress("University of Alberta");
        request.setSourcePlace(temp);

        ConcretePlace temp2 = new ConcretePlace();
        temp2.setAddress("Rogers Place");
        temp2.setLatLng(new LatLng(55, 55));
        request.setDestinationPlace(temp2);
    }

    // ONLINE TESTS

    /**
     * Test for adding a request online and searching for it.
     */
    @Test
    public void addAndGetRequestOnline(){
        //context = new Robolectric.getShadowApplication();
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

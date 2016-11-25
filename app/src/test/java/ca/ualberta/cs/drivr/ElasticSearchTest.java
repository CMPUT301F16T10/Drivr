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
import android.net.NetworkInfo;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Tests for the methods in the ElasticSearch class.
 *
 * @author Tiegan Bonowicz
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ElasticSearchTest {

    private User user;
    private Request request;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    /**
     * Used to set the user and request for each test.
     */
    @Before
    public void setUp() {
        connectivityManager = Mockito.mock(ConnectivityManager.class);
        networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        ShadowLog.stream = System.out;
        user = new User("rider1", "Jelas");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");

        request = new Request();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.ACCEPTED);
        inDriver.setUsername("tiegan");
        drivers.add(inDriver);

        request.setRider(user);
        request.setDrivers(drivers);
        request.setFareString("555.55");
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
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for updating a request online and searching for it.
     */
    @Test
    public void updateAndSearchRequestOnline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        request.setDescription("Easiest thing to change.");
        elasticSearch.updateRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(loadedRequest.getDescription(), request.getDescription());
        assertEquals(request.getId(), loadedRequest.getId());

        request.setDescription("Go to Rogers Place");
    }

    /**
     * Test for searching for a request online based on location.
     */
    @Test
    public void searchRequestByGeolocationOnline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        Location location = new Location("");
        location.setLatitude(50);
        location.setLongitude(50);

        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByGeolocation(location);
        Robolectric.flushBackgroundThreadScheduler();

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for searching for a request online based on a keyword.
     */
    @Test
    public void searchRequestByKeywordOnline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByKeyword("Rogers");
        Robolectric.flushBackgroundThreadScheduler();

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getId(), loadedRequest.getId());
    }

    /**
     * Test for searching a request by given address location online.
     */
    @Test
    public void searchRequestByLocationOnline() {
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByLocation("University of Alberta");
        Robolectric.flushBackgroundThreadScheduler();

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

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
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveUser(user);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        User loadedUser = elasticSearch.loadUser(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        ElasticSearchController.DeleteUser deleteUser = new ElasticSearchController.DeleteUser();
        deleteUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(loadedUser.getUsername(), user.getUsername());
    }

    /**
     * Test for updating a user online and searching for it.
     */
    @Test
    public void updateAndSearchUserOnline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveUser(user);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        Robolectric.flushBackgroundThreadScheduler();
        user.setEmail("test2@test2.test2");
        elasticSearch.updateUser(user);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        User loadedUser = elasticSearch.loadUser(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        ElasticSearchController.DeleteUser deleteUser = new ElasticSearchController.DeleteUser();
        deleteUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(loadedUser.getEmail(), user.getEmail());
    }

    // OFFLINE TESTS
    /**
     * Test for adding a request offline.
     */
    @Test
    public void addRequestOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        ShadowLog.v("Size", Integer.toString(loadedRequests.size()));
        assertEquals(request.getRider(), loadedRequest.getRider());
    }

    /**
     * Test for updating a request offline.
     */
    @Test
    public void updateRequestOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.updateRequest(request);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);
        assertEquals(request.getRider(), loadedRequest.getRider());
    }

    /**
     * Test for getting requests associated to a user offline.
     */
    @Test
    public void loadRequestsOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        assertEquals(loadedRequests.size(), 0);
    }

    /**
     * Test for getting requests by a geolocation offline.
     */
    @Test
    public void searchRequestByGeolocationOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
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
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        ArrayList<Request> loadedRequests = elasticSearch.searchRequestByKeyword("hi");
        assertNull(loadedRequests);
    }

    /**
     * Test for getting requests by price offline.
     */
    @Test
    public void searchRequestByLocationOffline() {
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        ArrayList<Request> loadedRequests =
                elasticSearch.searchRequestByLocation("University of Alberta");
        assertNull(loadedRequests);
    }

    /**
     * Test for saving a user offline.
     */
    @Test
    public void saveUserOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveUser(user);
        assertNotNull(elasticSearch.getUser());
    }

    /**
     * Test for updating a user offline.
     */
    @Test
    public void updateUserOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.updateUser(user);
        assertNotNull(elasticSearch.getUser());
    }

    /**
     * Test for getting a user offline.
     */
    @Test
    public void gettingUserOffline(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        User user = elasticSearch.loadUser("bob");
        assertNull(user);
    }

    // OFFLINE TO ONLINE TEST
    /**
     * Test for making sure anything added offline gets uploaded online once connected again.
     */
    @Test
    public void onNetworkStateChanged(){
        Mockito.when(networkInfo.isConnected()).thenReturn(false);
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
        elasticSearch.saveRequest(request);

        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        elasticSearch.onNetworkStateChanged();
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> loadedRequests = elasticSearch.loadUserRequests(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        Request loadedRequest = loadedRequests.get(loadedRequests.size()-1);

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(request.getId(), loadedRequest.getId());
    }
}

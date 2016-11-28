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

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;


/**
 * Tests for the ElasticSearch controller to make sure everything works accordingly.
 *
 * @author Tiegan Bonowicz
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ElasticSearchControllerTest {

    private User user;
    private Request request;

    /**
     * Used to set the user and request for each test.
     */
    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
        user = new User("rider1", "rider1");
        user.setPhoneNumber("123-456-7890");
        user.setEmail("test@test.test");

        request = new Request();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.ACCEPTED);
        inDriver.setUsername("driver1");
        drivers.add(inDriver);

        Driver inDriver2 = new Driver();
        inDriver2.setStatus(RequestState.ACCEPTED);
        inDriver2.setUsername("driver2");
        drivers.add(inDriver2);

        request.setRequestState(RequestState.ACCEPTED);
        request.setRider(user);
        request.setDrivers(drivers);
        request.setFareString("555.55");
        request.setDate(new Date());
        request.setDescription("Go to Rogers Place");
        request.setKm(100);

        ConcretePlace temp = new ConcretePlace();
        temp.setLatLng(new LatLng(50, 50));
        temp.setAddress("University of Alberta");
        request.setSourcePlace(temp);

        ConcretePlace temp2 = new ConcretePlace();
        temp2.setAddress("Rogers Place");
        temp2.setLatLng(new LatLng(55, 55));
        request.setDestinationPlace(temp2);
    }

    /**
     * Test to make sure a request is added and gotten. Uses request ID search.
     *
     * Use Case 1 - US 01.01.01
     * As a rider, I want to request rides between two locations.
     */
    @Test
    public void addAndGetRequest(){
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        Request gottenRequest = new Request();
        ElasticSearchController.GetRequest getRequest =
                new ElasticSearchController.GetRequest();
        getRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();
        try {
            gottenRequest = getRequest.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the request.");
        }

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a request is updated and gotten. Uses username search.
     *
     * Use Case 2 - US 01.02.01
     * As a rider, I want to see current requests I have open.
     *
     * Use Case 4 - US 01.04.01
     * As a rider, I want to cancel requests.
     *
     * Use Case 7 - US 01.07.01
     * As a rider, I want to confirm the completion of a request and enable payment.
     *
     * Use Case 8 - US 01.08.01
     * As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of
     * acceptances if more than 1 driver accepts simultaneously.
     *
     * Use Case 22 - US 05.01.01
     * As a driver,  I want to accept a request I agree with and accept that offered payment upon
     * completion.
     *
     * Use Case 23 - US 05.02.01
     * As a driver, I want to view a list of things I have accepted that are pending, each request
     * with its description, and locations.
     */
    @Test
    public void updateAndGetRequest(){
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ElasticSearchController.UpdateRequest updateRequest =
                new ElasticSearchController.UpdateRequest();
        updateRequest.execute(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForUserRequests searchForRequests =
                new ElasticSearchController.SearchForUserRequests();
        searchForRequests.execute("driver1");
        Robolectric.flushBackgroundThreadScheduler();

        try {
            gotten = searchForRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the request.");
        }

        Request gottenRequest = gotten.get(0);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(gottenRequest.getDescription(), request.getDescription());
        assertEquals(request.getId(), gottenRequest.getId());

        request.setDescription("Go to Rogers Place");
    }


    /**
     * Test to make sure an open request can be gotten.
     *
     * Use Case 18 - US 04.02.01
     * As a driver, I want to browse and search for open requests by keyword.
     */
    @Test
    public void searchOpenRequest(){
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForOpenRequests searchForOpenRequests =
                new ElasticSearchController.SearchForOpenRequests();
        searchForOpenRequests.execute("");
        Robolectric.flushBackgroundThreadScheduler();

        try {
            gotten = searchForOpenRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the request.");
        }

        Request gottenRequest = gotten.get(0);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a request can be gotten with a geolocation.
     *
     * Use Case 17 - US 04.01.01
     * As a driver, I want to browse and search for open requests by geo-location.
     *
     * Use Case 21 - US 04.05.01 (added 2016-11-14)
     * As a driver, I should be able to search by address or nearby an address.
     */
    @Test
    public void searchRequestWithGeolocation(){
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        ArrayList<Request> gotten = new ArrayList<Request>();
        ElasticSearchController.SearchForGeolocationRequests searchForLocationRequests =
                new ElasticSearchController.SearchForGeolocationRequests();
        Location location = new Location("");
        location.setLatitude(50);
        location.setLongitude(50);
        searchForLocationRequests.execute(location);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            gotten = searchForLocationRequests.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to load the request.");
        }

        Request gottenRequest = gotten.get(0);

        ElasticSearchController.DeleteRequest deleteRequest = new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request.getId());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(request.getId(), gottenRequest.getId());
    }

    /**
     * Test to make sure a user is added and can be gotten.
     *
     * Use Case 13 - US 03.01.01
     * As a user, I want a profile with a unique username and my contact information.
     *
     * Use Case 15 - US 03.03.01
     * As a user, I want to, when a username is presented for a thing, retrieve and show its
     * contact information.
     */
    @Test
    public void addAndSearchUser(){
        ElasticSearchController.AddUser addUser = new ElasticSearchController.AddUser();
        addUser.execute(user);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        try {
            dup = getUser.get();
        } catch (Exception e) {
            Log.d("Error", "Failed to load the user.");
        }

        ElasticSearchController.DeleteUser deleteUser = new ElasticSearchController.DeleteUser();
        deleteUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        ShadowLog.v("User dup", dup.getUsername());

        assertEquals(user.getUsername(), dup.getUsername());
    }

    /**
     * Test to make sure updating a user and searching for it works.
     *
     * Use Case 13 - US 03.01.01
     * As a user, I want a profile with a unique username and my contact information.
     *
     * Use Case 14 - US 03.02.01
     * As a user, I want to edit the contact information in my profile.
     *
     * Use Case 15 - US 03.03.01
     * As a user, I want to, when a username is presented for a thing, retrieve and show its
     * contact information.
     */
    @Test
    public void updateAndSearchUser(){
        ElasticSearchController.AddUser updateUser = new ElasticSearchController.AddUser();
        updateUser.execute(user);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        user.setEmail("test2@test2.test2");;
        updateUser.execute(user);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        User dup = null;
        ElasticSearchController.GetUser getUser = new ElasticSearchController.GetUser();
        getUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        try {
            dup = getUser.get();
        } catch (Exception e) {
            ShadowLog.v("Error", "Failed to load the user.");
        }

        ElasticSearchController.DeleteUser deleteUser = new ElasticSearchController.DeleteUser();
        deleteUser.execute(user.getUsername());
        Robolectric.flushBackgroundThreadScheduler();

        assertEquals(user.getEmail(), dup.getEmail());
    }
}

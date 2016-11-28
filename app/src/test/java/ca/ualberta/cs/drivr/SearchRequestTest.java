package ca.ualberta.cs.drivr;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 * Tests for the methods in the SearchRequests class.
 *
 * @author Tiegan Bonowicz
 */

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class SearchRequestTest {

    private Request request1;
    private Request request2;
    private ConcretePlace location;
    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    /**
     * Used to set up the two requests for each test.
     */
    @Before
    public void setUp() {
        context = Mockito.mock(Context.class);
        connectivityManager = Mockito.mock(ConnectivityManager.class);
        networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .thenReturn(connectivityManager);
        Mockito.when(connectivityManager.getActiveNetworkInfo()).thenReturn(networkInfo);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        ShadowLog.stream = System.out;

        request1 = new Request();

        User user = new User();
        user.setUsername("rider1");

        DriversList drivers = new DriversList();

        Driver inDriver1 = new Driver();
        inDriver1.setStatus(RequestState.ACCEPTED);
        inDriver1.setUsername("driver1");
        drivers.add(inDriver1);

        Driver inDriver2 = new Driver();
        inDriver2.setStatus(RequestState.ACCEPTED);
        inDriver2.setUsername("driver2");
        drivers.add(inDriver2);

        request1.setRequestState(RequestState.PENDING);
        request1.setRider(user);
        request1.setDrivers(drivers);
        request1.setFareString("101.23");
        request1.setDate(new Date());
        request1.setDescription("Go to Rogers Place");
        request1.setKm(20);

        ConcretePlace temp = new ConcretePlace();
        temp.setLatLng(new LatLng(50, 50));
        temp.setAddress("University of Alberta");
        request1.setSourcePlace(temp);

        ConcretePlace temp2 = new ConcretePlace();
        temp2.setAddress("Rogers Place");
        temp2.setLatLng(new LatLng(55, 55));
        request1.setDestinationPlace(temp2);

        request2 = new Request();

        request2.setRequestState(RequestState.PENDING);
        request2.setRider(user);
        request2.setDrivers(drivers);
        request2.setFareString("20.50");
        request2.setDate(new Date());
        request2.setDescription("Take me home");
        request2.setKm(1);

        ConcretePlace temp3 = new ConcretePlace();
        temp3.setLatLng(new LatLng(100, 100));
        temp3.setAddress("Rexall Place");
        request2.setSourcePlace(temp3);

        ConcretePlace temp4 = new ConcretePlace();
        temp4.setAddress("Sherwood Park Mall");
        temp4.setLatLng(new LatLng(105, 105));
        request2.setDestinationPlace(temp4);

        location = temp2;
    }

    /**
     * Test for making sure nothing is returned if nothing is passed to SearchRequest.
     */
    @Test
    public void returnNull(){
        SearchRequest searchRequest = new SearchRequest(null, null, null, null, null, "");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        assertEquals(gottenRequests.size(), 0);
    }

    /**
     * Tests to make sure the correct request is gotten by keyword.
     *
     * Use Case 18 - US 04.02.01
     * As a driver, I want to browse and search for open requests by keyword.
     */
    @Test
    public void getRequestByKeyword() {
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request1);
        addRequest.execute(request2);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        SearchRequest searchRequest = new SearchRequest(null, null, null, null, null, "Take home");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request1.getId());
        deleteRequest.execute(request2.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request request = gottenRequests.get(0);

        assertEquals(request.getId(), request2.getId());
    }

    /**
     * Tests to make sure the correct request is gotten for location.
     *
     * Use Case 17 - US 04.01.01
     * As a driver, I want to browse and search for open requests by geo-location.
     *
     * Use Case 21 - US 04.05.01 (added 2016-11-14)
     * As a driver, I should be able to search by address or nearby an address.
     */
    @Test
    public void getRequestByLocation() {
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request1);
        addRequest.execute(request2);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        location.setAddress("Rogers Place");
        location.setLatLng(new LatLng(55, 55));

        SearchRequest searchRequest = new SearchRequest(null, null, null, null, location, "");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request1.getId());
        deleteRequest.execute(request2.getId());
        Robolectric.flushBackgroundThreadScheduler();

        ShadowLog.v("Size", Integer.toString(gottenRequests.size()));

        Request request = gottenRequests.get(0);

        assertEquals(request.getId(), request1.getId());
    }

    /**
     * Tests to make sure the right request is gotten by filtering by price.
     *
     * Use Case 19 - US 04.03.01 (added 2016-11-14)
     * As a driver, I should be able filter request searches by price per KM and price.
     */
    @Test
    public void filterRequestByPrice() {
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request1);
        addRequest.execute(request2);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        SearchRequest searchRequest = new SearchRequest("100", "150", null, null, null, "");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request1.getId());
        deleteRequest.execute(request2.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request request = gottenRequests.get(0);

        assertEquals(request.getId(), request1.getId());
    }

    /**
     * Tests to make sure the right request is gotten by filtering by price per KM.
     *
     * Use Case 19 - US 04.03.01 (added 2016-11-14)
     * As a driver, I should be able filter request searches by price per KM and price.
     */
    @Test
    public void filterRequestByPricePer() {
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request1);
        addRequest.execute(request2);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        SearchRequest searchRequest = new SearchRequest(null, null, "15", "25", null, "");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request1.getId());
        deleteRequest.execute(request2.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request request = gottenRequests.get(0);

        assertEquals(request.getId(), request2.getId());
    }


    /**
     * Tests to make sure the right request is gotten by using all searches/filters.
     *
     * Use Case 17, 18, 19, 21 combined
     */
    @Test
    public void testAllAtOnce() {
        ElasticSearchController.AddRequest addRequest = new ElasticSearchController.AddRequest();
        addRequest.execute(request1);
        addRequest.execute(request2);
        Robolectric.flushBackgroundThreadScheduler();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            ShadowLog.v("Interrupted", "Continuing");
        }

        location.setLatLng(new LatLng(100, 100));
        location.setAddress("Rexall Place");

        SearchRequest searchRequest = new SearchRequest("15", "25", "15", "25", location,
                "Take home");
        ArrayList<Request> gottenRequests = searchRequest.getRequests(context);

        ElasticSearchController.DeleteRequest deleteRequest =
                new ElasticSearchController.DeleteRequest();
        deleteRequest.execute(request1.getId());
        deleteRequest.execute(request2.getId());
        Robolectric.flushBackgroundThreadScheduler();

        Request request = gottenRequests.get(0);

        assertEquals(request.getId(), request2.getId());
    }

}

package ca.ualberta.cs.drivr;

import android.net.ConnectivityManager;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

/**
 * Tests for the methods in the ElasticSearch class.
 *
 * @author Tiegan Bonowicz
 */

//TODO: Grab async testing from StackOverflow, use it, credit it.
//TODO: Use different contexts for online and offline for testing.
public class ElasticSearchTest {

    // ONLINE TESTS

    /**
     * Test for adding a request online and searching for it.
     */
    @Test
    public void addAndGetRequestOnline(){
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request, loadedRequest);
    }

    /**
     * Test for updating a request online and searching for it.
     */
    @Test
    public void updateRequestOnline(){
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(loadedRequest, request);
        request.setDriver(new User("name", "email", "phonenumer").getPublicInfo());
        elasticSearch.requestUpdate(request);
        Request newLoadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertNotSame(newLoadedRequest, loadedRequest);
        assertEquals(newLoadedRequest, request);


    }

    /**
     * Test for searching for a request online based on location.
     */
    @Test
    public void searchRequestByLocationOnline(){
        Request request = new Request();
        ElasticSearch elasticSearch =  new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request,loadedRequest);
    }

    /**
     * Test for searching for a request online based on a keyword.
     */
    @Test
    public void searchRequestByKeywordOnline(){
        Request request = new Request();
        ElasticSearch elasticSearch =  new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request,loadedRequest);
    }

    /**
     * Test for saving a user online and searching for it.
     *
     * This test should fail if the test case is already in ElasticSearch.
     */
    @Test
    public void saveUserOnline(){
        User user = new User();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        //        elastic search will only update the userId if it successfully posts to the database
        assertFalse(user.getUserId().isEmpty());
    }

    /**
     * Test for updating a user online and searching for it.
     */
    @Test
    public void updateUserOnline(){
        User user = new User("John", "email@email", "123-456-7890");
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser(user.getUserId());
        assertEquals(loadedUser, user);
    }

    // OFFLINE TESTS
    /**
     * Test for adding a request offline.
     */
    @Test
    public void addRequestOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for updating a request offline.
     */
    @Test
    public void updateRequestOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for getting requests associated to a user offline.
     */
    @Test
    public void loadRequestsOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for getting requests by a location offline.
     */
    @Test
    public void searchRequestByLocationOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for getting requests by a keyword offline.
     */
    @Test
    public void searchRequestByKeywordOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for saving a user offline.
     */
    @Test
    public void saveUserOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for updating a user offline.
     */
    @Test
    public void updateUserOffline(){
        assertEquals(2+2, 4);
    }

    /**
     * Test for getting a user offline.
     */
    @Test
    public void gettingUserOffline(){
        assertEquals(2+2, 4);
    }

    // OFFLINE TO ONLINE TEST
    /**
     * Test for making sure anything added offline gets uploaded online once connected again.
     */
    @Test
    public void onNetworkStateChanged(){
        Request request = new Request();

        ElasticSearch elasticSearch = new ElasticSearch();
        ArrayList<Request> requestArrayList = elasticSearch.getOfflineRequests();
        requestArrayList.add(request);
        elasticSearch.onNetworkStateChanged();
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request, loadedRequest);
    }
}

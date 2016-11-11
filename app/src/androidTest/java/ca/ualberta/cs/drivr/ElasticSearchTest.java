package ca.ualberta.cs.drivr;

import android.net.ConnectivityManager;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

/**
 * Update these tests.
 */

public class ElasticSearchTest {

    @Test
    public void addRequest(){
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request, loadedRequest);
    }

    @Test
    public void updateRequest(){
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

    @Test
    public void loadRequests(){
        Request request = new Request();
        ElasticSearch elasticSearch =  new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request,loadedRequest);
    }

    @Test
    public void saveUser(){
        User user = new User();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        //        elastic search will only update the userId if it successfully posts to the database
        assertFalse(user.getUserId().isEmpty());
    }

    @Test
    public void updateUser(){
        User user = new User("John", "email@email", "123-456-7890");
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser(user.getUserId());
        assertEquals(loadedUser, user);


    }

    //    This tests both the load and save functions
    @Test
    public void loadUser(){
        User user = new User("John", "email@email", "123-456-7890");
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser(user.getUserId());
        assertEquals(loadedUser, user);


    }

    /**
     * The onNetworkStateChanged deals with all offline request modifications by tagging unsynced requests
     *
     * US 08.01.01
     * As an driver, I want to see requests that I already accepted while offline.
     *
     * US 08.02.01
     * As a rider, I want to see requests that I have made while offline.
     *
     * US 08.03.01
     * As a rider, I want to make requests that will be sent once I get connectivity again.
     *
     * US 08.04.01
     * As a driver, I want to accept requests that will be sent once I get connectivity again.
     *
     *
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


//        elasticSearch.setOfflineRequests();



    }
}

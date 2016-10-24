package ca.ualberta.cs.drivr;

import android.net.ConnectivityManager;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotSame;

/**
 * Created by colton on 2016-10-23.
 */

public class ElasticSearchTest {
    @Test
    public void testRequestPost(){
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request, loadedRequest);
    }

    @Test
    public void testRequestUpdate(){
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
    public void testLoadRequest(){
        Request request = new Request();
        ElasticSearch elasticSearch =  new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getRequestId());
        assertEquals(request,loadedRequest);
    }

    @Test
    public void testSaveUser(){
        User user = new User();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        //        elastic search will only update the userId if it successfully posts to the database
        assertFalse(user.getUserId().isEmpty());
    }

    //    This tests both the load and save functions
    @Test
    public void testLoadUser(){
        User user = new User("John", "email@email", "123-456-7890");
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser(user.getUserId());
        assertEquals(loadedUser, user);


    }

    @Test
    public void testOnNetworkStateChanged(){
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

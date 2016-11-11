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
    public void requestPost() {
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getId());
        assertEquals(request, loadedRequest);
    }

    @Test
    public void requestUpdate() {
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getId());
        assertEquals(loadedRequest, request);
        request.setDriver(new User("name", "username"));
        elasticSearch.requestUpdate(request);
        Request newLoadedRequest = elasticSearch.loadRequest(request.getId());
        assertNotSame(newLoadedRequest, loadedRequest);
        assertEquals(newLoadedRequest, request);
    }

    @Test
    public void loadRequest() {
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.requestPost(request);
        Request loadedRequest = elasticSearch.loadRequest(request.getId());
        assertEquals(request, loadedRequest);
    }

    @Test
    public void saveUser() {
        User user = new User();
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        // elastic search will only update the userId if it successfully posts to the database
        assertFalse(user.getUsername().isEmpty());
    }

    // This tests both the load and save functions
    @Test
    public void loadUser() {
        User user = new User("John", "johns_username");
        ElasticSearch elasticSearch = new ElasticSearch();
        elasticSearch.saveUser(user);
        User loadedUser = elasticSearch.loadUser(user.getUsername());
        assertEquals(loadedUser, user);
    }

    /**
     * The onNetworkStateChanged deals with all offline item_request modifications by tagging unsynced requests
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
     */
    @Test
    public void onNetworkStateChanged() {
        Request request = new Request();
        ElasticSearch elasticSearch = new ElasticSearch();
        ArrayList<Request> requestArrayList = elasticSearch.getOfflineRequests();
        requestArrayList.add(request);
        elasticSearch.onNetworkStateChanged();
        Request loadedRequest = elasticSearch.loadRequest(request.getId());
        assertEquals(request, loadedRequest);
    }
}

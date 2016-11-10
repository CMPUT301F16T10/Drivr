/*
 *  Copyright 2016 CMPUT301F16T10
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package ca.ualberta.cs.drivr;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestsListControllerTest {

    private MockUserManager mockUserManager;
    private Request request1;
    private Request request2;
    private Request request3;

    @Before
    public void setup() {
        mockUserManager = new MockUserManager();
        RequestsList requestsList = new RequestsList();
        request1 = new Request();
        request2 = new Request();
        request3 = new Request();
        requestsList.add(request1);
        requestsList.add(request2);
        requestsList.add(request3);
        mockUserManager.setRequestsList(requestsList);
    }

    /**
     * UC 18 Driver Offer Accepted
     * US 05.04.01 As a Driver, I want to be notified if my ride offer was Confirmed.
     */
    @Test
    public void acceptRequest() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.acceptRequest(request1);
        assertEquals(RequestState.ACCEPTED, request1.getRequestState());
    }


    /**
     * UC 8 Rider Confirms Driver Acceptance
     * US 01.08.01 As a Rider, I want to Confirm a Driver's Acceptance.
     * This allows us to choose from a list of Acceptances if more than one Driver Accepts simultaneously.
     *
     */
    @Test
    public void confirmRequest() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.confirmRequest(request2);
        assertEquals(RequestState.CONFIRMED, request2.getRequestState());
    }

    @Test
    public void declineRequest() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.declineRequest(request3);
        assertEquals(RequestState.DECLINED, request3.getRequestState());
    }


    /**
     * UC 4 Rider Cancels Requests
     * US 01.04.01 As a Rider, I want to cancel Requests.
     */
    @Test
    public void cancelRequest() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.cancelRequest(request1);
        assertEquals(RequestState.CANCELLED, request1.getRequestState());
    }

    /**
     * UC 7 Rider Confirms And Pays
     * US 01.07.01 As a Rider, I want to Confirm the completion of a Request and enable payment.
     * Testing for Completed request
     */
    @Test
    public void completeRequest() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.completeRequest(request2);
        assertEquals(RequestState.COMPLETED, request2.getRequestState());
    }

    /**
     * UC 22 Driver Accepts Requests Offline
     * US 08.04.01 As a Driver, I want to Accept Requests that will be sent once I
     * get connectivity again.
     */
    @Test
    public void acceptRequestOffline() {
        RequestsListController requestsListController = new RequestsListController(mockUserManager);
        requestsListController.acceptRequest(request3);
        assertEquals(RequestState.ACCEPTED, request3.getRequestState());
    }
}

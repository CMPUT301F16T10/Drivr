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

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestsListControllerTest {


    /**
     * UC 18 Driver Offer Accepted
     * US 05.04.01 As a Driver, I want to be notified if my ride offer was Confirmed.
     */

    @Test
    public void acceptRequest() {
        UserManager userManager = new UserManager();
        Request request = new Request();
        userManager.getRequests().add(request);
        RequestsListController requestsListController = new RequestsListController(userManager);
        requestsListController.acceptRequest(request);
        assertEquals(RequestState.ACCEPTED, request.getRequestState());
    }


    /**
     * UC 8 Rider Confirms Driver Acceptance
     * US 01.08.01 As a Rider, I want to Confirm a Driver's Acceptance.
     * This allows us to choose from a list of Acceptances if more than one Driver Accepts simultaneously.
     *
     */

    @Test
    public void confirmRequest() {
        UserManager userManager = new UserManager();
        Request request = new Request();
        userManager.getRequests().add(request);
        RequestsListController requestsListController = new RequestsListController(userManager);
        requestsListController.confirmRequest(request);
        assertEquals(RequestState.CONFIRMED, request.getRequestState());
    }

    @Test
    public void declineRequest() {
        UserManager userManager = new UserManager();
        Request request = new Request();
        userManager.getRequests().add(request);
        RequestsListController requestsListController = new RequestsListController(userManager);
        requestsListController.declineRequest(request);
        assertEquals(RequestState.DECLINED, request.getRequestState());
    }


    /**
     * UC 4 Rider Cancels Requests
     * US 01.04.01 As a Rider, I want to cancel Requests.
     */


    @Test
    public void cancelRequest() {
        UserManager userManager = new UserManager();
        Request request = new Request();
        userManager.getRequests().add(request);
        RequestsListController requestsListController = new RequestsListController(userManager);
        requestsListController.cancelRequest(request);
        assertEquals(RequestState.CANCELLED, request.getRequestState());
    }

    /**
     * UC 7 Rider Confirms And Pays
     * US 01.07.01 As a Rider, I want to Confirm the completion of a Request and enable payment.
     * Testing for Completed request
     */

    @Test
    public void completeRequest() {
        UserManager userManager = new UserManager();
        Request request = new Request();
        userManager.getRequests().add(request);
        RequestsListController requestsListController = new RequestsListController(userManager);
        requestsListController.completeRequest(request);
        assertEquals(RequestState.COMPLETED, request.getRequestState());
    }
}

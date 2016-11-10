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

public class RequestControllerTest {

    private MockUserManager mockUserManager;
    private Request request1;
    private Request request2;
    private Request request3;


    @Before
    public void setup() {
        mockUserManager = new MockUserManager();
        mockUserManager.setRequestsList(new RequestsList());
        request1 = new Request();
        request1.setRequestState(RequestState.PENDING);
        mockUserManager.getRequestsList().add(request1);
        request2 = new Request();
        request2.setRequestState(RequestState.PENDING);
        mockUserManager.getRequestsList().add(request2);
        request3 = new Request();
        request3.setRequestState(RequestState.PENDING);
        mockUserManager.getRequestsList().add(request3);
    }

    /**
     * UC 15 Driver Receives Payment
     * US 05.01.01 As a Driver, I want to Accept a Request I agree with and Accept that
     * offered payment upon completion.
     */
    @Test
    public void acceptRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.acceptRequest(request2);
        assertEquals(RequestState.ACCEPTED, request2.getRequestState());
    }

    /**
     * UC 8 Rider Confirms Driver Acceptance
     * US 01.08.01 As a Rider, I want to Confirm a Driver's Acceptance.
     * This allows us to choose from a list of Acceptances if more than one Driver Accepts simultaneously.
     * UC 18 Driver Offer Accepted
     * US 05.04.01 As a Driver, I want to be notified if my ride offer was Confirmed.
     */
    @Test
    public void confirmRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.confirmRequest(request2);
        assertEquals(RequestState.CONFIRMED, request2.getRequestState());
    }

    /**
     * UC 4 Rider Cancels Requests
     * US 01.04.01 As a Rider, I want to cancel Requests.
     */
    @Test
    public void cancelRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.cancelRequest(request2);
        assertEquals(RequestState.CANCELLED, request2.getRequestState());
    }

    /**
     * UC 7 Rider Confirms And Pays
     * US 01.07.01 As a Rider, I want to Confirm the completion of a Request and enable payment.
     * Testing for Completed request
     */
    @Test
    public void completeRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.completeRequest(request2);
        assertEquals(RequestState.COMPLETED, request2.getRequestState());
    }

    @Test
    public void declineRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.declineRequest(request2);
        assertEquals(RequestState.DECLINED, request2.getRequestState());
    }

    @Test
    public void deleteRequest() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.deleteRequest(request2);
        assertFalse(mockUserManager.getRequestsList().has(request2));
    }

    /**
     * UC 22 Driver Accepts Requests Offline
     * US 08.04.01 As a Driver, I want to Accept Requests that will be sent once I
     * get connectivity again.
     */
    @Test
    public void acceptRequestOffline() {
        RequestController requestController = new RequestController(mockUserManager);
        requestController.acceptRequest(request3);
        assertEquals(RequestState.ACCEPTED, request3.getRequestState());
    }

}

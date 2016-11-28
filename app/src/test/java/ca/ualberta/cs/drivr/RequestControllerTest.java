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

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestControllerTest {

    private MockUserManager mockUserManager;
    private RequestController requestController;
    private Request request1;
    private Request request2;
    private Request request3;

    @Before
    public void setup() {
        mockUserManager = new MockUserManager();
        requestController = new RequestController(mockUserManager);
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

    @Test
    public void canAcceptRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.ACCEPTED);
        assertFalse(requestController.canAcceptRequest(request, UserMode.RIDER));
        assertFalse(requestController.canAcceptRequest(request, UserMode.DRIVER));
        request.setRequestState(RequestState.PENDING);
        assertFalse(requestController.canAcceptRequest(request, UserMode.RIDER));
        assertTrue(requestController.canAcceptRequest(request, UserMode.DRIVER));
    }

    @Test
    public void canConfirmRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.PENDING);
        assertFalse(requestController.canConfirmRequest(request, UserMode.RIDER));
        assertFalse(requestController.canConfirmRequest(request, UserMode.DRIVER));
        request.setRequestState(RequestState.ACCEPTED);
        assertTrue(requestController.canConfirmRequest(request, UserMode.RIDER));
        assertFalse(requestController.canConfirmRequest(request, UserMode.DRIVER));
    }

    @Test
    public void canCancelRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.PENDING);
        assertTrue(requestController.canCancelRequest(request, UserMode.RIDER));
        assertFalse(requestController.canCancelRequest(request, UserMode.DRIVER));
        request.setRequestState(RequestState.ACCEPTED);
        assertFalse(requestController.canCancelRequest(request, UserMode.RIDER));
        assertFalse(requestController.canCancelRequest(request, UserMode.DRIVER));
    }

    @Test
    public void canCompleteRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.PENDING);
        assertFalse(requestController.canCompleteRequest(request, UserMode.RIDER));
        assertFalse(requestController.canCompleteRequest(request, UserMode.DRIVER));
        request.setRequestState(RequestState.CONFIRMED);
        assertTrue(requestController.canCompleteRequest(request, UserMode.RIDER));
        assertFalse(requestController.canCompleteRequest(request, UserMode.DRIVER));
    }


    @Test
    public void canDeclineRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.PENDING);
        assertFalse(requestController.canDeclineRequest(request, UserMode.RIDER));
        assertFalse(requestController.canDeclineRequest(request, UserMode.DRIVER));
        request.setRequestState(RequestState.ACCEPTED);
        assertTrue(requestController.canDeclineRequest(request, UserMode.RIDER));
        assertFalse(requestController.canDeclineRequest(request, UserMode.DRIVER));
    }

    @Test
    public void declineRequest() {
        requestController.declineRequest(request2);
        assertEquals(RequestState.DECLINED, request2.getRequestState());
    }

    @Test
    public void canDeleteRequest() {
        Request request = new Request();
        request.setRequestState(RequestState.CANCELLED);
        assertTrue(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertTrue(requestController.canDeleteRequest(request, UserMode.DRIVER));

        request.setRequestState(RequestState.DECLINED);
        assertTrue(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertTrue(requestController.canDeleteRequest(request, UserMode.DRIVER));

        request.setRequestState(RequestState.COMPLETED);
        assertTrue(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertTrue(requestController.canDeleteRequest(request, UserMode.DRIVER));

        request.setRequestState(RequestState.PENDING);
        assertTrue(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertFalse(requestController.canDeleteRequest(request, UserMode.DRIVER));

        request.setRequestState(RequestState.ACCEPTED);
        assertFalse(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertFalse(requestController.canDeleteRequest(request, UserMode.DRIVER));

        request.setRequestState(RequestState.CONFIRMED);
        assertFalse(requestController.canDeleteRequest(request, UserMode.RIDER));
        assertFalse(requestController.canDeleteRequest(request, UserMode.DRIVER));

    }

    @Test
    public void deleteRequest() {
        requestController.deleteRequest(request2);
        assertFalse(mockUserManager.getRequestsList().has(request2));
    }

    /**
     * UC 22 Driver Accepts Requests Offline
     * US 08.04.01 As a Driver, I want to Accept Requests that will be sent once I
     * get connectivity again.
     */
    /* @Test
    public void acceptRequestOffline() {
        requestController.acceptRequest(request3);
        assertEquals(RequestState.ACCEPTED, request3.getRequestState());
    } */

}

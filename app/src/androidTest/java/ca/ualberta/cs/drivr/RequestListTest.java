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
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestListTest {
    @Test
    public void add() {
        RequestsList requestsList = new RequestsList();
        assertEquals(0, requestsList.size());
        requestsList.add(new Request());
        assertEquals(1, requestsList.size());
    }

    @Test
    public void remove() {
        RequestsList requestsList = new RequestsList();
        Request request = new Request();
        requestsList.add(request);
        assertTrue(requestsList.has(request));
        requestsList.remove(request);
        assertFalse(requestsList.has(request));
    }

    @Test
    public void removeByIndex() {
        RequestsList requestsList = new RequestsList();
        Request request = new Request();
        requestsList.add(request);
        assertTrue(requestsList.has(request));
        requestsList.remove(0);
        assertFalse(requestsList.has(request));
    }

    @Test
    public void has() {
        RequestsList requestsList = new RequestsList();
        Request request = new Request();
        assertFalse(requestsList.has(request));
        requestsList.add(request);
        assertTrue(requestsList.has(request));
    }

    @Test
    public void getRequestsList() {
        RequestsList requestsList = new RequestsList();
        Request request = new Request();
        assertEquals(0, requestsList.getRequestsList().size());
        requestsList.add(request);
        assertEquals(1, requestsList.getRequestsList().size());
    }

    @Test
    public void getRequests() {
        RequestsList requestsList = new RequestsList();
        Request request1 = new Request();
        Request request2 = new Request();
        Request request3 = new Request();
        request1.setRequestState(RequestState.ACCEPTED);
        request2.setRequestState(RequestState.DECLINED);
        request3.setRequestState(RequestState.DECLINED);
        requestsList.add(request1);
        requestsList.add(request2);
        requestsList.add(request3);
        assertEquals(0, requestsList.getRequests(RequestState.COMPLETED).size());
        assertEquals(1, requestsList.getRequests(RequestState.ACCEPTED).size());
        assertEquals(2, requestsList.getRequests(RequestState.DECLINED).size());
    }
}

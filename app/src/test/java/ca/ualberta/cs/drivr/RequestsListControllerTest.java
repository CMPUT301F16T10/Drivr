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

}

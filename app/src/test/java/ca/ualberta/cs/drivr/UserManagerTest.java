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

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2016-10-12.
 */

public class UserManagerTest {

    /*
    * Testing our Manager Class
    *
    * For:
    * Our attributes such as name, username, email, and phonenumber
    * all meet requirements.
    *
    */

    @Test
    public void getAndSetUser() {
        UserManager userManager = UserManager.getInstance();
        User user = new User();
        userManager.setUser(user);
        assertEquals(user, userManager.getUser());
    }

    @Test
    public void getAndSetRequestsList() {
        UserManager userManager = UserManager.getInstance();
        RequestsList requestsList = new RequestsList();
        userManager.setRequestsList(requestsList);
        assertEquals(requestsList, userManager.getRequestsList());
    }

    @Test
    public void getAndSetUserMode() {
        UserManager userManager = UserManager.getInstance();
        userManager.setUserMode(UserMode.DRIVER);
        assertEquals(UserMode.DRIVER, userManager.getUserMode());
        userManager.setUserMode(UserMode.RIDER);
        assertEquals(UserMode.RIDER, userManager.getUserMode());
    }

}

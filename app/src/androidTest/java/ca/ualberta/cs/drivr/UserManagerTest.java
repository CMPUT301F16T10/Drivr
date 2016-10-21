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
    public void getSetName() {
        UserManager userManager = new UserManager();
        userManager.setName("Adam Christiansen");
        assertEquals("Adam Christiansen", userManager.getName());
        try {
            userManager.setName("Some super long name that is way too long and will throw an "
                    + "exception because the software can't handle how long it is");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetUsername() {
        UserManager userManager = new UserManager();
        userManager.setUsername("validusername");
        assertEquals("validusername", userManager.getUsername());
        try {
            userManager.setUsername("#@**invalidusername");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetEmail() {
        UserManager userManager = new UserManager();
        userManager.setEmail("valid@valid.valid");
        assertEquals("valid@valid.valid", userManager.getEmail());
        try {
            userManager.setEmail("invalid email@@invalid.invalid");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetPhoneNumber() {
        UserManager userManager = new UserManager();
        userManager.setPhoneNumber("7801234567");
        assertEquals("valid@valid.valid", userManager.getPhoneNumber());
        try {
            userManager.setPhoneNumber("780123a67");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    //Test for setPassword was here but now it has been removed.
}

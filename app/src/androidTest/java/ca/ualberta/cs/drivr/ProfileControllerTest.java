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

/**
 * Created by adam on 2016-10-12.
 */

public class ProfileControllerTest {


    /**
     * UC 10 Create User Profile
     * US 03.01.01 As a User,
     * I want a profile with a unique username and my contact information.
     *
     * UC 12 Show Information For Another User
     * US 03.03.01 As a User,
     * I want to, when a username is presented for a thing, retrieve and show its contact information
     *
     */


    @Test
    public void getName_and_setName() {
        UserManager userManager = new UserManager();
        ProfileController profileController = new ProfileController(userManager);
        profileController.setName("Adam Christiansen");
        assertEquals("Adam Christiansen", profileController.getName());
    }

    @Test
    public void getPhoneNumber_and_setPhoneNumber() {
        UserManager userManager = new UserManager();
        ProfileController profileController = new ProfileController(userManager);
        profileController.setPhoneNumber("7801234567");
        assertEquals("7801234567", profileController.getPhoneNumber());
    }

    @Test
    public void getEmail_and_setEmail() {
        UserManager userManager = new UserManager();
        ProfileController profileController = new ProfileController(userManager);
        profileController.setEmail("somebody@somedomain.sometld");
        assertEquals("somebody@somedomain.sometld", profileController.getEmail());
    }

    @Test
    public void getAddress_and_setAddress() {
        UserManager userManager = new UserManager();
        ProfileController profileController = new ProfileController(userManager);
        profileController.setAddress("123 Some Road, Some City, Some Province, Some Country");
        assertEquals("123 Some Road, Some City, Some Province, Some Country", profileController.getAddress());
    }

}

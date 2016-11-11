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

    private MockUserManager mockUserManager = null;

    @Before
    public void setup() {
        mockUserManager = new MockUserManager();
        mockUserManager.setUser(new User());
    }

    @Test
    public void getAndSetName() {
        ProfileController profileController = new ProfileController(mockUserManager);
        profileController.setName("Adam Christiansen");
        assertEquals("Adam Christiansen", profileController.getName());
    }

    @Test
    public void getAndSetUsername() {
        ProfileController profileController = new ProfileController(mockUserManager);
        profileController.setUsername("some_username");
        assertEquals("some_username", profileController.getUsername());
    }

    @Test
    public void getAndSetPhoneNumber() {
        ProfileController profileController = new ProfileController(mockUserManager);
        profileController.setPhoneNumber("7801234567");
        assertEquals("7801234567", profileController.getPhoneNumber());
    }

    @Test
    public void getAndSetEmail() {
        ProfileController profileController = new ProfileController(mockUserManager);
        profileController.setEmail("somebody@somedomain.sometld");
        assertEquals("somebody@somedomain.sometld", profileController.getEmail());
    }
}

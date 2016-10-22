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

import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2016-10-12.
 */

public class PrivateUserInfoTest {

    /**
     * UC 10 Create User Profile
     * US 03.01.01 As a User, I want a profile with a unique username and my contact information.
     */

    /** Removed passwords (the test is saved, though, so we can get it back at a later date
     * if necessary)
     * Add address test and credit card info test in its place.
     */

    @Test
    public void setAddress() {
        PrivateUserInfo privateUserInfo = new PrivateUserInfo();
        privateUserInfo.setAddress("Rogers Place");
        try {
            privateUserInfo.setAddress("Some address which is invalid and causes an exception" +
                    "to be thrown");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void setCreditCard() {
        PrivateUserInfo privateUserInfo = new PrivateUserInfo();
        privateUserInfo.setCreditCard("0000-0000-0000-0000");
        try {
            privateUserInfo.setCreditCard("Some fake credit card number + identification");
            assertTrue(false);
        }
        catch (Exception e) {}
    }
}

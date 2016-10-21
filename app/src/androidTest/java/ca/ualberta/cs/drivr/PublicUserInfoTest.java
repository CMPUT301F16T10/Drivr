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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by adam on 2016-10-12.
 */

public class PublicUserInfoTest {

    @Test

    /**
     * For:
     *
     * UC 12 Show Information For Another User
     * US 03.03.01 As a User, I want to, when a username is presented for a thing,
     * retrieve and show its contact information.
     *
     * Testing:
     * Public User Info meets guidelines for each attribute
     *
     */

    public void getSetName() {
        PublicUserInfo publicUserInfo = new PublicUserInfo();
        publicUserInfo.setName("Adam Christiansen");
        assertEquals("Adam Christiansen", publicUserInfo.getName());
        try {
            publicUserInfo.setName("Some super long name that is way too long and will throw an "
                    + "exception because the software can't handle how long it is");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetUsername() {
        PublicUserInfo publicUserInfo = new PublicUserInfo();
        publicUserInfo.setUsername("validusername");
        assertEquals("validusername", publicUserInfo.getUsername());
        try {
            publicUserInfo.setUsername("#@**invalidusername");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetEmail() {
        PublicUserInfo publicUserInfo = new PublicUserInfo();
        publicUserInfo.setEmail("valid@valid.valid");
        assertEquals("valid@valid.valid", publicUserInfo.getEmail());
        try {
            publicUserInfo.setEmail("invalid email@@invalid.invalid");
            assertTrue(false);
        }
        catch (Exception e) {}
    }

    @Test
    public void getSetPhoneNumber() {
        PublicUserInfo publicUserInfo = new PublicUserInfo();
        publicUserInfo.setPhoneNumber("7801234567");
        assertEquals("valid@valid.valid", publicUserInfo.getPhoneNumber());
        try {
            publicUserInfo.setPhoneNumber("780123a67");
            assertTrue(false);
        }
        catch (Exception e) {}
    }


    // Assume a Rider and Driver will have different Ratings
    @Test
    public void ratingForUser(){
        PublicUserInfo publicUserInfo = new PublicUserInfo();
        publicUserInfo.setRating(UserMode.DRIVER, 5);
        publicUserInfo.setRating(UserMode.RIDER, 0);
        assertNotEquals(publicUserInfo.getRating(UserMode.DRIVER), publicUserInfo.getRating(UserMode.RIDER));
    }
}

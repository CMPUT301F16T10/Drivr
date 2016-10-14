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
    // Test all methods work for PublicInfo
    @Test
    public void editPublicUserInfo(){
        User user = new User();
        PublicUserInfo publicUserInfo = new PublicUserInfo();

        publicUserInfo.setName("ABC");
        publicUserInfo.setUserName("CAB");
        publicUserInfo.setEmail("ABC@CAB.com");
        publicUserInfo.setPhoneNumber("780-111-1111");


        assertTrue(user.getPublicInfo().getName().equals("ABC"));
        assertTrue(user.getPublicInfo().getUserName().equals("CAB"));
        assertTrue(user.getPublicInfo().getEmail().equals("ABC@CAB.com"));
        assertTrue(user.getPublicInfo().getPhoneNumber().equals("780-111-1111"));

    }

    // Test the Rating is Different for the Driver vs. Rider
    @Test
    public void ratingForUser(){
        User user = new User();


        PublicUserInfo publicUserInfo = new PublicUserInfo();
        UserManager userManager = new UserManager();
        SettingsController settingsController = new SettingsController(userManager);

        settingsController.setUserMode(UserMode.RIDER);
        int Rating = user.getPublicInfo().getRating();

        settingsController.setUserMode(UserMode.DRIVER);
        int Rating2 = user.getPublicInfo().getRating();

        assertNotEquals(Rating, Rating2);

    }
}

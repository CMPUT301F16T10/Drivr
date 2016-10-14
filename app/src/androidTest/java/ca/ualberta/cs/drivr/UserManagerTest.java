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
        @Test
        public void thisAlwaysPasses() {
            assertEquals(12, 4 * 3);
        }


        // Test UserManager can be created
        @Test
        public void createUserManager() throws Exception {
            assertTrue(new UserManager() instanceof UserManager);
        }


        // Test UserManager can get PublicInfo
        @Test
        public void publicInfoWithUserManager(){
            User user = new User();
            PublicUserInfo publicUserInfo = new PublicUserInfo();
            PrivateUserInfo privateUserInfo = new PrivateUserInfo();

            UserManager userManager = new UserManager();

            user.setPublicInfo(publicUserInfo);
            user.setPrivateInfo(privateUserInfo);

            assertEquals(userManager.getPublicUserInfo(), publicUserInfo);
            assertEquals(userManager.getPrivateUserInfo(), privateUserInfo);
        }





}

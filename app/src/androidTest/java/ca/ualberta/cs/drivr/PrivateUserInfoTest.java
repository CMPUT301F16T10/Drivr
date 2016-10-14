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
    @Test
    public void setPassword() {
        PrivateUserInfo privateUserInfo = new PrivateUserInfo();
        privateUserInfo.setPassword("my password");
        try {
            privateUserInfo.setPassword("Some password that is way too long to be a valid password "
                    + "and will cause an exception to be thrown");
            assertTrue(false);
        }
        catch (Exception e) {}
    }
}

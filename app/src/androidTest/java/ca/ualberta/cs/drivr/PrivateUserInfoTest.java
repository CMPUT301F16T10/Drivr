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

public class PrivateUserInfoTest {
    @Test
    public void thisAlwaysPasses() {
        assertEquals(12, 4 * 3);
    }



    // Test the User's Address is a register Address
    @Test
    public void testAddressExists() {
        User user = new User();
        PrivateUserInfo privateUserInfo = new PrivateUserInfo();
        user.setPrivateInfo(privateUserInfo);
        assertTrue(false);
    }

    // Test the User's CreditCard is always a valid format
    @Test
    public void testCreditCardExists(){
        User user = new User();
        PrivateUserInfo privateUserInfo = new PrivateUserInfo();
        user.setPrivateInfo(privateUserInfo);
        privateUserInfo.setCreditCard("0");
    }

    //Test the User's password meets requirments
    @Test
    public void testPasswordRequirments(){

    }


    //Test the User Private Info is only accessible by User
    @Test
    public void testHiddenPrivateUserInfo(){

    }
}

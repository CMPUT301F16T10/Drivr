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

/**
 * Created by adam on 2016-10-12.
 */

public class ProfileController {

    private IUserManager userManager;

    public ProfileController(IUserManager userManager) {
        this.userManager = userManager;
    }

    public String getName() {
        return userManager.getUser().getName();
    }

    public void setName(String name) {
        userManager.getUser().setName(name);
    }

    public String getPhoneNumber() {
        return userManager.getUser().getPhoneNumber();
    }

    public void setPhoneNumber(String phoneNumber) {
        userManager.getUser().setPhoneNumber(phoneNumber);
    }

    public String getEmail() {
        return userManager.getUser().getEmail();
    }

    public void setEmail(String email) {
        userManager.getUser().setEmail(email);
    }

    public String getAddress() {
        return userManager.getUser().getAddress();
    }

    public void setAddress(String address) {
        userManager.getUser().setAddress(address);
    }
}

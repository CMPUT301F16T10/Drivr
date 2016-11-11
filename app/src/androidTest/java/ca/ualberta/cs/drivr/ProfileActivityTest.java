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

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class ProfileActivityTest extends ActivityInstrumentationTestCase2<ProfileActivity> {

    private Solo solo;

    public ProfileActivityTest() {
        super(ca.ualberta.cs.drivr.ProfileActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /**
     * UC 11 Edit Profile Contact Information
     * US 03.02.01 As a User, I want to edit the contact information in my profile
     */

    public void testEditName() {
        solo.assertCurrentActivity("Expected ProfileActivity", ProfileActivity.class);
        solo.clickOnButton("Edit button");
        final String newName = "New Name McNewNamerson";
        solo.enterText(solo.getEditText("Edit name"), newName);
        solo.clickOnButton("Save button");
        assertTrue(solo.waitForText(newName));
    }

    public void testEditPhoneNumber() {
        solo.assertCurrentActivity("Expected ProfileActivity", ProfileActivity.class);
        solo.clickOnButton("Edit button");
        final String newPhoneNumber = "780-123-9876";
        solo.enterText(solo.getEditText("Edit email"), newPhoneNumber);
        solo.clickOnButton("Save button");
        assertTrue(solo.waitForText(newPhoneNumber));
    }

    public void testEditEmail() {
        solo.assertCurrentActivity("Expected ProfileActivity", ProfileActivity.class);
        solo.clickOnButton("Edit button");
        final String newEmail = "newemail@newdomain.newtld";
        solo.enterText(solo.getEditText("Edit email"), newEmail);
        solo.clickOnButton("Save button");
        assertTrue(solo.waitForText(newEmail));
    }

    public void testEditAddress() {
        solo.assertCurrentActivity("Expected ProfileActivity", ProfileActivity.class);
        solo.clickOnButton("Edit button");
        final String newAddress = "1234 New Address, New City, New Province";
        solo.enterText(solo.getEditText("Edit address"), newAddress);
        solo.clickOnButton("Save button");
        assertTrue(solo.waitForText(newAddress));
    }

}

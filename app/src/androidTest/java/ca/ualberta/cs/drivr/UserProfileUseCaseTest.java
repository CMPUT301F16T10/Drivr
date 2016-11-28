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

import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by Daniel on 2016-11-27.
 */

public class UserProfileUseCaseTest extends ActivityInstrumentationTestCase2<MainActivity> {
   /*
    * Testing for user stories related to user profiles
    *
    * For:
    * 03.01.01:
    *   "As a user, I want a profile with a unique username and my contact information."
    * 03.02.01:
    *   "As a user, I want to edit the contact information in my profile."
    * 03.03.01:
    *   "As a user, I want to, when a username is presented for a thing, retrieve and show its contact information."
    * 03.04.01:
    *  "As a driver, in my profile I can provide details about the vehicle I drive."
    *
    */

    private Solo solo;

    public UserProfileUseCaseTest() {
        super(ca.ualberta.cs.drivr.MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

   /*
    * 03.01.01:
    *   "As a user, I want a profile with a unique username and my contact information."
    */
    public void testAddingNewUser() {
        solo.assertCurrentActivity("Expected LoginActivity", LoginActivity.class);
        TextView signup = (TextView)solo.getView(R.id.login_sign_up_text);
        solo.clickOnView(signup);
        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.enterText((EditText) solo.getView (R.id.login_name), "Daniel");
        solo.enterText((EditText) solo.getView (R.id.login_email), "Daniel@google.com");
        solo.enterText((EditText) solo.getView (R.id.login_phone), "7801234567");
        solo.clickOnButton ("Sign Up");
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
    }

    /*
    * 03.02.01:
    *   "As a user, I want to edit the contact information in my profile."
    */
    public void testEditUser() {

        login();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);
        View login = getActivity().findViewById(R.id.main_fab_login);
        solo.clickOnView(login);
        solo.assertCurrentActivity("Expected LoginActivity", LoginActivity.class);

        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.clickOnText("Sign In");

        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnView(fabButton);
        View profile = getActivity().findViewById(R.id.main_fab_profile);
        solo.clickOnView(profile);

        View editProfile = solo.getView(R.id.profile_edit_icon);
        solo.clickOnView(editProfile);

        solo.clearEditText((EditText) solo.getView(R.id.profile_username_edit_text));
        solo.clearEditText((EditText) solo.getView (R.id.profile_name_edit_text));
        solo.clearEditText((EditText) solo.getView (R.id.profile_email_edit_text));
        solo.clearEditText((EditText) solo.getView (R.id.profile_phone_number_edit_text));
        solo.enterText((EditText) solo.getView(R.id.profile_username_edit_text), "DanielL");
        solo.enterText((EditText) solo.getView (R.id.profile_name_edit_text), "DanielL");
        solo.enterText((EditText) solo.getView (R.id.profile_email_edit_text), "DanielL@google.com");
        solo.enterText((EditText) solo.getView (R.id.profile_phone_number_edit_text), "7801234567");
        solo.clickOnView(editProfile);
    }


//   /*
//    * 03.04.01:
//    *   "As a driver, in my profile I can provide details about the vehicle I drive."
//    */
//    public void testSwitchDriver() {
//        login();
//        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
//        View fabButton = solo.getView(R.id.forTesting);
//        solo.clickOnView(fabButton);
//        View driverMode = solo.getView(R.id.main_driver_mode);
//        solo.waitForView(driverMode);
//        solo.clickOnView(driverMode);
//
//        solo.waitForDialogToOpen();
//        solo.enterText((EditText) AlertDialog, "123123123");
//        solo.clickOnText("Save Description");
//        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
//
//        solo.clickOnView(fabButton);
//        View profile = solo.getView(R.id.main_fab_profile);
//        solo.clickOnView(profile);
//        View editProfile = solo.getView(R.id.profile_edit_icon);
//        solo.clickOnView(editProfile);
//
//        solo.clearEditText((EditText) solo.getView(R.id.vehicle_description_edit_text));
//        solo.enterText((EditText) solo.getView (R.id.vehicle_description_edit_text), "Test Change vehicle description");
//    }

    public void login(){
        solo.assertCurrentActivity("Expected LoginActivity", LoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.clickOnText("Sign In");
    }

}


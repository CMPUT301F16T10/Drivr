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

import android.content.Intent;
import android.graphics.PointF;
import android.support.test.filters.MediumTest;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.SupportMapFragment;
import com.robotium.solo.Solo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by adam on 2016-10-12.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;


    /**
     * Testing our Main Activity can open and have access to all other Activities
     */

    public MainActivityTest() {
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

    public void testOpenSettingsActivity() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.clickOnView( solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.fabSettings));
        solo.assertCurrentActivity("Expected SettingsActivity", SettingsActivity.class);
        solo.goBack();
    }

    public void testOpenProfileActivity() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnView( solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.main_fab_profile));
        solo.assertCurrentActivity("Expected ProfileActivity", ProfileActivity.class);
        solo.goBack();
    }

    public void testOpenRequestHistoryActivity() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnView( solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.main_fah_history));
        solo.assertCurrentActivity("Expected RequestHistoryActivity", RequestHistoryActivity.class);
        solo.goBack();
    }

    public void testOpenRequestsListActivity() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnView( solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.main_fab_requests));
        solo.assertCurrentActivity("Expected RequestsListActivity", RequestsListActivity.class);
        solo.goBack();
    }

    public void testSearchDestination() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.enterText(solo.getEditText("Search bar"), "West Edmonton Mall");
        solo.clickOnButton("Search button");
        assertTrue(solo.waitForText("West Edmonton Mall"));
    }

    /**
     * UC 13 Browse And Search Open Requests
     * US 04.01.01 US 04.01.01 As a Driver, I want to browse and search for open
     * Requests by Geo-location.
     */

    /*public void searchRequestsByGeolocation() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnText("Source");
        final String source = solo.getText("Source").getText().toString();
        // We want 2 occurrences: one in the text view and one on the map
        assertTrue(solo.waitForText(source, 2, 3000));

    }*/

    @MediumTest
    public void testFragment () {

        int fragmentId = R.id.main_map;

        solo.waitForFragmentById (fragmentId);
        SupportMapFragment mFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.main_map);

        solo.waitForView(mFragment);
        View view = solo.getView(fragmentId);
        solo.clickOnView(view);

        solo.waitForDialogToOpen();
        solo.waitForText("Yes");
        solo.clickOnButton("Yes");


    }


        UserManager userManager = UserManager.getInstance();

        public void enterDriverMode() {
            int fragmentId = R.id.main_map;
            solo.waitForFragmentById(fragmentId);
            SupportMapFragment mFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.main_map);

            solo.waitForView(mFragment);

            View fabButton = solo.getView(R.id.forTesting);
            solo.clickOnView(fabButton);

            View driverButton = solo.getView(R.id.main_driver_mode);
            solo.waitForView(driverButton);

            solo.clickOnView(driverButton);


            if (userManager.getUser().getVehicleDescription().isEmpty()) {

                solo.waitForDialogToOpen();
                solo.enterText(solo.getEditText(""), "Car");
                solo.clickOnButton("Save Description");
            }
        }



        public void loginDriver() {

            solo.enterText(solo.getEditText("Username"), "JustinDriver");

            View signButton = solo.getView(R.id.sign_in_button);

            solo.clickOnView(signButton);
        }


    public void loginRider(){


            solo.enterText(solo.getEditText("Username"), "JustinRider");

            View signButton = solo.getView(R.id.sign_in_button);

            solo.clickOnView(signButton);

        }
        public void createDriver(){
            View fabButton = solo.getView(R.id.forTesting);
            solo.clickOnView(fabButton);

            View loginButton = solo.getView(R.id.main_fab_login);
            solo.waitForView(loginButton);

        solo.clickOnView(solo.getView(R.id.login_sign_up_text));
        solo.enterText(solo.getEditText("Username"), "JustinDriver");
        solo.enterText(solo.getEditText("Name"), "Justin");
        solo.enterText(solo.getEditText("Email Address"), "j@gmail.com");
        solo.enterText(solo.getEditText("Phone Number"), "1234567890");

            solo.clickOnView(solo.getView(R.id.login_sign_up_text));
            solo.enterText(solo.getEditText("Username"), "JustinRider");
            solo.enterText(solo.getEditText("Name"), "Justin");
            solo.enterText(solo.getEditText("Email Address"), "j@gmail.com");
            solo.enterText(solo.getEditText("Phone Number"), "1234567890");


            solo.clickOnView(solo.getView(R.id.sign_up_button));

        }


    public void createRider(){;

            View loginButton = solo.getView(R.id.main_fab_login);
            solo.waitForView(loginButton);

      //  View loginButton = solo.getView(R.id.main_fab_login);
       // solo.waitForView(loginButton);

        solo.clickOnView(solo.getView(R.id.login_sign_up_text));
        solo.enterText(solo.getEditText("Username"), "JustinRider");
        solo.enterText(solo.getEditText("Name"), "Justin");
        solo.enterText(solo.getEditText("Email Address"), "j@gmail.com");
        solo.enterText(solo.getEditText("Phone Number"), "1234567890");
            solo.clickOnView(solo.getView(R.id.login_sign_up_text));
            solo.enterText(solo.getEditText("Username"), "JustinDriver");
            solo.enterText(solo.getEditText("Name"), "Justin");
            solo.enterText(solo.getEditText("Email Address"), "j@gmail.com");
            solo.enterText(solo.getEditText("Phone Number"), "1234567890");

            solo.clickOnView(solo.getView(R.id.sign_up_button));
        }

        public void acceptRequest(){

        }

        /**
         * UC 30 SpecifyGeoLocations
         * UC 6 Estimate Fair Cost
         *
         */
    public void testCreateRequestByMapLocation(){
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        loginRider();

        int fragmentId = R.id.main_map;

        solo.waitForFragmentById (fragmentId);
        SupportMapFragment mFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.main_map);

        solo.waitForView(mFragment);
        View view = solo.getView(fragmentId);
        solo.clickOnView(view);

        solo.waitForDialogToOpen();
        solo.clickOnButton("Yes");

        view.scrollTo(100,100);

        solo.clickOnView(view);
        solo.waitForDialogToOpen();
        solo.clickOnButton("Yes");
        solo.scrollDown();

        View viewButton = solo.getView(R.id.new_request_create_button);
        solo.clickOnView(viewButton);

        View acceptButton = solo.getView(R.id.request_accept_text);
        //assertTrue(solo.searchText("No Driver Yet"));
        solo.waitForText("Going from 11410");
        solo.waitForText("12.66");


    }

    /**
     *
     * UC 15 ShowUserInformation
     * US 03.03.01 As a User, I want to, when a username is presented for a thing, retrieve and show its contact information.
     */

    public void testViewProfile(){
        loginRider();
        solo.clickOnView(solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.main_fab_requests));
        solo.clickOnText("JustinDriver");
        solo.waitForActivity(DriverProfileActivity.class);

    }

    /**
     * UC 4 RiderCancelsRequest
     * US 01.04.01 As a Rider, I want to cancel Requests.
     */
    public void testCancelRequest(){
        testCreateRequestByMapLocation();
        solo.clickOnView(solo.getView(R.id.request_accept_text));
        solo.clickOnView(solo.getView(R.id.forTesting));
        solo.clickOnView(solo.getView(R.id.main_fab_requests));
        solo.clickOnView(solo.getView(R.id.item_request_deleted));
    }

    /**
     * UC 21 ViewRequestAddresses
     */
    public void testViewSearchRequests(){

        int fragmentId = R.id.main_map;
        //createDriver();
        loginDriver();
        enterDriverMode();

        View view = solo.getView(fragmentId);
        solo.clickOnView(view);

        solo.scrollDown();

        View viewButton = solo.getView(R.id.request_search_button);
        solo.clickOnView(viewButton);

        solo.waitForText("JustinRider");

    }

    /*
   * UC 1 SpecifyRequestLocations
   * US 01.01.01
   *   As a rider, I want to request rides between two locations.
   *
   * UC 6 EstimateFare
   * US 01.06.01
   *   As a rider, I want an estimate of a fair fare to offer to drivers.
   */
    public void testCreateRequest () {
        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.clickOnText("Sign In");
        /*
        * Not actually selecting the buttons, just selecting the area on the map where the buttons
        * would be
        */
        View profileView = solo.getView(R.id.main_fab_profile);
        View historyView = solo.getView(R.id.main_fah_history);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.clickOnView(profileView);
        solo.clickOnText("Yes");
        solo.clickOnView(historyView);
        solo.clickOnText("Yes");
        solo.assertCurrentActivity("Expected RequestsActivity", NewRequestActivity.class);
        solo.scrollDown();
        solo.clickOnButton("Create");
    }

    /* UC 2 RiderViewRequests
    *   As a rider, I want to see current requests I have open.
    *
    */

    public void testRiderCurrentRequests(){
        //createRider();

        loginRider();
        //testCreateRequestByMapLocation();

        solo.clickOnView(solo.getView(R.id.forTesting));
        View requestButton = solo.getView(R.id.main_fab_requests);
        solo.clickOnView(requestButton);

        solo.waitForView(requestButton);


        solo.waitForActivity(RequestsListActivity.class);
        solo.waitForText("JustinDriver");
    }

    /**
     * UC 31 ViewGeoLocations
     * Driver can we request on a map
     *
     */
    public void testViewGeoLocations(){


        loginRider();
        //testCreateRequestByMapLocation();
        enterDriverMode();


        solo.clickOnView(solo.getView(R.id.forTesting));
        View viewButton = solo.getView(R.id.main_fab_requests);
        solo.clickOnView(viewButton);

        //testViewSearchRequests();

        View address = solo.getView(R.id.item_request_route_text);
        solo.clickOnView(address);

        solo.waitForActivity(RequestActivity.class);
        solo.waitForText("Edmonton");

    }

    /**
     *
     * UC 23 ViewDriverRequests
     * US 05.02.01 As a Driver, I want to view a list of things I have Accepted that are Pending, each Request with its description, and locations.
     */

    public void testViewDriverRequests(){

        //createRider();
        //createDriver();

        // Create Request
        //loginRider();
        //testCreateRequestByMapLocation();

        loginDriver();
        enterDriverMode();
        //acceptRequest();

        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);

        View requestButton = solo.getView(R.id.main_fab_requests);
        solo.waitForView(requestButton);
        solo.clickOnView(requestButton);

        solo.waitForActivity(RequestsListActivity.class);
        solo.waitForText("Edmonton");

    }

    /**
     * UC 5 ContactDriver
     * Note Error may Happen, but test works
     */

    public void testPhoneDriver(){

        loginDriver();
        solo.clickOnView(solo.getView(R.id.forTesting));
        View requestButton = solo.getView(R.id.main_fab_requests);
        solo.waitForView(requestButton);
        solo.clickOnView(requestButton);

        solo.waitForActivity(RequestsListActivity.class);
        solo.waitForText("Edmonton");

        solo.clickOnView(solo.getView(R.id.item_request_call_image));
        solo.waitForText("1234567890");


    }



    /*
    * UC 14 EditProfile
    * 03.02.01:
    *   "As a user, I want to edit the contact information in my profile."
    */
    public void testEditUser() {

        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.clickOnText("Sign In");
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);
        View profile = getActivity().findViewById(R.id.main_fab_profile);
        solo.clickOnView(profile);
        View editProfile = solo.getView(R.id.profile_edit_icon);
        solo.clickOnView(editProfile);

        solo.clearEditText((EditText) solo.getView (R.id.profile_name_edit_text));
        solo.clearEditText((EditText) solo.getView (R.id.profile_email_edit_text));
        solo.clearEditText((EditText) solo.getView (R.id.profile_phone_number_edit_text));
        solo.enterText((EditText) solo.getView(R.id.profile_username_edit_text), "DanielL");
        solo.enterText((EditText) solo.getView (R.id.profile_name_edit_text), "DanielL");
        solo.enterText((EditText) solo.getView (R.id.profile_email_edit_text), "DanielL@google.com");
        solo.enterText((EditText) solo.getView (R.id.profile_phone_number_edit_text), "7801234567");
        solo.clickOnView(editProfile);
    }

    /*
    * Uc 16 AddVehicleInfo
    * 03.04.01:
    *  "As a driver, in my profile I can provide details about the vehicle I drive."
    */

    public void testDriverSwitch() {
        solo.enterText((EditText) solo.getView(R.id.login_username), "Daniel");
        solo.clickOnText("Sign In");
        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);

        View driverMode = solo.getView(R.id.main_driver_mode);
        solo.clickOnView(driverMode);

    }

}

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
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.robotium.solo.Solo;

/**
 * Created by Daniel on 2016-11-27.
 */

public class RequestsUseCaseTests extends ActivityInstrumentationTestCase2<MainActivity>{

    /*
    * US 01.01.01
    *   As a rider, I want to request rides between two locations.
    * US 01.02.01
    *   As a rider, I want to see current requests I have open.
    * US 01.03.01
    *   As a rider, I want to be notified if my request is accepted.
    * US 01.04.01
    *   As a rider, I want to cancel requests.
    * US 01.05.01
    *   As a rider, I want to be able to phone or email the driver who accepted a request.
    * US 01.06.01
    *   As a rider, I want an estimate of a fair fare to offer to drivers.
    * US 01.07.01
    *   As a rider, I want to confirm the completion of a request and enable payment.
    * US 01.08.01
    *   As a rider, I want to confirm a driver's acceptance. This allows us to choose from a list of acceptances if more than 1 driver accepts simultaneously.
    */


    private Solo solo;

    public RequestsUseCaseTests() {
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
    * US 01.01.01
    *   As a rider, I want to request rides between two locations.
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


    /*
    * US 01.04.01
    *   As a rider, I want to cancel requests.
    */

    public void cancelRequest () {
        login();
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        View historyView = solo.getView(R.id.main_fah_history);
        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);
        solo.waitForView(historyView);
        solo.clickOnView(historyView);
        solo.assertCurrentActivity("Expected RequestHistoryActivity", RequestHistoryActivity.class);

    }


    public void login(){
        View fabButton = solo.getView(R.id.forTesting);
        solo.clickOnView(fabButton);
        View login = getActivity().findViewById(R.id.main_fab_login);
        solo.clickOnView(login);
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

}

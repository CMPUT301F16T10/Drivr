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
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        int[] location = new int[2];
        View view = solo.getView(R.id.content_main);
        view.getLocationOnScreen(location);
        int x= location[0];
        int y= location[1];
        solo.clickOnScreen(x,y);
        solo.clickOnScreen(x+1, y+1);
        solo.goBack();
    }

    /*
    * US 01.02.01
    *   As a rider, I want to request rides between two locations.
    */
    public void testRequestHistory () {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        int[] location = new int[2];
        View view = solo.getView(R.id.forTesting);
        view.getLocationOnScreen(location);
        int x= location[0];
        int y= location[1];
        solo.clickOnScreen(x,y);

        View login = getActivity().findViewById(R.id.main_fab_login);
        solo.clickOnView(login);
        solo.assertCurrentActivity("Expected requests list activity", RequestsListActivity.class);
        solo.goBack();
    }

}

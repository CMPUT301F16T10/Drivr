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

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by adam on 2016-10-12.
 */

public class RequestActivityTest extends ActivityInstrumentationTestCase2<RequestActivity> {

    private Solo solo;

    public RequestActivityTest() {
        super(ca.ualberta.cs.drivr.RequestActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testViewSourceLocationOnMap() {
        solo.assertCurrentActivity("Expected RequestActivity", RequestActivity.class);
        solo.clickOnText("Source");
        final String source = solo.getText("Source").getText().toString();
        // We want 2 occurrences: one in the text view and one on the map
        assertTrue(solo.waitForText(source, 2, 3000));
    }

    public void testViewDestinationLocationOnMap() {
        solo.assertCurrentActivity("Expected RequestActivity", RequestActivity.class);
        solo.clickOnText("Destination Location");
        final String destination = solo.getText("Destination Location").getText().toString();
        // We want 2 occurrences: one in the text view and one on the map
        assertTrue(solo.waitForText(destination, 2, 3000));
    }
}

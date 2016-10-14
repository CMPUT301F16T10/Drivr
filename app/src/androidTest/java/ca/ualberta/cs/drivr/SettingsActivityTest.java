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

public class SettingsActivityTest extends ActivityInstrumentationTestCase2<SettingsActivity> {
    @Test
    public void thisAlwaysPasses() {
        assertEquals(12, 4 * 3);
    }

    private Solo solo;

    public SettingsActivityTest() {
        super(ca.ualberta.cs.drivr.SettingsActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testChangeUserMode() {
        solo.assertCurrentActivity("Expected SettingsActivity", SettingsActivity.class);
        // Guarantee the current user mode is rider
        if (solo.waitForText("Rider", 1, 100))
            solo.clickOnButton("Change user mode");

        assertTrue(solo.waitForText("Rider"));
        solo.clickOnButton("Change user mode");
        assertTrue(solo.waitForText("Driver"));
        solo.clickOnButton("Change user mode");
        assertTrue(solo.waitForText("Rider"));
    }

    public void testChangeMapUnits() {
        solo.assertCurrentActivity("Expected SettingsActivity", SettingsActivity.class);
        // Guarantee the current map units are metric
        if (solo.waitForText("Metric", 1, 100))
            solo.clickOnButton("Change map units");

        assertTrue(solo.waitForText("Metric"));
        solo.clickOnButton("Change map units");
        assertTrue(solo.waitForText("Imperial"));
        solo.clickOnButton("Change map units");
        assertTrue(solo.waitForText("Metric"));
    }

}

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

/**
 * Created by adam on 2016-10-12.
 */

/**
 * Testing for swaps in Modes
 * Rider to Driver.
 * Metric to Imperial.
 */
public class SettingsControllerTest {

    @Test
    public void setUserMode() {
        UserManager userManager = new UserManager();
        SettingsController settingsController = new SettingsController(userManager);
        settingsController.setUserMode(UserMode.DRIVER);
        assertEquals(UserMode.DRIVER, userManager.getUserMode());
        settingsController.setUserMode(UserMode.RIDER);
        assertEquals(UserMode.RIDER, userManager.getUserMode());
    }

    @Test
    public void changeUnits() {
        UserManager userManager = new UserManager();
        SettingsController settingsController = new SettingsController(userManager);
        settingsController.setMapUnits(MapUnits.METRIC);
        assertEquals(MapUnits.METRIC, userManager.getMapUnits());
        settingsController.setMapUnits(MapUnits.IMPERIAL);
        assertEquals(MapUnits.IMPERIAL, userManager.getMapUnits());
    }

}

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

import android.location.Location;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by colton on 2016-10-23.
 */

public class SearchBuilderTest {

    @Test
    public void searchString(){
        SearchBuilder searchBuilder = new SearchBuilder();

        String searchTerm = searchBuilder.searchString("icecream");

        assertEquals(searchTerm, "{some json}");
    }


    @Test
    public void searchLocation(){
        SearchBuilder searchBuilder = new SearchBuilder();
        android.location.Location location = new android.location.Location("Universtiy of Alberta");
//        location.set(new Location("U"));

        String searchTerm = searchBuilder.searchLocation(location);

        assertEquals(searchTerm, "{some json}");

    }

}

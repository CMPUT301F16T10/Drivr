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

/**
 * Created by colton on 2016-10-23.
 */

// This builds strings for the elasticsearch database to return
//    There are different types of searched that can be made - location, points of interest ...
public class SearchBuilder {

//    search near a string
    public String searchString(String searchterm){
        throw new UnsupportedOperationException();
    }

//    search near a geolocation
    public String searchLocation(android.location.Location searchTerm ){
        throw new UnsupportedOperationException();
    }
}

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

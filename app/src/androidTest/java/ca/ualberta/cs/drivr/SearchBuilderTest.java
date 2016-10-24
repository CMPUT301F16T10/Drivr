package ca.ualberta.cs.drivr;

import android.location.Location;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by colton on 2016-10-23.
 */

public class SearchBuilderTest {

    @Test
    public void testSearchString(){
        SearchBuilder searchBuilder = new SearchBuilder();

        String searchTerm = searchBuilder.searchString("icecream");

        assertEquals(searchTerm, "{some json}");
    }


    @Test
    public void testSearchLocation(){
        SearchBuilder searchBuilder = new SearchBuilder();
        android.location.Location location = new android.location.Location("Universtiy of Alberta");
//        location.set(new Location("U"));

        String searchTerm = searchBuilder.searchLocation(location);

        assertEquals(searchTerm, "{some json}");

    }

}

package ca.ualberta.cs.drivr;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by colton on 2016-10-23.
 */

public class ElasticSearchControllerTest {

    @Test
    public void searchString(){
        ElasticSearchController elasticSearchController = new ElasticSearchController();

        String searchTerm = elasticSearchController.searchString("icecream");

        assertEquals(searchTerm, "{some json}");
    }


    @Test
    public void searchLocation(){
        ElasticSearchController elasticSearchController = new ElasticSearchController();
        android.location.Location location = new android.location.Location("Universtiy of Alberta");
//        location.set(new Location("U"));

        String searchTerm = elasticSearchController.searchLocation(location);

        assertEquals(searchTerm, "{some json}");

    }

}

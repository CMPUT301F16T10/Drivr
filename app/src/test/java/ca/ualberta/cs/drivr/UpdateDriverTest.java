package ca.ualberta.cs.drivr;


import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 *
 * Have the Ability to Update a Drivers Info
 */

public class UpdateDriverTest {


    /*
    *
     * UC 11 RateDriver
     * US 01.11.01 As a Rider, I want to rate a Driver for his/her service (1-5).
     *
     */
    @Test
    public void updateDriverRating(){

        User user = new User();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setStatus(RequestState.COMPLETED);
        inDriver.setUsername("Driver");
        drivers.add(inDriver);

        Request request = new Request();

        request.setRequestState(RequestState.COMPLETED);
        request.setRider(user);
        request.setDrivers(drivers);
        request.setFareString("555.55");
        request.setDate(new Date());
        request.setDescription("Go to Rogers Place");
        request.setKm(100);

        ArrayList<Driver> driverArrayList = request.getDriversWithStatus(RequestState.COMPLETED);
        Driver finishedDriver = driverArrayList.get(0);
        finishedDriver.setRating(5);

        Assert.assertEquals("Rating Equal", finishedDriver.getRating(), driverArrayList.get(0).getRating());

    }
}

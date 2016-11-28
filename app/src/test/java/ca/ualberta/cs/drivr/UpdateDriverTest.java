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

    /**
     * UC 9 ViewVehicle
     * US 01.09.01 As a Rider, I should see a description of the Driver's Vehicle.
     */

    @Test
    public void getDriverVehicleInfo(){
        User user = new User();

        DriversList drivers = new DriversList();
        Driver inDriver = new Driver();
        inDriver.setVehicleDescription("SUV");
        inDriver.setStatus(RequestState.ACCEPTED);
        inDriver.setUsername("Driver");
        drivers.add(inDriver);

        Request request = new Request();

        request.setRequestState(RequestState.ACCEPTED);
        request.setRider(user);
        request.setDrivers(drivers);

        ArrayList<Driver> driverArrayList = request.getDriversWithStatus(RequestState.ACCEPTED);
        Driver finishedDriver = driverArrayList.get(0);
        Assert.assertEquals("Vehicle Description",finishedDriver.getVehicleDescription(), "SUV");

    }

    /**
     * UC 10 ViewDriverRating
     * US 01.10.01 As a Rider, I want to see some summary rating of the Drivers who accepted my offers.
     */
    @Test
    public void getDriversRatings(){

        UserManager userManager = UserManager.getInstance();

        User user = new User();
        userManager.setUser(user);
        DriversList drivers = new DriversList();



        Driver inDriver = new Driver();
        inDriver.setRating(3);
        inDriver.setStatus(RequestState.ACCEPTED);
        drivers.add(inDriver);

        Driver secondDriver = new Driver();
        secondDriver.setRating(4);
        secondDriver.setStatus(RequestState.ACCEPTED);
        drivers.add(secondDriver);


        Request request = new Request();
        request.setRequestState(RequestState.ACCEPTED);
        request.setId("1");
        request.setRider(user);
        request.setDrivers(drivers);

        RequestsList requestsList = new RequestsList();
        requestsList.add(request);
        userManager.setRequestsList(requestsList);

        ArrayList<Driver> driverArrayList = request.getDriversWithStatus(RequestState.COMPLETED);

        DriversList ratedDrivers = userManager.getRequestsList().getById("1").getDrivers();

        Assert.assertEquals("Driver 1", ( ratedDrivers.get(0).getRating()), 3.0);
        Assert.assertEquals("Driver 2", (ratedDrivers.get(1).getRating()), 4.0);

    }
}

package ca.ualberta.cs.drivr;

import android.location.Location;

import java.util.Currency;
import java.util.Date;

/**
 * Created by adam on 2016-10-12.
 */

public class Request {

    private User rider;
    private User driver;
    private Date date;
    private Currency cost;
    private RequestState requestState;
    private Location source;
    private Location destination;

    public Request() { throw new UnsupportedOperationException(); }

    public Request(User rider, Location source, Location destination) { throw new UnsupportedOperationException(); }

    public Request(User rider, User driver, Location source, Location destination) { throw new UnsupportedOperationException(); }


}

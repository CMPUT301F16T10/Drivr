package ca.ualberta.cs.drivr;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by ~Kilza~ on 2016-11-21.
 */

public class DriverTest {
    @Test
    public void getStatus() {
        Driver driver = new Driver();
        driver.setStatus(RequestState.PENDING);
        RequestState state = driver.getStatus();
        assertNotNull(state);
    }
}

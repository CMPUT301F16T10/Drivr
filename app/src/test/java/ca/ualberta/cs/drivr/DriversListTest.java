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

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by adam on 2016-11-13.
 */

public class DriversListTest {

    private final DriversList driversList = new DriversList();
    private final Driver driver1 = new Driver();
    private final Driver driver2 = new Driver();
    private final Driver driver3 = new Driver();
    private final Driver driver4 = new Driver();
    private final Driver driver5 = new Driver();
    private final Driver driver6 = new Driver();
    private final Driver driver7 = new Driver();

    @Before
    public void setUp() {
        // Setup some drivers with statuses of PENDING, ACCEPTED, DECLINED, and CANCELLED
        driver1.setStatus(RequestState.PENDING);
        driver2.setStatus(RequestState.DECLINED);
        driver3.setStatus(RequestState.ACCEPTED);
        driver4.setStatus(RequestState.CANCELLED);
        driver5.setStatus(RequestState.ACCEPTED);
        driver6.setStatus(RequestState.CANCELLED);
        driver7.setStatus(RequestState.CANCELLED);

        driversList.add(driver1);
        driversList.add(driver2);
        driversList.add(driver3);
        driversList.add(driver4);
        driversList.add(driver5);
        driversList.add(driver6);
        driversList.add(driver7);
    }

    @Test
    public void add() {
        // Check the initial size of the DriversList
        assertEquals(7, driversList.size());

        // Add the first confirmed driver
        final Driver d1 = new Driver();
        d1.setStatus(RequestState.CONFIRMED);
        try {
            driversList.add(d1);
        }
        catch (DriversList.ConfirmedDriverAlreadyExistsException e) {
            fail("add() should not throw");
        }

        // Check the new size of the DriversList
        assertEquals(8, driversList.size());

        // Try to add a second confirmed driver
        final Driver d2 = new Driver();
        d2.setStatus(RequestState.CONFIRMED);
        try {
            driversList.add(d2);
            fail("Expected add() to throw");
        }
        catch (DriversList.ConfirmedDriverAlreadyExistsException e) {
            // Expected to get here
        }

        // Check the size of the DriversList to make sure it wasn't modified
        assertEquals(8, driversList.size());
    }

    @Test
    public void addAll() {
        // Create a new list of drivers with one confirmed driver to add to the collection
        final ArrayList<Driver> drivers1 = new ArrayList<>();
        final Driver d1 = new Driver();
        final Driver d2 = new Driver();
        final Driver d3 = new Driver();
        d1.setStatus(RequestState.CONFIRMED);
        d2.setStatus(RequestState.DECLINED);
        d3.setStatus(RequestState.CANCELLED);
        drivers1.add(d1);
        drivers1.add(d2);
        drivers1.add(d3);

        // Check the initial size of the DriversList
        assertEquals(7, driversList.size());

        try {
            driversList.addAll(drivers1);
        }
        catch (DriversList.ConfirmedDriverAlreadyExistsException e) {
            fail("addAll() should not throw");
        }

        // Check the new size of the DriversList
        assertEquals(10, driversList.size());

        // Create a new list of drivers with one confirmed driver to add to the collection
        final ArrayList<Driver> drivers2 = new ArrayList<>();
        final Driver d4 = new Driver();
        final Driver d5 = new Driver();
        final Driver d6 = new Driver();
        d4.setStatus(RequestState.CONFIRMED);
        d5.setStatus(RequestState.DECLINED);
        d6.setStatus(RequestState.CANCELLED);
        drivers2.add(d4);
        drivers2.add(d5);
        drivers2.add(d6);

        try {
            driversList.addAll(drivers2);
            fail("Expected addAll() to throw");
        }
        catch (DriversList.ConfirmedDriverAlreadyExistsException e) {
            // Expected to get here
        }

        // Check the size of the DriversList to make sure it wasn't modified
        assertEquals(10, driversList.size());
    }

    /**
     * The the remove by index.
     */
    @Test
    public void remove() {
        // Test the index out of bounds
        try {
            driversList.remove(7);
            fail("Expected remove() to throw");
        }
        catch (IndexOutOfBoundsException e) {
            // Expected to get here
        }

        // Test removing a driver
        assertTrue(driversList.contains(driver5));
        driversList.remove(4);
        assertFalse(driversList.contains(driver5));
    }

    /**
     * Test remove by object.
     */
    @Test
    public void removeDriver() {
        // Removing a driver that does not exist in the collection
        final Driver d = new Driver();
        assertEquals(7, driversList.size());
        driversList.remove(d);
        assertEquals(7, driversList.size());

        // Removing a driver that does exist in the collection
        assertEquals(7, driversList.size());
        driversList.remove(driver3);
        assertEquals(6, driversList.size());
    }



    @Test
    public void clear() {
        assertEquals(7, driversList.size());
        driversList.clear();
        assertEquals(0, driversList.size());
    }

    @Test
    public void contains() {
        // A driver that does not exist in the collection
        final Driver d = new Driver();
        assertFalse(driversList.contains(d));

        // A driver that should exist in the collection
        assertTrue(driversList.contains(driver7));
    }

    @Test
    public void isEmpty() {
        assertFalse(driversList.isEmpty());
        driversList.clear();
        assertTrue(driversList.isEmpty());
    }

    @Test
    public void size() {
        assertEquals(7, driversList.size());
        assertEquals(0, new DriversList().size());
    }

    @Test
    public void get() {
        Driver d;

        // Test the index out of bounds
        try {
            d = driversList.get(7);
            fail("Expected get() to throw");
        }
        catch (IndexOutOfBoundsException e) {
            // Expected to get here
        }

        // Test getting a driver
        d = driversList.get(4);
        assertEquals(driver5, d);
    }

    @Test
    public void iterator() {
        int count = 0;
        for (Driver d : driversList)
            count++;
        assertEquals(7, count);
    }

    @Test
    public void getByStatus() {
        DriversList ds = driversList.getByStatus(RequestState.CANCELLED);
        assertEquals(3, ds.size());
        for (Driver d : ds)
            assertEquals(RequestState.CANCELLED, d.getStatus());
    }

    @Test
    public void hasConfirmedDriver() {
        assertFalse(driversList.hasConfirmedDriver());

        final Driver d = new Driver();
        d.setStatus(RequestState.CONFIRMED);
        driversList.add(d);

        assertTrue(driversList.hasConfirmedDriver());
    }

    @Test
    public void getConfirmedDriverOrDefault() {
        assertEquals(null, driversList.getConfirmedDriverOrDefault(null));

        final Driver d = new Driver();
        d.setStatus(RequestState.CONFIRMED);
        driversList.add(d);

        assertEquals(d, driversList.getConfirmedDriverOrDefault(null));
    }

    @Test
    public void toArray() {
        final Driver[] a = driversList.toArray();
        assertEquals(7, driversList.size());
        assertEquals(driversList.size(), a.length);
        for (int i = 0; i < 7; i++)
            assertEquals(driversList.get(i), a[i]);
    }
}

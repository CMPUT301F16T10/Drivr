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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class for staring a list of drivers. It is used for tracking the drivers for a request, and
 * enforces the nice property that at any given time at most one driver can have the status of
 * confirmed.
 * @see Driver
 * @see RequestState
 */
public class DriversList implements Iterable<Driver> {

    /**
     * The list of drivers in the collection.
     */
    private ArrayList<Driver> drivers;

    /**
     * Instantiate a new DriversList.
     */
    DriversList() {
        drivers = new ArrayList<>();
    }

    /**
     * Add a driver to the list. First it is checked to make sure that the driver can be added to
     * the list and doesn't break the property that at most one driver can be confirmed.
     * @param d The driver to add.
     * @throws ConfirmedDriverAlreadyExistsException
     */
    public void add(Driver d) throws ConfirmedDriverAlreadyExistsException {
        if (d.getStatus() == RequestState.CONFIRMED && hasConfirmedDriver())
            throw new ConfirmedDriverAlreadyExistsException();
        drivers.add(d);
    }

    /**
     * Adds all drivers to the list. First it is checked to make sure that the driver can be added to
     * the list and doesn't break the property that at most one driver can be confirmed.
     * @param other The drivers to add to the list.
     * @throws ConfirmedDriverAlreadyExistsException
     */
    public void addAll(Collection<Driver> other) throws ConfirmedDriverAlreadyExistsException {
        int count = 0;
        if (hasConfirmedDriver())
            count++;
        for (Driver d : other)
            if (d.getStatus() == RequestState.CONFIRMED)
                count++;
        if (count > 1)
            throw new ConfirmedDriverAlreadyExistsException();
        drivers.addAll(other);
    }

    /**
     * Removes the driver at a certain index from the collection.
     * @param i The index.
     * @throws IndexOutOfBoundsException
     */
    public void remove(int i) throws IndexOutOfBoundsException {
        try {
            drivers.remove(i);
        }
        catch (IndexOutOfBoundsException e) {
            throw e;
        }
    }

    /**
     * Removes a driver from the collection if it exists.
     * @param d
     */
    public void remove(Driver d) {
        drivers.remove(d);
    }

    /**
     * Removes all drivers from the list.
     */
    public void clear() {
        drivers.clear();
    }

    /**
     * Checks if a driver has membership in the collection.
     * @param d The driver.
     * @return True if the collection has the driver, false otherwise.
     */
    public boolean contains(Driver d) {
        return drivers.contains(d);
    }

    /**
     * Checks if the collection is empty.
     * @return True if the collection is empty, false otherwise.
     */
    public boolean isEmpty() {
        return drivers.isEmpty();
    }

    /**
     * Get the number of drivers in the collection.
     * @return The number of drivers.
     */
    public int size() {
        return drivers.size();
    }

    /**
     * Get a driver at an index.
     * @param i The index.
     * @return The driver.
     * @throws IndexOutOfBoundsException
     */
    public Driver get(int i) throws IndexOutOfBoundsException {
        try {
            return drivers.get(i);
        }
        catch (IndexOutOfBoundsException e) {
            throw e;
        }
    }

    /**
     * Get an iterator over the collection.
     * @return The iterator.
     */
    public Iterator<Driver> iterator() {
        return drivers.iterator();
    }

    /**
     * From this collection, creates a new collection of drivers with a given status.
     * @param state The status to collect by.
     * @return A collection of drivers.
     */
    public DriversList getByStatus(RequestState state) {
        final DriversList list = new DriversList();
        for (Driver d : drivers)
            if (d.getStatus() == state)
                list.add(d);
        return list;
    }

    /**
     * Test whether the collection has a confirmed driver.
     * @return True if there is a confirmed driver, false otherwise.
     */
    public boolean hasConfirmedDriver() {
        for (Driver d: drivers)
            if (d.getStatus() == RequestState.CONFIRMED)
                return true;
        return false;
    }


    /**
     * Test whether the collection has only accepted drivers.
     * @return True if all drivers have an accepted state, false otherwise.
     */
    public boolean hasOnlyAcceptedDrivers() {
        for (Driver d: drivers)
            if (d.getStatus() != RequestState.ACCEPTED)
                return false;
        return true;
    }

    /**
     * Gets the confirmed driver if it exists, otherwise return the default value.
     * @param def The default value.
     * @return The confirmed driver or the default value.
     */
    public Driver getConfirmedDriverOrDefault(Driver def) {
        if (hasConfirmedDriver()) {
            for (Driver d : drivers)
                if (d.getStatus() == RequestState.CONFIRMED)
                    return d;
        }
        return def;
    }

    /**
     * Get the collection as an array of drivers.
     * @return An array of drivers.
     */
    public Driver[] toArray() {
        return drivers.toArray(new Driver[0]);
    }

    /**
     * An exception to be thrown on operations that would break the property of there being at most
     * one confirmed driver.
     */
    public class ConfirmedDriverAlreadyExistsException extends RuntimeException {
        public ConfirmedDriverAlreadyExistsException() {}
        public ConfirmedDriverAlreadyExistsException(String message) { super(message); }
        public ConfirmedDriverAlreadyExistsException(Throwable cause) { super(cause); }
        public ConfirmedDriverAlreadyExistsException(String message, Throwable cause) { super(message, cause); }
    }
}

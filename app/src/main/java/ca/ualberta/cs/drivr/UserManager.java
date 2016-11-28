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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Observable;

/**
 * A (singleton) model class for providing access to application data.
 */
public class UserManager extends Observable implements IUserManager {

    private static final String SAVE_FILENAME_USER = "UserManager.User.dat";
    private static final String SAVE_FILENAME_REQUESTS = "UserManager.RequestsList.dat";

    /**
     * The current user.
     */
    private User user;

    /**
     * The current user mode.
     */
    private UserMode userMode;

    /**
     * The user's requests.
     */
    private RequestsList requestsList;

    /**
     * A connectivity manager instance.
     */
    private ConnectivityManager connectivityManager;

    /**
     * A singleton instance.
     */
    private static UserManager instance;

    private static void loadUser() {
        try {
            if (instance == null)
                instance = new UserManager();
            FileInputStream fis = App.getContext().openFileInput(SAVE_FILENAME_USER);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            instance.user = gson.fromJson(in, User.class);
            if (instance == null)
                throw new FileNotFoundException();
        }
        catch (FileNotFoundException e) {
            instance.user = new User();
        }
    }

    private static void loadRequests() {
        try {
            if (instance == null)
                instance = new UserManager();
            FileInputStream fis = App.getContext().openFileInput(SAVE_FILENAME_REQUESTS);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            instance.requestsList = gson.fromJson(in, RequestsList.class);
            if (instance == null)
                throw new FileNotFoundException();
        }
        catch (FileNotFoundException e) {
            instance.requestsList = new RequestsList();
        }
    }

    private static void saveUser() {
        try {
            FileOutputStream fos = App.getContext().openFileOutput(SAVE_FILENAME_USER, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            gson.toJson(instance.user, writer);
            writer.flush();
        }
        catch (IOException e) {
            // Do nothing
        }
    }

    private static void saveRequests() {
        try {
            FileOutputStream fos = App.getContext().openFileOutput(SAVE_FILENAME_REQUESTS, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            gson.toJson(instance.requestsList, writer);
            writer.flush();
        }
        catch (IOException e) {
            // Do nothing
        }
    }

    /**
     * Get access to the singleton instance.
     * @return The singleton instance.
     */
    public static UserManager getInstance() {
        if (instance == null) {
            loadUser();
            loadRequests();
        }
        return instance;
    }

    /**
     * Instantiate a new user manager.
     */
    protected UserManager() {
        user = new User();
        userMode = UserMode.RIDER;
        requestsList = new RequestsList();
    }

    /**
     * Get the current user.
     * @return The current user.
     */
    @Override
    public User getUser() {
        return user;
    }

    /**
     * Set the current user.
     * @param user The current user.
     */
    @Override
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get the list of requests.
     * @return The requests list.
     */
    @Override
    public RequestsList getRequestsList() {
        return requestsList;
    }

    /**
     * Set the list of requests.
     * @param requestsList The requests list.
     */
    @Override
    public void setRequestsList(RequestsList requestsList) {
        this.requestsList = requestsList;
    }

    /**
     * Get the current user mode.
     * @return The current user mode.
     */
    @Override
    public UserMode getUserMode() {
        return userMode;
    }

    /**
     * Set the user mode.
     * @param userMode The user mode.
     */
    @Override
    public void setUserMode(UserMode userMode) {
        this.userMode = userMode;
    }

    /**
     * Get the connectivity manager.
     * @return The connectivity manager.
     */
    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }

    /**
     * Set the connectivity manager.
     * @param connectivityManager The connectivity manager.
     */
    public void setConnectivityManager(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    /**
     * Notifies all observers of this object that the object has changed. This forces the object
     * into a changed state so all observers will see that the object has changed on every call to
     * this method.
     */
    @Override
    public void notifyObservers() {
        setChanged();
        saveUser();
        saveRequests();
        super.notifyObservers();
    }
}

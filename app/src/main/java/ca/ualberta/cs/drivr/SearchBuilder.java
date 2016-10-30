package ca.ualberta.cs.drivr;

import android.os.AsyncTask;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

/**
 * Created by colton on 2016-10-23.
 * Modified by tiegan on 2016-10-30.
 */

/**
 * We're searching for one of four things: Location by geolocation, location by search request,
 * user profile (no specific search for user - they just click name and that name is used) and
 * requests themselves either taken on by the driver or given by the rider.
 *
 * (Although, as I mentioned in the comments of the ES class, location could probably just be used
 * for both searches and geolocation.)
 *
 * As of right now, it's going to use a lot of stuff from Lab 7 heavily. Whether or not this
 * changes is TBD.
 */

public class SearchBuilder {
    private static JestDroidClient client;

    // Search for a location by geolocation (Location used to get geo-coordinates)
    //TODO: Actually work on it.
    public static class searchForLocationRequests extends
            AsyncTask<android.location.Location, Void, Void> {
        @Override
        protected Void doInBackground(android.location.Location... searchTerm) {
            throw new UnsupportedOperationException();
        }
    }

    // Search for a user profile (String used to get unique username).
    //TODO: Actually work on it.
    public static class searchForUser extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... username) {
            throw new UnsupportedOperationException();
        }
    }

    // Search for requests associated with user.
    //TODO: Actually work on it.
    public static class searchForRequests extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... username) {
            throw new UnsupportedOperationException();
        }
    }

    //TODO: Fix where we're getting our Elasticsearch from.
    public static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig
                    .Builder("TBD");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}

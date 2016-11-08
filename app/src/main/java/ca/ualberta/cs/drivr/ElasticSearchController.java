package ca.ualberta.cs.drivr;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Update;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

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

public class ElasticSearchController {
    private static JestDroidClient client;

    // Enters a request into ElasticSearch
    //TODO: Fix index, result.isSucceeded()
    public static class AddRequest extends
            AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();
            for(Request request: requests) {
                String add = "";
                Index index = new Index.Builder(add).index("--").type("request").build();
                try {
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded()) {
                        continue;
                    } else {
                        Log.i("Error", "Failed to insert the request into elastic search.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the requests.");
                }

            }
            return null;
        }
    }

    // Enters user into ElasticSearch
    //TODO: Actually work on it.
    public static class AddUser extends
            AsyncTask<User, Void, Void> {
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();
            for(User user: users) {
                Index index = new Index.Builder(user).index("--").type("user").build();
                try {
                    DocumentResult result = client.execute(index);
                    if(result.isSucceeded()) {
                        continue;
                    } else {
                        Log.i("Error", "Failed to insert the request into elastic search.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the requests.");
                }

            }
            return null;
        }
    }

    // Updates a request in ElasticSearch
    //TODO: Actually work on it.
    public static class UpdateRequest extends
            AsyncTask<Request, Void, Void> {
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();
            for(Request request: requests) {
                Update update = new Update.Builder(request).index("--").type("request").build();
                try {
                    DocumentResult result = client.execute(update);
                    if(result.isSucceeded()) {
                        continue;
                    } else {
                        Log.i("Error", "Failed to insert the request into elastic search.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the requests.");
                }

            }
            return null;
        }
    }

    // Search for a location by geolocation (Location used to get geo-coordinates)
    //TODO: Actually work on it.
    public static class SearchForLocationRequests extends
            AsyncTask<android.location.Location, Void, Void> {
        @Override
        protected Void doInBackground(android.location.Location... geolocation) {
            verifySettings();
            throw new UnsupportedOperationException();
        }
    }

    // Search for a user profile (String used to get unique username).
    //TODO: Actually work on it.
    public static class GetUser extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... username) {
            verifySettings();
            User user = new User();
            String search_string = "{\"query_string\" : {\"username\" : \"" + username + "\"}}";

            // Do same thing done with AddTweetsTask
            Search search = new Search.Builder(search_string)
                        .addIndex("---")
                        .addType("User")
                        .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    user = result.getSourceAsObject(User.class);
                } else {
                    Log.i("Error", "The search executed but it didn't work.");
                }
            } catch (Exception e) {
                Log.i("Error", "Executing the get user method failed.");
            }

            return user;
        }
    }

    // Search for requests associated with user.
    //TODO: Actually work on it.
    public static class SearchForRequests extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... username) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            //Need to differentiate between rider and driver.
            //oooooorrrrrrrrrr we just do the two queries together, since that's what you'd need to do, right?
            String search_string = "{\"query_string\" : {\"username\" : \"" + username + "\"}}";

            // Do same thing done with AddTweetsTask
            Search search = new Search.Builder(search_string)
                    .addIndex("---")
                    .addType("Requests")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<Request> foundRequests = result
                            .getSourceAsObjectList(Request.class);
                    requests.addAll(foundRequests);
                } else {
                    Log.i("Error", "The search executed but it didn't work.");
                }
            } catch (Exception e) {
                Log.i("Error", "Executing the get user method failed.");
            }

            return requests;
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

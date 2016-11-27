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

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * In ElasticSearchController, the actual calls to ElasticSearch are done. This requires the use of
 * internal classes that use asynchronous tasks in order to perform these calls, as attempts at not
 * using them have resulted in ElasticSearch not working.
 *
 * The controller contains 5 classes for requests and 2 for users. For requests, it can add a
 * request, update it, search for requests by username, by location or by keyword. Requests are
 * stored in a document that doesn't translate 1-to-1 with Request, requiring the use of a model
 * class named ElasticSearchRequest used solely in this class and solely just for allowing Request
 * to be gotten from ElasticSearch
 *
 * For users, it adds and updates users using the same class, and it can also search for a user.
 *
 * @author Tiegan Bonowicz
 * @see ElasticSearch
 * @see ElasticSearchRequest
 */

public class ElasticSearchController {
    private static JestDroidClient client;

    /**
     * Here, a request is added to ElasticSearch by first building the add query for the document
     * to be stored in ElasticSearch, then adding it into ElasticSearch, then immediately afterwards
     * getting the ID where it's stored, appending it to the end of the add query and then replacing
     * the query we just added with the new query including the ID.
     */
    public static class AddRequest extends AsyncTask<Request, Void, Void> {

        /**
         * Add query:
         * {
         *     "rider": "rider", (in Request Rider)
         *     "driver": [{
         *         "username": "username", (in Request Driver)
         *         "status": "status" (in Request Driver)
         *     }, ...],
         *     "status": "status", (in Request)
         *     "description": "description", (in Request)
         *     "fare": fare, (in Request)
         *     "date": "date", (in Request)
         *     "km": km (in Request)
         *     "sourceAddress": "sourceAddress", (in Request SourcePlace)
         *     "start": [ startLongitude, startLatitude], (in Request SourcePlace)
         *     "destinationAddress": "destinationAddress", (in Request DestinationPlace)
         *     "end": [ endLongitude, endLatitude], (in Request DestinationPlace)
         *     "id": "id" (gotten from ElasticSearch after first add)
         * }
         *
         * @param requests The request given by the user.
         * @return null
         */
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();
            for (Request request: requests) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String addedDate = format.format(request.getDate());

                String add = "{" +
                        "\"rider\": \"" + request.getRider().getUsername() + "\"," +
                        "\"driver\": [";

                for (int i = 0; i < request.getDrivers().size(); i++) {
                    Driver driver = request.getDrivers().get(i);
                    add = add + "{\"username\": \"" + driver.getUsername() +
                            "\", \"status\": \"" + driver.getStatus() + "\"}";
                    if (i != request.getDrivers().size() - 1) {
                        add += ", ";
                    }
                }

                add += "]," +
                        "\"status\": \"" + request.getRequestState().toString() + "\"," +
                        "\"description\": \"" + request.getDescription() + "\"," +
                        "\"fare\": " + request.getFareString() + " ," +
                        "\"date\": \"" + addedDate + "\"," +
                        "\"km\": " + request.getKm() + " , " +
                        "\"sourceAddress\": \"" + request.getSourcePlace().getAddress() + "\", " +
                        "\"start\": [" +
                        Double.toString(request.getSourcePlace().getLatLng().longitude) + ", " +
                        Double.toString(request.getSourcePlace().getLatLng().latitude) + "]," +
                        "\"destinationAddress\": \"" + request.getDestinationPlace().getAddress() +
                        "\", \"end\": [" +
                        Double.toString(request.getDestinationPlace().getLatLng().longitude) +
                        ", " + Double.toString(request.getDestinationPlace().getLatLng().latitude) +
                        "]";

                Index index = new Index.Builder(add + "}")
                        .index("drivr")
                        .type("requests")
                        .build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        request.setId(result.getId());
                        add += ", \"id\": \"" + request.getId() + "\" }";
                        index = new Index.Builder(add)
                                .index("drivr")
                                .type("requests")
                                .id(request.getId())
                                .build();

                        try {
                            result = client.execute(index);
                            if(!result.isSucceeded()) {
                                Log.i("Error", "Failed to insert the request into elastic search.");
                            }
                        }
                        catch (Exception e) {
                            Log.i("Error", "The application failed to build and send the " +
                                    "requests.");
                        }
                    }
                    else {
                        Log.i("Error", "Failed to insert the request into elastic search.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the requests.");
                }

            }
            return null;
        }
    }

    /**
     * Here, a request that is not pending is updated.
     */
    public static class UpdateRequest extends AsyncTask<Request, Void, Void> {

        /**
         * Add query:
         * {
         *     "rider": "rider" (in Request)
         *     "driver": [{
         *         "username": "username", (in Request.Driver)
         *         "status": "status" (in Request.Driver)
         *     }, ...],
         *     "status": "status", (in Request)
         *     "description": "description" (in Request)
         *     "fare": fare (in Request)
         *     "date": "date" (in Request)
         *     "km": km (in Request)
         *     "sourceAddress": "sourceAddress", (in Request SourcePlace)
         *     "start": [ startLongitude, startLatitude], (in Request SourcePlace)
         *     "destinationAddress": "destinationAddress", (in Request DestinationPlace)
         *     "end": [ endLongitude, endLatitude], (in Request DestinationPlace)
         *     "id": "id" (in Request)
         * }
         *
         * @param requests The request given by the user.
         * @return null
         */
        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();
            for (Request request: requests) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                String addedDate = format.format(request.getDate());

                String add = "{" +
                        "\"rider\": \"" + request.getRider().getUsername() + "\"," +
                        "\"driver\": [";

                for (int i = 0; i < request.getDrivers().size(); i++) {
                    Driver driver = request.getDrivers().get(i);
                    add = add + "{\"username\": \"" + driver.getUsername() +
                            "\", \"status\": \"" + driver.getStatus() + "\"}";
                    if (i != request.getDrivers().size() - 1)
                        add += ", ";
                }

                add += "]," +
                        "\"status\": \"" + request.getRequestState().toString() + "\"," +
                        "\"description\": \"" + request.getDescription() + "\"," +
                        "\"fare\": " + request.getFareString() + " ," +
                        "\"date\": \"" + addedDate + "\"," +
                        "\"sourceAddress\": \"" + request.getSourcePlace().getAddress() + "\", " +
                        "\"km\": " + request.getKm() + " , " +
                        "\"start\": [" +
                        Double.toString(request.getSourcePlace().getLatLng().longitude) + ", " +
                        Double.toString(request.getSourcePlace().getLatLng().latitude) + "]," +
                        "\"destinationAddress\": \"" + request.getDestinationPlace().getAddress() +
                        "\", \"end\": [" +
                        Double.toString(request.getDestinationPlace().getLatLng().longitude) +
                        ", " + Double.toString(request.getDestinationPlace().getLatLng().latitude) +
                        "], \"id\": \"" + request.getId() + "\"}";

                Index index = new Index.Builder(add)
                        .index("drivr")
                        .type("requests")
                        .id(request.getId())
                        .build();

                try {
                    DocumentResult result = client.execute(index);
                    if(!result.isSucceeded())
                        Log.i("Error", "Failed to insert the request into elastic search.");
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the requests.");
                }

            }
            return null;
        }
    }

    /**
     * Here, all open requests will be gotten from ElasticSearch.
     */
    public static class SearchForOpenRequests
            extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * First search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "match":
         *         {
         *             "status": "Accepted"
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         *
         * Second search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "match":
         *         {
         *             "status": "Pending"
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * @param nothings Mandatory blank string.
         * @return The ArrayList of matching requests
         */
        @Override
        protected ArrayList<Request> doInBackground(String... nothings) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for (String nothing: nothings) {
                String search_string = "{\"from\": 0, \"size\": 10000, "
                        + "\"query\": {\"match\": {\"status\": \"Accepted\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by keyword failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, "
                        + "\"query\": {\"match\": {\"status\": \"pending\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by keyword failed.");
                }

            }
            return requests;
        }
    }

    /**
     * Here, all the open requests that have the description containing the keyword given by the
     * user will be gotten.
     */
    public static class SearchForKeywordRequests
            extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * First search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "description": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "accepted"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Second search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "description": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "pending"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * @param keywords The keyword specified by the user
         * @return The ArrayList of matching requests
         */
        @Override
        protected ArrayList<Request> doInBackground(String... keywords) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for (String keyword: keywords) {
                String search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"description\": \"" + keyword + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"accepted\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by keyword failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"description\": \"" + keyword + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"pending\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by keyword failed.");
                }
            }
            return requests;
        }
    }

    /**
     * Here, the program will get take in the specified geolocation from the user, search within a
     * 5 km radius for open requests where the start and destination locations are within that range
     * and return the gotten requests.
     */
    public static class SearchForGeolocationRequests
            extends AsyncTask<Location, Void, ArrayList<Request>> {

        /**
         * First search query:
         * {
         *     "from": 0, "size": 10000, "filter":
         *     {
         *        "geo_distance":
         *         {
         *             "distance": "5km",
         *             "start": [geolocation Longitude, geolocation Latitude]
         *         }
         *     },
         *     "query":
         *     {
         *         "match":
         *         {
         *             "status": "Accepted"
         *         }
         *     }
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Second search query:
         * {
         *     "from": 0, "size": 10000, "filter":
         *     {
         *        "geo_distance":
         *         {
         *             "distance": "5km",
         *             "start": [geolocation Longitude, geolocation Latitude]
         *         }
         *     },
         *     "query":
         *     {
         *         "match":
         *         {
         *             "status": "Pending"
         *         }
         *     }
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Third search query:
         * {
         *     "from": 0, "size": 10000, "filter":
         *     {
         *        "geo_distance":
         *         {
         *             "distance": "5km",
         *             "end": [geolocation Longitude, geolocation Latitude]
         *         }
         *     },
         *     "query":
         *     {
         *         "match":
         *         {
         *             "status": "Accepted"
         *         }
         *     }
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Fourth search query:
         * {
         *     "from": 0, "size": 10000, "filter":
         *     {
         *        "geo_distance":
         *         {
         *             "distance": "5km",
         *             "end": [geolocation Longitude, geolocation Latitude]
         *         }
         *     },
         *     "query":
         *     {
         *         "match":
         *         {
         *             "status": "Pending"
         *         }
         *     }
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * @param geolocations The location given by the user.
         * @return The ArrayList of matching requests
         */
        @Override
        protected ArrayList<Request> doInBackground(Location... geolocations) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for (Location geolocation : geolocations) {
                String search_string = "{\"from\": 0, \"size\": 10000, \"filter\": "
                        + "{ \"geo_distance\": "
                        + "{ \"distance\": \"5km\", \"start\": ["
                        + Double.toString(geolocation.getLongitude()) + ", "
                        + Double.toString(geolocation.getLatitude()) + "]}}, " +
                        "\"query\": {\"match\": {\"status\": \"Accepted\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by geolocation failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"filter\": "
                        + "{ \"geo_distance\": "
                        + "{ \"distance\": \"5km\", \"start\": ["
                        + Double.toString(geolocation.getLongitude()) + ", "
                        + Double.toString(geolocation.getLatitude()) + "]}}, " +
                        "\"query\": {\"match\": {\"status\": \"Pending\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by geolocation failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"filter\": "
                        + "{ \"geo_distance\": "
                        + "{ \"distance\": \"5km\", \"end\": ["
                        + Double.toString(geolocation.getLongitude()) + ", "
                        + Double.toString(geolocation.getLatitude()) + "]}}, " +
                        "\"query\": {\"match\": {\"status\": \"Accepted\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by geolocation failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"filter\": "
                        + "{ \"geo_distance\": "
                        + "{ \"distance\": \"5km\", \"end\": ["
                        + Double.toString(geolocation.getLongitude()) + ", "
                        + Double.toString(geolocation.getLatitude()) + "]}}, " +
                        "\"query\": {\"match\": {\"status\": \"Pending\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by geolocation failed.");
                }
            }
            return requests;
        }
    }

    /**
     * Here, the open requests associated to a specific location are gotten from ElasticSearch
     * and returned.
     */
    public static class SearchForLocationRequests
            extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * First search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "sourceAddress": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "accepted"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Second search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "sourceAddress": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "pending"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Third search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "destinationAddress": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "accepted"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Fourth search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "filtered":
         *         {
         *             "query":
         *             {
         *                 "match":
         *                 {
         *                     "destinationAddress": "keyword" (given by user)
         *                 },
         *             },
         *             "filter":
         *             {
         *                 "term":
         *                 {
         *                     "status": "pending"
         *                 }
         *             }
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * @param locations The location given by the user.
         * @return The ArrayList of requests containing that location.
         */
        @Override
        protected ArrayList<Request> doInBackground(String... locations) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for (String location: locations) {
                String search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"sourceAddress\": \"" + location + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"accepted\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by location failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"sourceAddress\": \"" + location + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"pending\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by location failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"destinationAddress\": \"" + location + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"accepted\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by location failed.");
                }

                search_string = "{\"from\": 0, \"size\": 10000, \"query\": " +
                        "{\"filtered\": {\"query\": {\"match\": " +
                        "{\"destinationAddress\": \"" + location + "\"}}, " +
                        "\"filter\": {\"term\": {\"status\": \"pending\"}}}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests by location failed.");
                }
            }
            return requests;
        }
    }

    /**
     * Here, all requests associated to a user are gotten by checking all rider and driver usernames
     * to see if they contain the user's username, adding them to the ArrayList and then returning
     * that ArrayList.
     */
    public static class SearchForRequests extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * First search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "match":
         *         {
         *             "rider": "username" (given by user)
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * Second search query:
         * {
         *     "from": 0, "size": 10000, "query":
         *     {
         *         "match":
         *         {
         *             "driver.username": "username" (given by user)
         *         }
         *     },
         *     "sort":
         *     [{
         *         "date":
         *         {
         *             "order": "desc"
         *         }
         *     }]
         * }
         *
         * @param usernames The username given by the user.
         * @return The ArrayList of matching requests
         */
        @Override
        protected ArrayList<Request> doInBackground(String... usernames) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for (String username: usernames) {
                String search_string = "{\"from\": 0, \"size\": 10000, "
                        + "\"query\": {\"match\": " +
                        "{\"rider\": \"" + username + "\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                        tempRequests.clear();
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests failed.");
                }


                search_string = "{\"from\": 0, \"size\": 10000, "
                        + "\"query\": {\"match\": " +
                        "{\"driver.username\": \"" + username + "\"}}, " +
                        "\"sort\": [{\"date\": {\"order\": \"desc\"}}]}";

                search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for requests failed.");
                }
            }

            return requests;
        }
    }

    /**
     * Here, we get a specified request by searching with the request ID and returning it when we
     * find it.
     */
    public static class GetRequest extends AsyncTask<String, Void, Request> {

        /**
         * Search query:
         * {
         *     "query":
         *     {
         *         "match":
         *         {
         *             "id": "id" (given by user)
         *         }
         *     }
         * }
         * @param ids Request to be gotten by ID.
         * @return The request matching the given ID.
         */
        @Override
        protected Request doInBackground(String... ids) {
            verifySettings();
            ArrayList<Request> requests = new ArrayList<Request>();
            ArrayList<ElasticSearchRequest> tempRequests = new ArrayList<ElasticSearchRequest>();
            for(String id: ids) {
                String search_string = "{\"query\": {\"match\": "
                        + "{\"id\": \"" + id + "\" }}}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("requests")
                        .build();

                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<ElasticSearchRequest> foundRequests = result
                                .getSourceAsObjectList(ElasticSearchRequest.class);
                        tempRequests.addAll(foundRequests);
                        addRequests(requests, tempRequests);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing search for request by ID failed.");
                    return null;
                }
            }

            return requests.get(0);
        }
    }

    /**
     * Here, the user is either added or updated (since this class handles both) in ElasticSearch
     * by building it then adding it in the ID which is the same as the user's unique username.
     */
    public static class AddUser extends AsyncTask<User, Void, Void> {

        /**
         * Add query:
         * {
         *     "name": "name", (in User)
         *     "username": "username", (in User)
         *     "email": "email", (in User)
         *     "phoneNumber": "phoneNumber" (in User)
         * }
         *
         * @param users The user to be added.
         * @return null
         */
        @Override
        protected Void doInBackground(User... users) {
            verifySettings();
            for (User user: users) {
                String addUser = "{\"name\": \"" + user.getName() + "\", " +
                        "\"username\": \"" + user.getUsername() + "\", " +
                        "\"email\": \"" + user.getEmail() + "\", " +
                        "\"phoneNumber\": \"" + user.getPhoneNumber() + "\", " +
                        "\"description\": \"" + user.getVehicleDescription() + "\", " +
                        "\"rating\": " + Double.toString(user.getRating()) + ", " +
                        "\"totalRatings\": " + Integer.toString(user.getTotalRatings()) + "}";

                Index index = new Index.Builder(addUser)
                        .index("drivr")
                        .type("user")
                        .id(user.getUsername())
                        .build();

                try {
                    Log.i("User", addUser);
                    Log.i("index", index.toString());
                    DocumentResult result = client.execute(index);
                    if(!result.isSucceeded())
                        Log.i("Error", "Failed to insert the user into elastic search.");
                }
                catch (Exception e) {
                    Log.i("Error", "The application failed to build and send the users.");
                }
            }
            return null;
        }
    }

    /**
     * Here, we get a user profile by searching for the unique username and returning it when we
     * find it.
     */
    public static class GetUser extends AsyncTask<String, Void, User> {

        /**
         * Search query:
         * {
         *     "query":
         *     {
         *         "match":
         *         {
         *             "username": "username" (given by user)
         *         }
         *     }
         * }
         * @param usernames Username to be gotten.
         * @return The user matching the username
         */
        @Override
        protected User doInBackground(String... usernames) {
            verifySettings();
            User user = new User();
            for(String username: usernames) {
                String search_string = "{\"query\": {\"match\": "
                        + "{\"username\": \"" + username + "\" }}}";

                Search search = new Search.Builder(search_string)
                        .addIndex("drivr")
                        .addType("user")
                        .build();

                try {
                    Log.i("User", search_string);
                    Log.i("test", search.toString());
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        Log.i("uh", Integer.toString(result.getTotal()));
                        List<User> foundUsers = result
                                .getSourceAsObjectList(User.class);
                        user = foundUsers.get(foundUsers.size()-1);
                    }
                    else {
                        Log.i("Error", "The search executed but it didn't work.");
                    }
                }
                catch (Exception e) {
                    Log.i("Error", "Executing the get user method failed.");
                    user = null;
                }
            }

            return user;
        }
    }

    /**
     * Used strictly in testing, deletes a user in the specified ID.
     */
    public static class DeleteUser extends AsyncTask<String, Void, Void> {

        /**
         * @param ids The user to be deleted.
         * @return null
         */
        @Override
        protected Void doInBackground(String... ids) {
            verifySettings();
            for(String id: ids) {
                try {
                    client.execute(new Delete.Builder("1")
                            .index("drivr")
                            .type("user")
                            .id(id)
                            .build());
                }
                catch (Exception e) {
                    Log.i("Error", "Executing the delete user method failed.");
                }
            }

            return null;
        }
    }

    /**
     * Used strictly in testing, deletes a request in the specified ID.
     */
    public static class DeleteRequest extends AsyncTask<String, Void, Void> {

        /**
         * @param ids The user to be deleted.
         * @return null
         */
        @Override
        protected Void doInBackground(String... ids) {
            verifySettings();
            for(String id: ids) {
                try {
                    client.execute(new Delete.Builder("1")
                            .index("drivr")
                            .type("requests")
                            .id(id)
                            .build());
                }
                catch (Exception e) {
                    Log.i("Error", "Executing the delete request method failed.");
                }
            }

            return null;
        }
    }

    /**
     * Here, we set our client settings to make sure it's on the correct website where our
     * ElasticSearch results are stored.
     */
    private static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig
                    .Builder("http://cmput301.softwareprocess.es:8080/");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

    /**
     * Here, the requests gotten from ElasticSearch will be converted to actual requests that can
     * be stored within the app.
     *
     * @param requests The requests to be returned to the rest of the app
     * @param tempRequests The requests gotten from ElasticSearch.
     */
    private static void addRequests(ArrayList<Request> requests,
                                    ArrayList<ElasticSearchRequest> tempRequests) {
        boolean repeat = false;

        for (int i = 0; i < tempRequests.size(); i++) {
            ElasticSearchRequest gottenRequest = tempRequests.get(i);

            for(int j = 0; j < requests.size(); j++) {
                if(gottenRequest.getId().equals(requests.get(j).getId())) {
                    repeat = true;
                    break;
                }
            }

            if(repeat) {
                repeat = false;
                continue;
            }

            Request request = new Request();

            User tempUser = new User();
            tempUser.setUsername(gottenRequest.getRider());
            request.setRider(tempUser);
            request.getDrivers().addAll(gottenRequest.getDrivers());

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date;
            try {
                date = format.parse(gottenRequest.getDate());
            }
            catch (Exception e) {
                date = new Date();
            }

            request.setRequestState(RequestState.valueOf(gottenRequest.getStatus().toUpperCase()));
            request.setDescription(gottenRequest.getDescription());
            request.setDate(date);
            request.setFare(new BigDecimal(gottenRequest.getFare()));
            request.setKm(gottenRequest.getKm());
            request.setId(gottenRequest.getId());

            ConcretePlace temp = new ConcretePlace();
            temp.setAddress(gottenRequest.getSourceAddress());
            temp.setLatLng(new LatLng(gottenRequest.getStart()[1], gottenRequest.getStart()[0]));
            request.setSourcePlace(temp);

            ConcretePlace temp2 = new ConcretePlace();
            temp2.setAddress(gottenRequest.getDestinationAddress());
            temp2.setLatLng(new LatLng(gottenRequest.getEnd()[1], gottenRequest.getEnd()[0]));
            request.setDestinationPlace(temp2);

            requests.add(request);
        }
    }
}

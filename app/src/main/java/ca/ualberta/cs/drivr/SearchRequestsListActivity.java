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

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * An activity that shows a list of currently active requests.
 */

public class SearchRequestsListActivity extends AppCompatActivity {
    private static final String TAG = "RequestListActivity";
    public static final String EXTRA_SEARCH_REQUEST = "ca.ualberta.cs.driver.SearchRequestListActivity.EXTRA_SEARCH_REQUEST";

    private UserManager userManager = UserManager.getInstance();
    private SearchRequest searchRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        // Setup the RecyclerView
        RecyclerView requestsListRecyclerView = (RecyclerView) findViewById(R.id.requests_list_recycler);
        RequestsListAdapter adapter;
        String searchRequestJson = getIntent().getStringExtra(EXTRA_SEARCH_REQUEST);
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Uri.class, new UriSerializer())
                    .create();
            searchRequest = gson.fromJson(searchRequestJson, SearchRequest.class);
        }
        catch (JsonSyntaxException ex) {
            Log.i(TAG, "json error");
//                SearchRequest searchRequest = null;
        }

        adapter = new RequestsListAdapter(this, searchRequest.getRequests(getApplicationContext()), userManager);

        requestsListRecyclerView.setAdapter(adapter);
        requestsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public Request getRequest(int position) { throw new UnsupportedOperationException(); }

    public RequestsList getRequestsList() { throw new UnsupportedOperationException(); }
}

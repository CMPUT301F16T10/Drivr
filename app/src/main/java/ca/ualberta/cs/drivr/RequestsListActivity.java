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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;
import android.util.Log;

/**
 * An activity that shows a list of currently active requests.
 */

public class RequestsListActivity extends AppCompatActivity {

    private UserManager userManager = UserManager.getInstance();
    private RequestsListAdapter adapter;
    private RecyclerView requestsListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        // Setup the RecyclerView
        requestsListRecyclerView = (RecyclerView) findViewById(R.id.requests_list_recycler);
        adapter = new RequestsListAdapter(this, userManager.getRequestsList().getRequests(), userManager);
        adapter.registerAdapterDataObserver(new AdapterDataObserver() {
            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                requestsListRecyclerView.invalidate();
//                invalidateRequests();
//                adapter.getItemId(positionStart);
//                Log.i(this.toString(), "invalidate");
//                adapter.notifyDataSetChanged();
//                requestsListRecyclerView.removeAllViews();
//                requestsListRecyclerView.invalidate();
//                adapter.notifyItemRangeRemoved();
//                requestsListRecyclerView.setAdapter(adapter);
            }

        });
        requestsListRecyclerView.setAdapter(adapter);
        requestsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.filter(RequestState.PENDING, RequestState.ACCEPTED, RequestState.CONFIRMED);
        invalidateRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.filter(RequestState.PENDING, RequestState.ACCEPTED, RequestState.CONFIRMED);
        invalidateRequests();
    }

    public void invalidateRequests() {
        Log.i("test", "trying to invalidate");
        requestsListRecyclerView.invalidate();
    }





}

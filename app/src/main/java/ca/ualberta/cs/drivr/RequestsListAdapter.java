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
import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * A list adapter for rendering requests in a requests list recycler view.
 *
 * This is modified from:
 * <ul>
 *     <li>From: https://guides.codepath.com/android/using-the-recyclerview</li>
 *     <li>Author: CodePath</li>
 *     <li>Date accessed: November 10, 2016</li>
 * </ul>
 */
public class RequestsListAdapter extends RecyclerView.Adapter<RequestsListAdapter.ViewHolder> {

    /**
     * A class for storing subviews of a view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // References to the views in the list item
        public final TextView otherUserNameTextView;
        public final TextView fareTextView;
        public final TextView routeTextView;
        public final TextView statusTextView;
        public final ImageView callImageView;
        public final ImageView emailImageView;

        /**
         * Instantiate a new ViewHolder.
         * @param itemView The view of the lsit item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            otherUserNameTextView = (TextView) itemView.findViewById(R.id.item_request_other_user_name);
            fareTextView = (TextView) itemView.findViewById(R.id.item_request_fare_text);
            routeTextView = (TextView) itemView.findViewById(R.id.item_request_route_text);
            statusTextView = (TextView) itemView.findViewById(R.id.item_request_status_text);
            callImageView = (ImageView) itemView.findViewById(R.id.item_request_call_image);
            emailImageView = (ImageView) itemView.findViewById(R.id.item_request_email_image);
        }
    }

    /**
     * The context ot display the data.
     */
    private final Context context;

    /**
     * The requests to display.
     */
    private final ArrayList<Request> requests;

    /**
     * Instantiate a new RequestListAdapter.
     * @param context The context to display the the requests
     * @param requests The requests
     */
    public RequestsListAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    /**
     * Inflates the view.
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RequestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View requestView = inflater.inflate(R.layout.item_request, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(requestView);
        return viewHolder;
    }

    /**
     * Called when the view holder is wants to bind the request at a certain position in the list.
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RequestsListAdapter.ViewHolder viewHolder, int position) {
        final Request request = requests.get(position);

        // Get the views to update
        final TextView otherUserNameTextView = viewHolder.otherUserNameTextView;
        final TextView fareTextView = viewHolder.fareTextView;
        final TextView routeTextView = viewHolder.routeTextView;
        final TextView statusTextView = viewHolder.statusTextView;
        final ImageView callImageView = viewHolder.callImageView;
        final ImageView emailImageView = viewHolder.emailImageView;

        // Show the other person's name
        final DriversList drivers = request.getDrivers();
        final String driverUsername = drivers.size() > 0 ? drivers.get(0).getUsername() : "No Driver Yet";
        otherUserNameTextView.setText(driverUsername);

        // Show the estimated time
        fareTextView.setText("$" + request.getFare().toString());

        // Show the route text
        final Place source = request.getSourcePlace();
        final Place destination = request.getDestinationPlace();
        final Address sourceAddress = request.getSourceAddress();
        final Address destinationAddress = request.getDestinationAddress();

        if (destination == null){
            routeTextView.setText("Going from " + sourceAddress.getAddressLine(0) + " to " + destinationAddress.getAddressLine(0));
        }
        else {
            routeTextView.setText("Going from " + source.getName() + " to " + destination.getName());
        }

        routeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                String requestString = gson.toJson(request, Request.class);
                Intent intent = new Intent(context, RequestActivity.class);
                intent.putExtra(RequestActivity.EXTRA_REQUEST, requestString);
                // TODO startActivityForResult() confirm if user presses accept or deny
                // startActivityForResult(intent, );
//                startActivity(intent);
//                startActivity
                context.startActivity(intent);
            }
        });

        // Show the status text
        statusTextView.setText(request.getRequestState().toString());

        // Add a listener to the call image
        callImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You are about to call the other person", Toast.LENGTH_SHORT).show();
            }
        });

        // Add a listener to the email image
        emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You are about to email the other person", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Get the number of items in the request list.
     * @return
     */
    @Override
    public int getItemCount() {
        return requests.size();
    }
}

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

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.common.base.Strings;
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
        public final TextView descriptionTextView;
        public final TextView fareTextView;
        public final TextView routeTextView;
        public final TextView statusTextView;
        public final ImageView callImageView;
        public final ImageView emailImageView;
        public final ImageView checkMarkImageView;
        public final ImageView xMarkImageView;

        /**
         * Instantiate a new ViewHolder.
         * @param itemView The view of the lsit item.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            otherUserNameTextView = (TextView) itemView.findViewById(R.id.item_request_other_user_name);
            descriptionTextView = (TextView) itemView.findViewById(R.id.item_request_description_text);
            fareTextView = (TextView) itemView.findViewById(R.id.item_request_fare_text);
            routeTextView = (TextView) itemView.findViewById(R.id.item_request_route_text);
            statusTextView = (TextView) itemView.findViewById(R.id.item_request_status_text);
            callImageView = (ImageView) itemView.findViewById(R.id.item_request_call_image);
            emailImageView = (ImageView) itemView.findViewById(R.id.item_request_email_image);
            checkMarkImageView = (ImageView) itemView.findViewById(R.id.item_request_complete);
            xMarkImageView = (ImageView) itemView.findViewById(R.id.item_request_deleted);

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
        final TextView descriptionTextView = viewHolder.descriptionTextView;
        final TextView fareTextView = viewHolder.fareTextView;
        final TextView routeTextView = viewHolder.routeTextView;
        final TextView statusTextView = viewHolder.statusTextView;
        final ImageView callImageView = viewHolder.callImageView;
        final ImageView emailImageView = viewHolder.emailImageView;

        // Todo Hide Image Views until correct Request State
        final ImageView checkImageView = viewHolder.checkMarkImageView;
        final ImageView deleteImageView = viewHolder.xMarkImageView;


        // Show the other person's name
        final DriversList drivers = request.getDrivers();

        final String driverUsername = drivers.size() == 1 ? drivers.get(0).getUsername(): "No Driver Yet";

        final String multipleDrivers = "Multiple Drivers Accepted";

        otherUserNameTextView.setText(drivers.size() > 1 ? multipleDrivers : driverUsername);

        // If the request has a description, show it. Otherwise, hide te description
        if (Strings.isNullOrEmpty(request.getDescription()))
            descriptionTextView.setVisibility(View.GONE);
        else
            descriptionTextView.setText(request.getDescription());

        // Show the fare
        fareTextView.setText("$" + request.getFareString());

        // Show the route
        routeTextView.setText(request.getRoute());

        // Driver User
        otherUserNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // there exists drivers
                if(otherUserNameTextView.getText() != "No Driver Yet") {
                    //Todo
                }
            }
        });

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
                if (drivers.size() == 0) {
                    Toast.makeText(context, "No driver number available at this time", Toast.LENGTH_SHORT).show();

                }
                // Start Dialer
                else if (drivers.size() == 1) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    String number = drivers.get(0).getPhoneNumber();
                    number = "tel:" + number;
                    intent.setData(Uri.parse(number));
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    context.startActivity(intent);

                }
                else {
                    //Todo add a dialog with all drivers
                    Toast.makeText(context, "Unsupported Right now", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add a listener to the email image
        emailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drivers.size() == 0) {
                    Toast.makeText(context, "No driver email available at this time", Toast.LENGTH_SHORT).show();

                }
                //http://stackoverflow.com/questions/8701634/send-email-intent
                else if(drivers.size() == 1) {

                    Intent intent = new Intent();
                    ComponentName emailApp = intent.resolveActivity(context.getPackageManager());
                    ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
                    boolean hasEmailApp = emailApp != null && !emailApp.equals(unsupportedAction);

                    String email = drivers.get(0).getEmail();
                    String subject = "Drivr Request: " + request.getId();
                    String body = "Drivr user " + drivers.get(0).getUsername();

                    if (hasEmailApp) {
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                        context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                    }
                    else {
                        Toast.makeText(context, "An email account is required to use this feature", Toast.LENGTH_LONG).show();

                        // Todo possibly implement to save contact info for later
                        // http://stackoverflow.com/questions/27528236/mailto-android-unsupported-action-error
                        /*
                        Intent intentEmail = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intentEmail.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        intentEmail.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
                        context.startActivity(intentEmail); */

                    }


                }
                else {
                    //Todo add a dialog with all drivers
                    Toast.makeText(context, "Unsupported Right now", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RequestCompletedActivity.class);



                context.startActivity(intent);
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

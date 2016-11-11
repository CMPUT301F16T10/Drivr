package ca.ualberta.cs.drivr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView otherUserNameTextView;
        public final TextView fareTextView;
        public final TextView routeTextView;
        public final TextView acceptTextView;
        public final TextView declineTextView;
        public final ImageView callImageView;
        public final ImageView emailImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            otherUserNameTextView = (TextView) itemView.findViewById(R.id.other_user_name_text);
            fareTextView = (TextView) itemView.findViewById(R.id.fare_text);
            routeTextView = (TextView) itemView.findViewById(R.id.route_text);
            acceptTextView = (TextView) itemView.findViewById(R.id.accept_text);
            declineTextView = (TextView) itemView.findViewById(R.id.decline_text);
            callImageView = (ImageView) itemView.findViewById(R.id.call_image);
            emailImageView = (ImageView) itemView.findViewById(R.id.email_image);
        }
    }

    private final Context context;
    private final ArrayList<Request> requests;

    public RequestsListAdapter(Context context, ArrayList<Request> requests) {
        this.context = context;
        this.requests = requests;
    }

    @Override
    public RequestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_request, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RequestsListAdapter.ViewHolder viewHolder, int position) {
        Request request = requests.get(position);

        // Get the views to update
        TextView otherUserNameTextView = viewHolder.otherUserNameTextView;
        TextView fareTextView = viewHolder.fareTextView;
        TextView routeTextView = viewHolder.routeTextView;
        TextView acceptTextView = viewHolder.acceptTextView;
        TextView declineTextView = viewHolder.declineTextView;
        ImageView callImageView = viewHolder.callImageView;
        ImageView emailImageView = viewHolder.emailImageView;

        // Show the other person's name
        final User driver = request.getDriver();
        otherUserNameTextView.setText(driver != null ? driver.getName() : "No Driver Yet");

        // Show the estimated time
        fareTextView.setText("$" + request.getFare().toString());

        // Show the route text
        final Place source = request.getSourcePlace();
        final Place destination = request.getDestinationPlace();
        routeTextView.setText("Going from " + source.getName() + " to " + destination.getName());

        // Add a listener to the accept text view
        acceptTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You have accepted the request", Toast.LENGTH_SHORT).show();
            }
        });

        // Add a listener to the decline text view
        declineTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You have declined the request", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public int getItemCount() {
        return requests.size();
    }
}

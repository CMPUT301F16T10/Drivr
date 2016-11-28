package ca.ualberta.cs.drivr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by justin on 27/11/16.
 */
public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {

    /**
     * A class for storing subviews of a view.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // References to the views in the list item
        public final TextView driverUserNameTextView;
        public final ImageView callDriverImageView;
        public final ImageView emailDriverImageView;
        public final ImageView acceptRequestImageView;
        public final ImageView driverProfileImageView;

        public final RatingBar driverRatingBarView;


        /**
         * Instantiate a new ViewHolder.
         *
         * @param itemView The view of the lsit item.
         */
        public ViewHolder(View itemView) {
            super(itemView);

            driverUserNameTextView = (TextView) itemView.findViewById(R.id.item_request_drivers_username);
            callDriverImageView = (ImageView) itemView.findViewById(R.id.item_request_call_driver);
            emailDriverImageView = (ImageView) itemView.findViewById(R.id.item_request_email_driver);
            acceptRequestImageView = (ImageView) itemView.findViewById(R.id.item_request_accepted);
            driverProfileImageView = (ImageView) itemView.findViewById(R.id.item_request_profile_view);
            driverRatingBarView = (RatingBar) itemView.findViewById(R.id.multiple_driver_bar);

        }
    }

    /**
     * The context ot display the data.
     */
    private final Context context;

    /**
     * The requests to display.
     */
    private final ArrayList<Driver> drivers;

    /**
     * Instantiate a new RequestListAdapter.
     *
     * @param context The context to display the the requests
     * @param drivers The driver
     */
    public DriverListAdapter(Context context, ArrayList<Driver> drivers) {
        this.context = context;
        this.drivers = drivers;
    }

    /**
     * Inflates the view.
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public DriverListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View requestView = inflater.inflate(R.layout.driver_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(requestView);
        return viewHolder;
    }

    /**
     * Called when the view holder is wants to bind the request at a certain position in the list.
     *
     * @param viewHolder
     * @param position
     */

    @Override
    public void onBindViewHolder(DriverListAdapter.ViewHolder viewHolder, int position) {


        // Get the views to update
        final TextView driverUserNameTextView = viewHolder.driverUserNameTextView;

        final ImageView driverProfileImageView = viewHolder.driverProfileImageView;
        final ImageView driverCallImageView = viewHolder.callDriverImageView;
        final ImageView driverEmailImageView = viewHolder.emailDriverImageView;
        final ImageView acceptDriverImageView = viewHolder.acceptRequestImageView;
        final RatingBar driverRatingRatingBar = viewHolder.driverRatingBarView;


        // Show the other person's name

        final String driverUsername = drivers.size() == 1 ? drivers.get(0).getUsername() : "No Driver Yet";

        final String multipleDrivers = "Multiple Drivers Accepted";

        otherUserNameTextView.setText(drivers.size() > 1 ? multipleDrivers : driverUsername);
    }
}

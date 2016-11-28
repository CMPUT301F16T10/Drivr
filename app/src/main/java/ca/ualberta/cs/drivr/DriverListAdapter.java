package ca.ualberta.cs.drivr;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private final DriversList drivers;

    /**
     * Instantiate a new RequestListAdapter.
     *
     * @param context The context to display the the requests
     * @param drivers The driver
     */
    public DriverListAdapter(Context context, DriversList drivers) {
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

        final Driver driver = drivers.get(position);

        // Get the views to update
        final TextView driverUserNameTextView = viewHolder.driverUserNameTextView;

        final ImageView driverProfileImageView = viewHolder.driverProfileImageView;
        final ImageView driverCallImageView = viewHolder.callDriverImageView;
        final ImageView driverEmailImageView = viewHolder.emailDriverImageView;
        final ImageView acceptDriverImageView = viewHolder.acceptRequestImageView;
        final RatingBar driverRatingRatingBar = viewHolder.driverRatingBarView;

        driverUserNameTextView.setText(driver.getUsername());
        driverRatingRatingBar.setRating(driver.getRating());

        // goto Profile
        driverProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriSerializer())
                        .create();
                String driverString = gson.toJson(driver, Driver.class);
                Intent intent = new Intent(context, DriverProfileActivity.class);
                intent.putExtra(DriverProfileActivity.DRIVER, driverString);
                context.startActivity(intent);
            }
        });

        // Call the Driver
        driverCallImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                String number = driver.getPhoneNumber();
                number = "tel:" + number;
                intent.setData(Uri.parse(number));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                context.startActivity(intent);
            }
        });

        driverEmailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName emailApp = intent.resolveActivity(context.getPackageManager());
                ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
                boolean hasEmailApp = emailApp != null && !emailApp.equals(unsupportedAction);

                String email = driver.getEmail();
                String subject = "New Accepted Request:";
                String body = "Drivr user " + drivers.get(0).getUsername();

                if (hasEmailApp) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                    context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                }
                else {
                    Toast.makeText(context, "An email account is required to use this feature", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return drivers.size();
    }
}

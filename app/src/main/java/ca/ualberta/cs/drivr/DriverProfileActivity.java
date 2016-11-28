package ca.ualberta.cs.drivr;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DriverProfileActivity extends AppCompatActivity {


    private TextView profileNameTextView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView vehicleTextView;

    private RatingBar driverRatingView;

    private User user;
    private String email;
    private String name;
    private String username;
    private String phoneNumber;
    private String vehicleDescription;


    public static final String DRIVER = "ca.ualberta.cs.drivr.RequestActivity.EXTRA_REQUEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String requestString = getIntent().getStringExtra(DRIVER);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();

        final Driver driver = gson.fromJson(requestString, Driver.class);

        name = driver.getName();
        email = driver.getEmail();
        username = driver.getUsername();
        phoneNumber = driver.getPhoneNumber();
        vehicleDescription = driver.getVehicleDescription();


        usernameTextView = (TextView) findViewById(R.id.profile_username_text_view);
        emailTextView = (TextView) findViewById(R.id.profile_email_text_view);
        phoneTextView = (TextView) findViewById(R.id.profile_phone_number_text_view);
        profileNameTextView = (TextView) findViewById(R.id.profile_name_text_view);
        vehicleTextView = (TextView) findViewById(R.id.vehicle_description_text_view);

        driverRatingView = (RatingBar) findViewById(R.id.ratingBar_DriverProfile);
        driverRatingView.setNumStars(driver.getTotalRatings());

        profileNameTextView.setText(name);
        usernameTextView.setText(username);
        emailTextView.setText(email);
        phoneTextView.setText(phoneNumber);
        vehicleTextView.setText(vehicleDescription);



    }

}

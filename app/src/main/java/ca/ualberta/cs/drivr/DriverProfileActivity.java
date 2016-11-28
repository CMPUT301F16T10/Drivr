package ca.ualberta.cs.drivr;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class DriverProfileActivity extends AppCompatActivity {


    private TextView profileNameTextView;
    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView vehicleTextView;

    private User user;
    private String email;
    private String name;
    private String username;
    private String phoneNumber;
    private String vehicleDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        usernameTextView = (TextView) findViewById(R.id.profile_username_text_view);
        emailTextView = (TextView) findViewById(R.id.profile_email_text_view);
        phoneTextView = (TextView) findViewById(R.id.profile_phone_number_text_view);
        profileNameTextView = (TextView) findViewById(R.id.profile_name_text_view);
        vehicleTextView = (TextView) findViewById(R.id.vehicle_description_text_view);


    }

}

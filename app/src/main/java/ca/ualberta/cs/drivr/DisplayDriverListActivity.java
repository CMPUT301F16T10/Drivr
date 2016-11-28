package ca.ualberta.cs.drivr;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * An Activity to Display Multiple Drivers that accepted a Request
 */
public class DisplayDriverListActivity extends Activity {

    private UserManager userManager = UserManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_driver_list);

        String requestString = getIntent().getStringExtra("REQUEST");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Uri.class, new UriSerializer())
                .create();
        final Request request = gson.fromJson(requestString, Request.class);

        // Setup the RecyclerView
        RecyclerView requestsListRecyclerView = (RecyclerView) findViewById(R.id.driver_list_recycler);
        DriverListAdapter adapter = new DriverListAdapter(this,request.getDrivers());
        requestsListRecyclerView.setAdapter(adapter);
        requestsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

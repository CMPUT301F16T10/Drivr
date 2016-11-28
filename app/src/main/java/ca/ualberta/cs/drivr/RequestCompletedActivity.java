package ca.ualberta.cs.drivr;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class RequestCompletedActivity extends AppCompatActivity {

    public static final String REQUEST_ID_EXTRA = "ca.ualberta.cs.driver.RequestCompletedActivity.REQUEST_ID_EXTRA";

    private class RatingHolder {
        private float rating;
        public RatingHolder() {
            rating = 0;
        }
        public float getRating() { return rating; }
        public void setRating(float rating) { this.rating = rating; }
    }

    private UserManager userManager = UserManager.getInstance();
    private TextView username;
    private final RatingHolder ratingHolder = new RatingHolder();
    private TextView profileTextView;
    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_completed);

        final String requestId = getIntent().getStringExtra(REQUEST_ID_EXTRA);

        // Todo Fetch Driver info including Username
        final RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        profileTextView = (TextView) findViewById(R.id.completed_driver_username_text_view);
        finishButton = (Button) findViewById(R.id.finish_button);

        ratingBar.setOnClickListener(new View.OnClickListener() {

            // Todo Update Rating
            @Override
            public void onClick(View v) {
                ratingHolder.setRating(ratingBar.getRating());
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {

            // Todo Go Back to Request Activity, Move Request to History, Save Ratings
            @Override
            public void onClick(View v) {
                Request request = userManager.getRequestsList().getById(requestId);
                Driver driver = request.getDrivers().size() > 0 ? request.getDrivers().get(0) : null;
                if (driver != null) {
                    Log.i("RequestCompletedActivty", "Completing request");
                    driver.changeRating(ratingHolder.getRating());
                    request.setRequestState(RequestState.COMPLETED);
                    userManager.getRequestsList().removeById(request);
                    userManager.getRequestsList().add(request);
                    ElasticSearch elasticSearch = new ElasticSearch(userManager.getConnectivityManager());
                    elasticSearch.updateUser(driver);
                    elasticSearch.updateRequest(request);
                    userManager.notifyObservers();
                }
                finish();
            }
        });


    }
}

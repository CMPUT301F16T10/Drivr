package ca.ualberta.cs.drivr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class RequestCompletedActivity extends AppCompatActivity {



    private UserManager userManager = UserManager.getInstance();
    private TextView username;
    private Double ratings;
    private RatingBar ratingBar;
    private TextView profileTextView;

    private Button finishButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_completed);



        // Todo Fetch Driver info including Username


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        profileTextView = (TextView) findViewById(R.id.completed_driver_username_text_view);
        finishButton = (Button) findViewById(R.id.finish_button);

        ratingBar.setOnClickListener(new View.OnClickListener() {

            // Todo Update Rating
            @Override
            public void onClick(View v) {

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {

            // Todo Go Back to Request Activity, Move Request to History, Save Ratings
            @Override
            public void onClick(View v) {

            }
        });


    }
}

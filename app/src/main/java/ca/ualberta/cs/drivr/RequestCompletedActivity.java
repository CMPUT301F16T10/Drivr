package ca.ualberta.cs.drivr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class RequestCompletedActivity extends AppCompatActivity {



    private UserManager userManager = UserManager.getInstance();
    private TextView username;
    private Double ratings;
    private RatingBar ratingBar;
    private TextView profileTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_completed);



        // Todo Fetch Driver info including Username


        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        profileTextView = (TextView) findViewById(R.id.username_text_view);

        ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}

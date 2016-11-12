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

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Setting this up for JavaDocs.
 */

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView profileNameTextView;
    private TextView emailTextView;
    private TextView addressTextView;
    private TextView numberTextView;
    private ProfileController profileController;
    private IUserManager iUserManager;

    private ImageView profileBoxImageView;
    private ImageView editProfileImageView;
    private ImageView profilePictureImageView;

    private String email;
    private String address;
    private String phoneNumber;

    private Boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize the Text Views
        profileNameTextView = (TextView)findViewById(R.id.profile_name);
        usernameTextView = (TextView)findViewById(R.id.profile_username);
        emailTextView = (TextView)findViewById(R.id.profile_email);
        addressTextView = (TextView)findViewById(R.id.profile_address);
        numberTextView = (TextView)findViewById(R.id.profile_phone);

        // Initialize the ImageViews
        profileBoxImageView = (ImageView)findViewById(R.id.profile_box);
        editProfileImageView = (ImageView)findViewById(R.id.edit_profile);
        profilePictureImageView = (ImageView)findViewById(R.id.profile_picture_image);

        editProfileImageView.setClickable(true);
        editProfileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                if (editMode){
                    Toast.makeText(getApplicationContext(), "Edit Data Mode OFF", Toast.LENGTH_SHORT).show();
                    editMode = false;
                }
                else {
                    Toast.makeText(getApplicationContext(), "Edit Data Mode On", Toast.LENGTH_SHORT).show();
                    editMode = true;
                }
            }
        });

        profileNameTextView.setOnClickListener(new TextView.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 if(editMode){
                                                     profileNameTextView.setText("Changed Name");
                                                 }
                                             }
                                         }


        );


        emailTextView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMode){
                    emailTextView.setText("Changed Email");
                }
            }
        }


        );

        addressTextView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMode){
                    addressTextView.setText("Changed Address");
                }
            }
        }


        );
        numberTextView.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editMode){
                    numberTextView.setText("Changed Number");
                }
            }
        }


        );





        //Create Controllers
        iUserManager = new IUserManager() {
            @Override
            public User getUser() {
                // Query for User
                return null;
            }

            @Override
            public void setUser(User user) { }

            @Override
            public RequestsList getRequestsList() {
                return null;
            }

            @Override
            public void setRequestsList(RequestsList requestsList) { }

            @Override
            public UserMode getUserMode() {
                return null;
            }

            @Override
            public void setUserMode(UserMode userMode) { }
        };

        profileController = new ProfileController(iUserManager);

        // TODO get user
        // TODO Set profile up

    }
}

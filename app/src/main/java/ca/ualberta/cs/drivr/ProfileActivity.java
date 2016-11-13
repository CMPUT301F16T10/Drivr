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
import android.widget.EditText;
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

    private ImageView profileBoxImageView;
    private ImageView editProfileImageView;
    private ImageView profilePictureImageView;

    private EditText userNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;

    private User user;
    private String email;
    private String name;
    private String userName;
    private String phoneNumber;

    private Boolean editMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize the Text Views
        profileNameTextView = (TextView)findViewById(R.id.profile_name);


        // Initialize the ImageViews
        profileBoxImageView = (ImageView)findViewById(R.id.profile_box);
        editProfileImageView = (ImageView)findViewById(R.id.edit_profile);
        profilePictureImageView = (ImageView)findViewById(R.id.profile_picture_image);

        userNameEditText = (EditText)findViewById(R.id.editTextUsername);
        phoneEditText = (EditText)findViewById(R.id.editTextPhoneNumber);
        addressEditText = (EditText)findViewById(R.id.editTextAddress);
        emailEditText = (EditText)findViewById(R.id.editTextEmail);

        // Set up UserManager
        final UserManager userManager = UserManager.getInstance();
        user = userManager.getUser();

        // Get info
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        userName = user.getUsername();
        name = user.getName();


        userNameEditText.setText(userName);
        phoneEditText.setText(phoneNumber);
        addressEditText.setText("123 Fake Street");
        emailEditText.setText(email);
        profileNameTextView.setText(name);

        userNameEditText.setTextIsSelectable(false);
        phoneEditText.setTextIsSelectable(false);
        addressEditText.setTextIsSelectable(false);
        emailEditText.setTextIsSelectable(false);

        userNameEditText.setCursorVisible(false);
        phoneEditText.setCursorVisible(false);
        addressEditText.setCursorVisible(false);
        emailEditText.setCursorVisible(false);

        editProfileImageView.setClickable(true);
        editProfileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                if (editMode){
                    Toast.makeText(getApplicationContext(), "Edit Data Mode OFF", Toast.LENGTH_SHORT).show();
                    editMode = false;
                    //userNameEditText.setCursorVisible(false);
                    phoneEditText.setCursorVisible(false);
                    addressEditText.setCursorVisible(false);
                    emailEditText.setCursorVisible(false);
                    profileNameTextView.setCursorVisible(true);

                    phoneNumber = phoneEditText.getText().toString();
                    email = emailEditText.getText().toString();

                    user.setEmail(email);
                    user.setPhoneNumber(phoneNumber);


                }
                else {
                    Toast.makeText(getApplicationContext(), "Edit Data Mode On", Toast.LENGTH_SHORT).show();
                    editMode = true;
                    //userNameEditText.setCursorVisible(true);
                    profileNameTextView.setCursorVisible(true);
                    phoneEditText.setCursorVisible(true);
                    addressEditText.setCursorVisible(true);
                    emailEditText.setCursorVisible(true);
                }
            }
        });










        // TODO get user
        // TODO Set profile up

    }
}

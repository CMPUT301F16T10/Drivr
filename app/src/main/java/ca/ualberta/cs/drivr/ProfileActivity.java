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
 * An activity that displays information about a user's profile.
 */
public class ProfileActivity extends AppCompatActivity {

    private final IUserManager userManager = UserManager.getInstance();
    private ProfileController profileController;

    private TextView usernameTextView;
    private TextView profileNameTextView;
    private TextView emailTextView;
    private TextView numberTextView;

    private ImageView profileBoxImageView;
    private ImageView editProfileImageView;
    private ImageView profilePictureImageView;

    private EditText usernameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;

    private User user;
    private String email;
    private String name;
    private String username;
    private String phoneNumber;

    private Boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get references to the TextViews
        profileNameTextView = (TextView) findViewById(R.id.profile_name);

        // Get references to the ImageViews
        profileBoxImageView = (ImageView) findViewById(R.id.profile_box);
        editProfileImageView = (ImageView) findViewById(R.id.edit_profile);
        profilePictureImageView = (ImageView) findViewById(R.id.profile_picture_image);

        // Get references to the EditTexts
        usernameEditText = (EditText) findViewById(R.id.editTextUsername);
        phoneEditText = (EditText) findViewById(R.id.editTextPhoneNumber);
        emailEditText = (EditText) findViewById(R.id.editTextEmail);

        // Set up UserManager
        user = userManager.getUser();

        // Get info
        phoneNumber = user.getPhoneNumber();
        email = user.getEmail();
        username = user.getUsername();
        name = user.getName();

        usernameEditText.setText(username);
        phoneEditText.setText(phoneNumber);
        emailEditText.setText(email);
        profileNameTextView.setText(name);

        usernameEditText.setTextIsSelectable(false);
        phoneEditText.setTextIsSelectable(false);
        emailEditText.setTextIsSelectable(false);

        usernameEditText.setCursorVisible(false);
        phoneEditText.setCursorVisible(false);
        emailEditText.setCursorVisible(false);

        editProfileImageView.setClickable(true);
        editProfileImageView.setOnClickListener(new View.OnClickListener(){
            @Override
                public void onClick(View v) {
                if (editMode) {
                    Toast.makeText(getApplicationContext(), "Edit Data Mode OFF", Toast.LENGTH_SHORT).show();
                    editMode = false;
                    //userNameEditText.setCursorVisible(false);
                    phoneEditText.setCursorVisible(false);
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
                    emailEditText.setCursorVisible(true);
                }
            }
        });

        // TODO get user
        // TODO Set profile up
    }
}

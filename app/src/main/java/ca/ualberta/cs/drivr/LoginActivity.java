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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private AsyncTask<Void, Void, Boolean> mAuthTask = null;
    private static final String TAG = "LoginActivity";

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private EditText loginPhone;
    private EditText loginEmail;
    private EditText loginName;
    private EditText loginUsername;
    private Button signUpButton;
    private Button signInButton;
    private TextView signUpText;
    private TextView signInText;

    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        // mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mLoginFormView = (ScrollView) findViewById(R.id.login_form);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);

        loginPhone = (EditText) findViewById(R.id.login_phone);
        loginEmail = (EditText) findViewById(R.id.login_email);
        loginName = (EditText) findViewById(R.id.login_name);
        loginUsername = (EditText) findViewById(R.id.login_username);

        signUpText = (TextView) findViewById(R.id.login_sign_up_text);
        signInText = (TextView) findViewById(R.id.login_sign_in_text);

        signInButton = (Button) findViewById(R.id.sign_in_button);
        signUpButton = (Button) findViewById(R.id.sign_up_button);

        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        signUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        signUpText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPhone.setVisibility(View.VISIBLE);
                loginEmail.setVisibility(View.VISIBLE);
                loginName.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.GONE);
                signUpText.setVisibility(View.GONE);
                signInText.setVisibility(View.VISIBLE);
            }
        });
        signInText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPhone.setVisibility(View.GONE);
                loginEmail.setVisibility(View.GONE);
                loginName.setVisibility(View.GONE);
                signUpButton.setVisibility(View.GONE);
                signInButton.setVisibility(View.VISIBLE);
                signInText.setVisibility(View.GONE);
                signUpText.setVisibility(View.VISIBLE);
            }
        });

    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // populateAutoComplete();
            }
        }
    }

    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        loginUsername.setError(null);
        loginName.setError(null);
        loginPhone.setError(null);
        loginEmail.setError(null);

        String username = loginUsername.getText().toString();
        String email = loginEmail.getText().toString();
        String name = loginName.getText().toString();
        String phone = loginPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            loginUsername.setError(getString(R.string.error_field_required));
            focusView = loginUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            loginName.setError(getString(R.string.error_field_required));
            focusView = loginName;
            cancel = true;
        }

        if (TextUtils.isEmpty(phone)) {
            loginPhone.setError(getString(R.string.error_field_required));
            focusView = loginName;
            cancel = true;
        } else if (phone.length() != 10) {
            loginPhone.setError("Phone Numbers must be 10-digits");
            focusView = loginPhone;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            loginEmail.setError(getString(R.string.error_field_required));
            focusView = loginEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            loginEmail.setError(getString(R.string.error_invalid_email));
            focusView = loginEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserSignUpTask(username, name, email, phone);
            mAuthTask.execute((Void) null);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        loginUsername.setError(null);
        String username = loginUsername.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            loginUsername.setError(getString(R.string.error_field_required));
            focusView = loginUsername;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String username;
        private User user;
        private UserManager userManager = UserManager.getInstance();

        UserLoginTask(String username) {this.username = username;}

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ElasticSearch elasticSearch = new ElasticSearch((ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE));
            user = elasticSearch.loadUser(username);
            Boolean actualBool;

            if(user == null) {
                actualBool = false;
            } else {
                actualBool = true;
            }

            mAuthTask = null;
            showProgress(false);

            if (actualBool) {
                userManager.setUser(user);
                userManager.notifyObservers();
                finish();
            } else {
                // mPasswordView.setError(getString(R.string.error_incorrect_password));
                // mPasswordView.requestFocus();
                loginUsername.setError("Username does not exist");
                loginUsername.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String username;
        private final String name;
        private final String email;
        private final String phone;
        private User newUser;
        private UserManager userManager = UserManager.getInstance();

        UserSignUpTask(String username, String name, String email, String phone) {
            this.username = username;
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            newUser = new User();
            newUser.setName(name);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPhoneNumber(phone);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            ElasticSearch elasticSearch = new ElasticSearch((ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE));
            Boolean actualBool = elasticSearch.saveUser(newUser);
            Log.i("Bool", actualBool.toString());

            mAuthTask = null;
            showProgress(false);

            if (actualBool) {
                User user = userManager.getUser();
                user.setEmail(email);
                user.setName(name);
                user.setPhoneNumber(phone);
                user.setUsername(username);
                userManager.notifyObservers();
                finish();
            } else {
                loginUsername.setError("Username already taken");
                loginUsername.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


package ca.ualberta.cs.drivr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.List;

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
//        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
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
//        loginUsername = (EditText) findViewById(R.id.)
//        populateAutoComplete();

//        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

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


//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
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
                    .setAction(android.R.string.ok, new View.OnClickListener() {
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
//                populateAutoComplete();
            }
        }
    }

    private void attemptSignUp() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        loginUsername.setError(null);
        loginName.setError(null);
        loginPhone.setError(null);
        loginEmail.setError(null);
//        mEmailView.setError(null);

//        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
        String username = loginUsername.getText().toString();
        String email = loginEmail.getText().toString();
        String name = loginName.getText().toString();
        String phone = loginPhone.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
//            mEmailView.setError(getString(R.string.error_field_required));
            loginUsername.setError(getString(R.string.error_field_required));
            focusView = loginUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
//            mEmailView.setError(getString(R.string.error_field_required));
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
//            mEmailView.setError(getString(R.string.error_field_required));
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

        // Reset errors.
        loginUsername.setError(null);
//        mEmailView.setError(null);

//        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
//        String email = mEmailView.getText().toString();
        String username = loginUsername.getText().toString();
//        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
//            mEmailView.setError(getString(R.string.error_field_required));
            loginUsername.setError(getString(R.string.error_field_required));
            focusView = loginUsername;
            cancel = true;
        }
//        else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

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

//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 4;
//    }

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

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }

//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        mEmailView.setAdapter(adapter);
//    }


//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

//        private final String mEmail;
//        private final String mPassword;
        private final String username;
        private User user;
        private UserManager userManager = UserManager.getInstance();

        UserLoginTask(String username) {
//            mEmail = email;
//            mPassword = password;
            this.username = username;
        }


        @Override
        protected Boolean doInBackground(Void... params) {

//            //TODO : use actual elastic search
//            ElasticSearch elasticSearch = new ElasticSearch();
//            user = elasticSearch.getUser(username);
//            if (user.getUserId().isEmpty()){
//                return false;
//            }
            user = new User();

            return true;

//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }


        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                userManager.setUser(user);
//                user2 = user;
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
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

        //        private final String mEmail;
//        private final String mPassword;
        private final String username;
        private final String name;
        private final String email;
        private final String phone;
        private User user;
        private UserManager userManager = UserManager.getInstance();

        UserSignUpTask(String username, String name, String email, String phone) {

            this.username = username;
            this.name = name;
            this.email = email;
            this.phone = phone;

        }


        @Override
        protected Boolean doInBackground(Void... params) {

//            //TODO : use actual elastic search
//            ElasticSearch elasticSearch = new ElasticSearch();
//            user = elasticSearch.getUser(username);
//            if (!user.getUserId().isEmpty()){
//                return false;
//            }
            user = new User();

            return true;



        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

//                userManager.setUser(user);
                User user = userManager.getUser();
                user.setEmail(email);
                user.setName(name);
                user.setPhoneNumber(phone);
//                user2 = user;
                finish();
            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
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


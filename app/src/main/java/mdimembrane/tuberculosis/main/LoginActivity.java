package mdimembrane.tuberculosis.main;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import mdimembrane.tuberculosis.NewAccount.NewAccountOne;
import mdimembrane.tuberculosis.ServerConfiguration.HttpConnection;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.util.NotificationUtils;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();
    String username, password;
    Button RequestAccount;
    SharedPreferences sharedpreferences;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private ProgressDialog mProgress;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RequestAccount = (Button) findViewById(R.id.button6);

        RequestAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewAccountOne.class);
                startActivity(intent);

            }
        });
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        /* Set up the login form. */
        mUsernameView = (EditText) findViewById(R.id.userNameET);
        mProgress = new ProgressDialog(this);
        String titleId = "Signing in...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");
        mPasswordView = (EditText) findViewById(R.id.passwordET);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.loginBT);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(PreferencesConstants.PushNotificationConfig.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(PreferencesConstants.PushNotificationConfig.TOPIC_GLOBAL);

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(PreferencesConstants.PushNotificationConfig.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };

        displayFirebaseRegId();
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PreferencesConstants.PushNotificationConfig.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(PreferencesConstants.PushNotificationConfig.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                return;
            }
        }
    }
    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            RequestAccount.setText("Firebase Reg Id: " + regId);
//        else
//            RequestAccount.setText("Firebase Reg Id is not received yet!");
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
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    private void saveProfilePic(Bitmap image) {



        File file =new FileHandling(getApplicationContext()).getOutputMediaFile();

        if (file == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        Log.d(TAG,
                "Save Image ");
    }

    public void ErrorAlert(String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, JSONObject> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            HttpConnection jParser = new HttpConnection();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("action", "user_login");
                jsonObject.accumulate("user_name", username);
                jsonObject.accumulate("password", password);

            } catch (Exception e) {
                // TODO: handle exception
            }
            JSONObject json = jParser.getJSONFromUrl(ServerConstants.USER_LOGIN, jsonObject);

            // TODO: register the new account here.


            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            mAuthTask = null;
            String MSG = "";
            boolean RESPONSE_CODE;
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        try {
                            String Qrimage = json.getString("image_data");
                            byte[] qrimage = Base64.decode(Qrimage.getBytes(), 1);
                            Bitmap bmp = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
                            saveProfilePic(bmp);
                        }catch (Exception e){
                            Log.d(TAG, "Error accessing file: " + e.getMessage());
                        }

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean(PreferencesConstants.SessionManager.ACCOUNT_SESSION, true);
                        editor.putString(PreferencesConstants.SessionManager.MY_ACCOUNT_TYPE, json.getString("account_type"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_NAME, json.getString("user_name"));
                        editor.putString(PreferencesConstants.SessionManager.USER_ID, json.getString("user_id"));
                        editor.putString(PreferencesConstants.SessionManager.MY_PERSON_NAME, json.getString("person_name"));
                        editor.putString(PreferencesConstants.SessionManager.MY_EMPLOYEE_CODE, json.getString("employee_code"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_STATE, json.getString("user_state"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_DISTT, json.getString("user_district"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_TEHSIL, json.getString("user_tehsil"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_VILLAGE, json.getString("user_village"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_PINCODE, json.getString("user_pincode"));
                        editor.putString(PreferencesConstants.SessionManager.MY_HOSPITAL_TYPE, json.getString("hospital_type"));
                        editor.putString(PreferencesConstants.SessionManager.MY_HOSPITAL_NAME, json.getString("hospital_name"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_PHONE, json.getString("user_phone"));
                        editor.putString(PreferencesConstants.SessionManager.MY_USER_AADHAR_NO, json.getString("user_aadhar_no"));
                        editor.commit();

                        //   imageview.setImageBitmap(bmp);
                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(intent);
                        finish();


                    } else {
                        ErrorAlert(json.getString("data").toString());
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }


}

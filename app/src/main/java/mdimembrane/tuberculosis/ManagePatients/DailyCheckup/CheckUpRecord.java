package mdimembrane.tuberculosis.ManagePatients.DailyCheckup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class CheckUpRecord extends AppCompatActivity {

    Button submitBTN, backBTN;
    SharedPreferences sharedpreferences;
    String patient_id, patient_name = null;
    EditText weightET, heightET, bpET, tempET;
    TextView idTV, nameTV;
    String weightSTR, heightSTR, bpSTR, tempSTR;
    private ProgressDialog mProgress;
    private CheckUpRecord mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_up_record);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        mProgress = new ProgressDialog(this);
        String titleId = "Save Data...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");

        weightET = (EditText) findViewById(R.id.weightEditText);
        heightET = (EditText) findViewById(R.id.heightEditText);
        bpET = (EditText) findViewById(R.id.bpEditText);
        tempET = (EditText) findViewById(R.id.tempEditText);

        idTV = (TextView) findViewById(R.id.idTextView);
        nameTV = (TextView) findViewById(R.id.nameTextView);

        idTV.setText(patient_id);
        nameTV.setText(patient_name);

        submitBTN = (Button) findViewById(R.id.submitButton);
        backBTN = (Button) findViewById(R.id.backButton);


        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_LONG).show();

                if (weightET.getText().toString().equals("") &&
                        heightET.getText().toString().equals("") &&
                        bpET.getText().toString().equals("") &&
                        tempET.getText().toString().equals("")) {

                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_form_alert_toast), Toast.LENGTH_LONG).show();

                    return;
                }

                weightSTR = weightET.getText().toString();
                heightSTR = heightET.getText().toString();
                bpSTR = bpET.getText().toString();
                tempSTR = tempET.getText().toString();

           //     new CheckupData().execute();



                ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo nf=cn.getActiveNetworkInfo();

                {
                    if (nf != null && nf.isConnected() == true) {
                        //   Toast.makeText(getApplicationContext(), "Internet Connection Available", Toast.LENGTH_LONG).show();

                        new CheckupData().execute();

                    } else {
                        Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();

                    }

                }

            }
        });


        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class CheckupData extends AsyncTask<Void, Void, JSONObject> {


        CheckupData() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.PATIENT_DATA, charset);
                multipart.addFormField("action", "insert_daily_record");
                multipart.addFormField("Rec_patient_id", patient_id);
                multipart.addFormField("Rec_patient_name", patient_name);
                multipart.addFormField("Rec_weight", weightSTR);
                multipart.addFormField("Rec_height", heightSTR);
                multipart.addFormField("Rec_bp", bpSTR);
                multipart.addFormField("Rec_temrature", tempSTR);
                multipart.addFormField("Rec_added_by", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_PERSON_NAME, "NA"));
                multipart.addFormField("Rec_added_by_id", sharedpreferences.getString(PreferencesConstants.SessionManager.USER_ID, "NA"));
                multipart.addFormField("close", "close");
                List<String> response = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                for (String line : response) {
                    Log.v("rht", "Line : " + line);
                    responseSTR = line;
                }
                json = new JSONObject(responseSTR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            mAuthTask = null;
            String MSG = "";
            boolean RESPONSE_CODE;
            mProgress.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("success")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.daily_details_submited), Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}

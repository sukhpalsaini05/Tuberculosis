package mdimembrane.tuberculosis.ManageNotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.R;

public class PatientMedDetails extends AppCompatActivity {

    TextView medicineIdTV, medicineNameTV, strengthTV, frequencyTV, unitsTV,
            ruteTV, issueDateTV, instructionTV, doseTimeTV, statusTV, commentsTV;

    String medicine_id, medicine_name = null;
    String statusSTR = "Pending";
    String remarksSTR="";
    Button cancleBT, givenBT;
    private ProgressDialog mProgress;
    boolean RESPONSE_CODE;
    String MSG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_med_details);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            medicine_id = bundle.getString("MEDICINE_ID");
            medicine_name = bundle.getString("MEDICINE_NAME");
        }


        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));


        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        cancleBT = (Button) findViewById(R.id.cancleBT);
        givenBT = (Button) findViewById(R.id.givenBT);

        medicineIdTV = (TextView) findViewById(R.id.idTextView);
        medicineNameTV = (TextView) findViewById(R.id.nameTextView);
        strengthTV = (TextView) findViewById(R.id.strengthTextView);
        frequencyTV = (TextView) findViewById(R.id.frequencyTextView);
        unitsTV = (TextView) findViewById(R.id.unitsTextView);
        ruteTV = (TextView) findViewById(R.id.ruteTextView);
        issueDateTV = (TextView) findViewById(R.id.issueDateTextView);
        instructionTV = (TextView) findViewById(R.id.doseInstTextView);
        doseTimeTV = (TextView) findViewById(R.id.doseTimeTextView);
        statusTV = (TextView) findViewById(R.id.statusTextView);
        commentsTV = (TextView) findViewById(R.id.commentsTextView);


        medicineIdTV.setText(medicine_id);
        medicineNameTV.setText(medicine_name);

       // new MedDetails().execute();



        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new MedDetails().execute();

            } else {
                Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

        }


        cancleBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CancelMedicineAlert();
            }
        });

        givenBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PatientMedDetails.this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle(getResources().getString(R.string.given_alert));
                alertDialogBuilder.setMessage(getResources().getString(R.string.given_alert_message));

                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                remarksSTR="";
                                statusSTR="Given";

                                new medStatus().execute();

                            }
                        });
                alertDialogBuilder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

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

    public void backButton(View view) {
        finish();
    }


    public void CancelMedicineAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PatientMedDetails.this);
        alertDialog.setTitle("Cancel Medicine");
        alertDialog.setMessage("Enter Remarks");

        final EditText input = new EditText(PatientMedDetails.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String remarks = input.getText().toString();
                        if (!remarks.equals("")) {
                            remarksSTR=remarks;
                            statusSTR="Cancel";

                            new medStatus().execute();

                        }else {
                            Toast.makeText(getApplicationContext(),"Remarks Required",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    public class MedDetails extends AsyncTask<Void, Void, JSONObject> {

        MedDetails() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.MEDICINE_DETAILS, charset);
                multipart.addFormField("action", "get_med_notifi_detail");
                multipart.addFormField("m_id", medicine_id);
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

            String MSG = "";
            boolean RESPONSE_CODE;
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(0);

                        String medicine_strength = jsonChildNode.optString("m_strength").toString();
                        String medicine_frequency = jsonChildNode.optString("m_frequency").toString();
                        String medicine_units = jsonChildNode.optString("m_units").toString();
                        String medicine_rute = jsonChildNode.optString("m_rute").toString();
                        String medicine_issue_date = jsonChildNode.optString("m_date").toString();
                        String medicine_instruction = jsonChildNode.optString("m_dose_instruction").toString();
                        String medicine_dose_time = jsonChildNode.optString("m_dose_time").toString();
                        String medicine_status = jsonChildNode.optString("m_status").toString();
                        String medicine_comments = jsonChildNode.optString("m_comments").toString();


                        strengthTV.setText(medicine_strength);
                        frequencyTV.setText(medicine_frequency);
                        unitsTV.setText(medicine_units);
                        ruteTV.setText(medicine_rute);
                        issueDateTV.setText(medicine_issue_date);
                        instructionTV.setText(medicine_instruction);
                        doseTimeTV.setText(medicine_dose_time);
                        statusTV.setText(medicine_status);
                        commentsTV.setText(medicine_comments);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }
    }

    private class medStatus extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PatientMedDetails.this);
            pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";

                MultipartUtility multipart = new MultipartUtility(ServerConstants.MEDICINE_DETAILS, charset);
                multipart.addFormField("action", "update_med_status");
                multipart.addFormField("m_id", medicine_id);
                multipart.addFormField("m_status", statusSTR);
                multipart.addFormField("m_comments", remarksSTR);
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
            pDialog.cancel();
            try {

                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");

                if (RESPONSE_CODE) {
                    if (MSG.equals("success")) {

                        Intent intent1 = getIntent();
                        setResult(RESULT_OK, intent1);
                        finish();
                    }
                }
            } catch (Exception e) {

            }

        }
    }
}

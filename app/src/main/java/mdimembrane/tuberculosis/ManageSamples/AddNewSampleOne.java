package mdimembrane.tuberculosis.ManageSamples;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.HttpConnection;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.ListViewAdapters.AddNewSampleModel;

public class AddNewSampleOne extends AppCompatActivity {

    String patient_id, patient_name = null;
    TextView idTV, nameTV;
    Spinner testnameSP;
    List<String> testNameList = new ArrayList<String>();
    String testNameSTR = "null";
    JSONObject jsonObject;
    String MSG = "";
    String pragnentSTR = "FALSE";
    boolean RESPONSE_CODE;
    CheckBox pragnentCB;
    ArrayList<AddNewSampleModel> addNewSampleModels = new ArrayList<AddNewSampleModel>();
    SharedPreferences sharedpreferences;
    int selectedTestIndex = 0;
    EditText testNoET;
    String testNoSTR;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_sample_one);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");
        }

        pDialog = new ProgressDialog(AddNewSampleOne.this);
        pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);

        pragnentCB = (CheckBox) findViewById(R.id.pragnentCheckBox);

        testNoET = (EditText) findViewById(R.id.testNoEditText);

        idTV = (TextView) findViewById(R.id.idHintTextView);
        nameTV = (TextView) findViewById(R.id.nameTextView);

        idTV.setText(patient_id);
        nameTV.setText(patient_name);

        new SendRequest().execute();


        pragnentCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pragnentSTR = pragnentCB.isChecked() + "";
                testNameList.clear();
                new SendRequest().execute();
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

    public void submitButton(View view) {
        testNoSTR = testNoET.getText().toString();

        new SaveSampleData().execute();

        Toast.makeText(getApplicationContext(), "Submitted successfully", Toast.LENGTH_SHORT).show();
        finish();


    }

    public void testInstructioBT(View view) {
        if (selectedTestIndex == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Test Name", Toast.LENGTH_SHORT).show();
        } else {
            //Show Dialog
            AddNewSampleModel addNewSampleModel = addNewSampleModels.get(selectedTestIndex - 1);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle("Instructions");
            alertDialogBuilder.setMessage(addNewSampleModel.getTestInstructions());
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }

    private void allTestName() {

        testNameList.add(0, getResources().getString(R.string.test_name));
        testnameSP = (Spinner) findViewById(R.id.testnameSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, testNameList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        testnameSP.setAdapter(dataAdapter);
        testnameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTestIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private class SendRequest extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HttpConnection jParser = new HttpConnection();
            // Getting JSON from URL

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.GET_TEST_NAME, charset);
                multipart.addFormField("action", "get_test_data");
                multipart.addFormField("patient_id", patient_id);
                multipart.addFormField("pragnent", pragnentSTR.toUpperCase());
                multipart.addFormField("close","close");
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
            pDialog.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");

                if (RESPONSE_CODE) {
                    try {
                        if (MSG.equals("OK")) {
                            testNameList.clear();
                            addNewSampleModels.clear();
                            JSONArray jsonMainNode = json.optJSONArray("data");
                            int lengthJsonArr = jsonMainNode.length();
                            for (int i = 0; i < lengthJsonArr; i++) {
                                JSONObject jsonChildNode = jsonMainNode
                                        .getJSONObject(i);
                                String t_id = jsonChildNode.optString("t_id")
                                        .toString();
                                String t_name = jsonChildNode.optString("t_name")
                                        .toString();
                                String t_description = jsonChildNode.optString("t_description")
                                        .toString();
                                String t_units = jsonChildNode.optString("t_units")
                                        .toString();
                                String t_min_range = jsonChildNode.optString("t_min_range")
                                        .toString();
                                String t_max_range = jsonChildNode.optString("t_max_range")
                                        .toString();
                                String t_instructions = jsonChildNode.optString("t_instructions")
                                        .toString();
                                addNewSampleModels.add(new AddNewSampleModel(t_id, t_name, t_description, t_units, t_max_range, t_instructions, t_min_range));
                                testNameList.add(t_name);

                            }
                            allTestName();
                        }


                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class SaveSampleData extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                pDialog.show();
            } catch (Exception e) {

            }

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            AddNewSampleModel addNewSampleModel = addNewSampleModels.get(selectedTestIndex - 1);


            HttpConnection jParser = new HttpConnection();
            // Getting JSON from URL

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.GET_TEST_NAME, charset);
                multipart.addFormField("action", "insert_lab_test");
                multipart.addFormField("test_name", addNewSampleModel.getTestName());
                multipart.addFormField("test_sample_no", testNoSTR);
                multipart.addFormField("test_unique_no", addNewSampleModel.getTestId());
                multipart.addFormField("test_description", addNewSampleModel.getTestDescription());
                multipart.addFormField("test_units", addNewSampleModel.getTestUnits());
                multipart.addFormField("test_min_range", addNewSampleModel.getTestMinRange());
                multipart.addFormField("test_max_range", addNewSampleModel.getTestMaxRange());
                multipart.addFormField("test_instructions", addNewSampleModel.getTestInstructions());
                multipart.addFormField("test_patient_name", patient_name);
                multipart.addFormField("test_patient_id", patient_id);
                multipart.addFormField("test_patient_pragnent", pragnentSTR);
                multipart.addFormField("test_patient_added_by", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_PERSON_NAME, "NA"));
                multipart.addFormField("test_patient_added_by_id", sharedpreferences.getString(PreferencesConstants.SessionManager.USER_ID, "NA"));
                multipart.addFormField("close","close");

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
            pDialog.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");

                if (RESPONSE_CODE) {
                    try {
                        if (MSG.equals("OK")) {

                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


package mdimembrane.tuberculosis.ManagePatients.EditPatient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class EditPatientDetailsThree extends AppCompatActivity {

    Spinner bloodGroupSP;
    EditText weightET, heightET, otherDiseaseET, commentET;
    SharedPreferences sharedpreferences;
    String MSG = "";
    boolean RESPONSE_CODE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_details_three);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        bloodGroupSP = (Spinner) findViewById(R.id.bloodGroupSpinner);
        weightET = (EditText) findViewById(R.id.weightEditText);
        heightET = (EditText) findViewById(R.id.heightEditText);
        otherDiseaseET = (EditText) findViewById(R.id.otherDiseaseEditText);
        commentET = (EditText) findViewById(R.id.commentEditText);


        weightET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_WEIGHT,"NA"));
        heightET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_HEIGHT,"NA"));
        otherDiseaseET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ANY_OTHER_DISEASES,"NA"));
        commentET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_COMMENTS,"NA"));
        List<String> blood_group_arrays = Arrays.asList(getResources().getStringArray(R.array.blood_group_arrays));
        bloodGroupSP.setSelection(blood_group_arrays.indexOf(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_BLOOD_GROUP,"NA")));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void backButton(View view) {
        finish();
    }

    public void nextButton(View view) {


        if (bloodGroupSP.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_select_blood_group), Toast.LENGTH_SHORT).show();
            return;
        }
        if (weightET.getText().toString().equals("")) {

            weightET.setError(getResources().getString(R.string.weight_validation));
            weightET.requestFocus();
            return;
        }
        if (heightET.getText().toString().equals("")) {

            heightET.setError(getResources().getString(R.string.height_validation));
            heightET.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.EditPatient.EDIT_BLOOD_GROUP, bloodGroupSP.getSelectedItem().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_WEIGHT, weightET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_HEIGHT, heightET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_ANY_OTHER_DISEASES, otherDiseaseET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_COMMENTS, commentET.getText().toString());
        editor.commit();


         new UpdateAllData().execute(ServerConstants.PATIENT_DATA);

    }


    public void SaveAlert(String record_id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_patient_form_tittle_updation_successful));
        alertDialogBuilder.setMessage(getResources().getString(R.string.alert_patient_form_details_updated)+"\n Patient Id : "+record_id);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent1 = getIntent();
                        setResult(RESULT_OK, intent1);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class UpdateAllData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPatientDetailsThree.this);
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
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                File uploadFile1 = new File(imagesFolder, "user_pic.jpg");

                MultipartUtility multipart = new MultipartUtility(args[0], charset);
                multipart.addFormField("action", "update_patient");
                multipart.addFormField("category_type", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_CATEGORY_TYPE, "Null"));
                multipart.addFormField("category_id", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_CATEGORY_NO, "Null"));
                multipart.addFormField("patient_name", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_NAME, "Null"));
                multipart.addFormField("patient_id", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_ID, "Null"));
                multipart.addFormField("guardian_type", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_TYPE, "Null"));
                multipart.addFormField("guardian_name", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_NAME, "Null"));
                multipart.addFormField("age", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_AGE, "Null"));
                multipart.addFormField("gender", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GENDER, "Null"));
                multipart.addFormField("patient_aadhar_no", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_AADHAR_NO, "Null"));
                multipart.addFormField("patient_phone", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_PHONE, "Null"));
                multipart.addFormField("guardian_phone", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_PHONE, "Null"));
                multipart.addFormField("state", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_USER_STATE, "Null"));
                multipart.addFormField("district", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_USER_DISTT, "Null"));
                multipart.addFormField("tehsil", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_USER_TEHSIL, "Null"));
                multipart.addFormField("address1", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ADDRESS1, "Null"));
                multipart.addFormField("address2", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ADDRESS2, "Null"));
                multipart.addFormField("blood_group", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_BLOOD_GROUP, "Null"));
                multipart.addFormField("weight", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_WEIGHT, "Null"));
                multipart.addFormField("height", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_HEIGHT, "Null"));
                multipart.addFormField("any_other_disease", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ANY_OTHER_DISEASES, "Null"));
                multipart.addFormField("comments", sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_COMMENTS, "Null"));
                multipart.addFormField("added_by", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_PERSON_NAME, "Null"));
                multipart.addFormField("added_by_id", sharedpreferences.getString(PreferencesConstants.SessionManager.USER_ID, "Null"));
                multipart.addFormField("hospital_name", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_HOSPITAL_NAME, "Null"));


                multipart.addFilePart("uploaded_file", uploadFile1);
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
                // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    SaveAlert(json.getString("record_id"));
                }
            } catch (Exception e) {

            }

        }
    }
}

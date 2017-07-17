package mdimembrane.tuberculosis.ManagePatients;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.main.MainScreen;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class AddPatientFour extends AppCompatActivity {
    Spinner bloodGroupSP;
    EditText weightET,heightET,otherDiseaseET,commentET;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_four);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        bloodGroupSP=(Spinner)findViewById(R.id.bloodGroupSpinner);
        weightET=(EditText)findViewById(R.id.weightEditText);
        heightET=(EditText)findViewById(R.id.heightEditText);
        otherDiseaseET=(EditText)findViewById(R.id.otherDiseaseEditText);
        commentET=(EditText)findViewById(R.id.commentEditText);
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
    public void backButton(View view)
    {
        finish();
    }
    public void nextButton(View view)
    {


        if(bloodGroupSP.getSelectedItemPosition()==0)
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_select_blood_group),Toast.LENGTH_SHORT).show();
            return;
        }
        if(weightET.getText().toString().equals("")){

            weightET.setError(getResources().getString(R.string.weight_validation));
            weightET.requestFocus();
            return;
        }
        if(heightET.getText().toString().equals("")){

            heightET.setError(getResources().getString(R.string.height_validation));
            heightET.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.AddNewPatient.BLOOD_GROUP, bloodGroupSP.getSelectedItem().toString());
        editor.putString(PreferencesConstants.AddNewPatient.WEIGHT, weightET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.HEIGHT, heightET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.ANY_OTHER_DISEASES, otherDiseaseET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.COMMENTS, commentET.getText().toString());
        editor.commit();

        SaveAlert();
    }

    public void SaveAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_patient_form_tittle_successful));
        alertDialogBuilder.setMessage(getResources().getString(R.string.alert_patient_form_details_submited));
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private class SendAllData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String responseSTR="";
            JSONObject json=null;
            try {

                String charset = "UTF-8";
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                File uploadFile1 = new File(imagesFolder, "user_pic.jpg");

                MultipartUtility multipart = new MultipartUtility(args[0], charset);
                multipart.addFormField("action", "insert_account");
                multipart.addFormField("patient_name", sharedpreferences.getString(PreferencesConstants.AddNewPatient.PATIENT_NAME, "Null"));
                multipart.addFormField("gaurdian_type", sharedpreferences.getString(PreferencesConstants.AddNewPatient.GAURDIAN_TYPE, "Null"));
                multipart.addFormField("gaurdian_name", sharedpreferences.getString(PreferencesConstants.AddNewPatient.GAURDIAN_NAME, "Null"));
                multipart.addFormField("age", sharedpreferences.getString(PreferencesConstants.AddNewPatient.AGE, "Null"));
                multipart.addFormField("gender", sharedpreferences.getString(PreferencesConstants.AddNewPatient.GENDER, "Null"));
                multipart.addFormField("patient_aadhar_no", sharedpreferences.getString(PreferencesConstants.AddNewPatient.PATIENT_AADHAR_NO, "Null"));
                multipart.addFormField("patient_phone", sharedpreferences.getString(PreferencesConstants.AddNewPatient.PATIENT_PHONE, "Null"));
                multipart.addFormField("gaurdian_phone", sharedpreferences.getString(PreferencesConstants.AddNewPatient.GAURDIAN_PHONE, "Null"));
                multipart.addFormField("address1", sharedpreferences.getString(PreferencesConstants.AddNewPatient.ADDRESS1, "Null"));
                multipart.addFormField("address2", sharedpreferences.getString(PreferencesConstants.AddNewPatient.ADDRESS2, "Null"));
                multipart.addFormField("symptoms_list", sharedpreferences.getString(PreferencesConstants.AddNewPatient.SYMPTOMS_LIST, "Null"));
                multipart.addFormField("other_symptoms", sharedpreferences.getString(PreferencesConstants.AddNewPatient.OTHER_SYMPTOMS, "Null"));
                multipart.addFormField("blood_group", sharedpreferences.getString(PreferencesConstants.AddNewPatient.BLOOD_GROUP, "Null"));
                multipart.addFormField("weight", sharedpreferences.getString(PreferencesConstants.AddNewPatient.WEIGHT, "Null"));
                multipart.addFormField("height", sharedpreferences.getString(PreferencesConstants.AddNewPatient.HEIGHT, "Null"));
                multipart.addFormField("any_other_disease", sharedpreferences.getString(PreferencesConstants.AddNewPatient.ANY_OTHER_DISEASES, "Null"));
                multipart.addFormField("comments", sharedpreferences.getString(PreferencesConstants.AddNewPatient.COMMENTS, "Null"));

                multipart.addFilePart("uploaded_file", uploadFile1);

                List<String> response = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                for (String line : response) {
                    Log.v("rht", "Line : " + line);
                    responseSTR=line;
                }
                json = new JSONObject(responseSTR);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

                SaveAlert();
            }
        }
    }


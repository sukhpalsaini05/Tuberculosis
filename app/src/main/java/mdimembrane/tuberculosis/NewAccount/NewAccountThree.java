package mdimembrane.tuberculosis.NewAccount;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.HttpConnection;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.LoginActivity;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class NewAccountThree extends AppCompatActivity {

    Button SaveDetails, BackButton;
    Spinner HospitalTypeSP, HospitalNameSP;
    EditText otherHospitalET, aadharET, phoneET;

    SharedPreferences sharedpreferences;
    List<String> HospitalTypeList = new ArrayList<String>();
    List<String> HospitalNameList = new ArrayList<String>();

    String HosTypeSTR = "null", HosNameSTR = "";

    JSONObject jsonObject;
    String MSG = "";
    boolean RESPONSE_CODE;
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_three);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        ipAddress = Formatter.formatIpAddress(ip);
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        HospitalTypeSP = (Spinner) findViewById(R.id.spinnerHospType);
        HospitalNameSP = (Spinner) findViewById(R.id.spinnerHospName);

        otherHospitalET = (EditText) findViewById(R.id.HospNameET);
        aadharET = (EditText) findViewById(R.id.aadharET);
        phoneET = (EditText) findViewById(R.id.phoneET);

        SaveDetails = (Button) findViewById(R.id.button1);
        BackButton = (Button) findViewById(R.id.button);


        SaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (HosTypeSTR.equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.TostHospitalType), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (otherHospitalET.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.TostHospitalName), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phoneET.getText().toString().length() < 10) {

                    phoneET.setError(getResources().getString(R.string.phone_no_validation));
                    phoneET.requestFocus();
                    return;
                }
                if (aadharET.getText().toString().length() < 12) {

                    aadharET.setError(getResources().getString(R.string.aadhar_no_validation));
                    aadharET.requestFocus();
                    return;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(PreferencesConstants.AddNewAccount.HOSPITAL_TYPE, HosTypeSTR);
                editor.putString(PreferencesConstants.AddNewAccount.HOSPITAL_NAME, otherHospitalET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.USER_PHONE, phoneET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.USER_AADHAR_NO, aadharET.getText().toString());
                editor.commit();

                new SendAllData().execute(ServerConstants.NEW_ACCOUNT);


                //  Toast.makeText(getApplicationContext(),"Details submited Now Wait For User Name and Password And Login in Your Account", Toast.LENGTH_LONG).show();

            }


        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("action", "getHealthCentreType");
            jsonObject.accumulate("Type", "null");
        } catch (Exception e) {
            // TODO: handle exception
        }
        new SendRequest().execute(ServerConstants.GET_LOCATION);
    }


    private void allHospType() {
        // TODO Auto-generated method stub
        HospitalTypeList.add(0, getResources().getString(R.string.Hospital_Type));
        HospitalTypeSP = (Spinner) findViewById(R.id.spinnerHospType);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, HospitalTypeList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        HospitalTypeSP.setAdapter(dataAdapter);
        HospitalTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HosTypeSTR = "";
                if (position > 0) {
                    jsonObject = new JSONObject();
                    try {
                        HosTypeSTR = HospitalTypeList.get(position);
                        jsonObject.accumulate("action", "getHealthCentreName");
                        jsonObject.accumulate("state", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_STATE, "Null"));
                        jsonObject.accumulate("distt", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_DISTT, "Null"));
                        jsonObject.accumulate("type", HosTypeSTR);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    new SendRequest().execute(ServerConstants.GET_LOCATION);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void allHospName() {
        // TODO Auto-generated method stub
        HospitalNameList.add(0, getResources().getString(R.string.Hospital_Name));
        HospitalNameSP = (Spinner) findViewById(R.id.spinnerHospName);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, HospitalNameList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        HospitalNameSP.setAdapter(dataAdapter);
        HospitalNameSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                otherHospitalET.setText("");
                if (position > 0) {

                    if (position == HospitalNameList.size() - 1) {
                        otherHospitalET.setVisibility(View.VISIBLE);
                        otherHospitalET.requestFocus();
                    } else {
                        otherHospitalET.setVisibility(View.GONE);
                        otherHospitalET.setText(HospitalNameList.get(position));
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void SaveAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle(getResources().getString(R.string.alert_tittle_successful));
        alertDialogBuilder.setMessage(getResources().getString(R.string.alert_account_request_details_submited));
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private class SendRequest extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewAccountThree.this);
            pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HttpConnection jParser = new HttpConnection();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(args[0], jsonObject);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    try {
                        if (MSG.equals("type")) {
                            HospitalTypeList.clear();
                            HospitalNameList.clear();
                            allHospName();

                        } else if (MSG.equals("name")) {
                            HospitalNameList.clear();
                        }
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode = jsonMainNode
                                    .getJSONObject(i);
                            String data = jsonChildNode.optString("data")
                                    .toString();
                            if (MSG.equals("type")) {
                                HospitalTypeList.add(data);
                            } else if (MSG.equals("name")) {
                                HospitalNameList.add(data);
                            }
                        }
                        if (MSG.equals("type")) {
                            allHospType();
                        } else if (MSG.equals("name")) {
                            HospitalNameList.add(getResources().getString(R.string.other_Hospital));
                            allHospName();
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

    private class SendAllData extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewAccountThree.this);
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
                multipart.addFormField("account_type", sharedpreferences.getString(PreferencesConstants.AddNewAccount.ACCOUNT_TYPE, "Null"));
                multipart.addFormField("account_name", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_NAME, "Null"));
                multipart.addFormField("account_gender", sharedpreferences.getString(PreferencesConstants.AddNewAccount.GENDER, "Null"));
                multipart.addFormField("account_empid", sharedpreferences.getString(PreferencesConstants.AddNewAccount.EMPLOYEE_CODE, "Null"));
                multipart.addFormField("account_state", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_STATE, "Null"));
                multipart.addFormField("account_district", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_DISTT, "Null"));
                multipart.addFormField("account_tehsil", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_TEHSIL, "Null"));
                multipart.addFormField("account_village", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_VILLAGE, "Null"));
                multipart.addFormField("account_address", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_ADDRESS, "Null"));
                multipart.addFormField("account_pincode", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_PINCODE, "Null"));
                multipart.addFormField("account_hc_type", sharedpreferences.getString(PreferencesConstants.AddNewAccount.HOSPITAL_TYPE, "Null"));
                multipart.addFormField("account_hc_name", sharedpreferences.getString(PreferencesConstants.AddNewAccount.HOSPITAL_NAME, "Null"));
                multipart.addFormField("account_phone", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_PHONE, "Null"));
                multipart.addFormField("account_aadhar", sharedpreferences.getString(PreferencesConstants.AddNewAccount.USER_AADHAR_NO, "Null"));
                multipart.addFormField("account_ipaddress", ipAddress);

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
            pDialog.dismiss();
            if(json!=null)
            {
                SaveAlert();
//                try {
//                    RESPONSE_CODE = json.getBoolean("response");
//                    MSG = json.getString("message");
//                    Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
//                    if (RESPONSE_CODE) {
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

        }
    }
}

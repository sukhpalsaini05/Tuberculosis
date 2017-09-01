package mdimembrane.tuberculosis.ManageMedicines.EditMedicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class EditMedicineOne extends AppCompatActivity {

    final int NEXT_ACTIVITY = 10;
    Button nextBTN, backBTN;
    EditText medicineNameET,medicineStrengthET,tabletsET;
    Spinner medicineRuteSP;
    String patient_id,medicine_id, patient_name = null;
    private ProgressDialog mProgress;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine_one);

        mProgress = new ProgressDialog(this);
        String titleId =getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");
            medicine_id = bundle.getString("MEDICINE_ID");
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        medicineNameET=(EditText)findViewById(R.id.medicinenameET);
        medicineStrengthET=(EditText)findViewById(R.id.medicineStrengthET);
        tabletsET=(EditText)findViewById(R.id.tabletsET);

        medicineRuteSP=(Spinner)findViewById(R.id.routeTypeSpinner);

        nextBTN = (Button) findViewById(R.id.nextButton);
        backBTN = (Button) findViewById(R.id.backButton);


        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(medicineNameET.getText().toString().equals(""))
                {
                    medicineNameET.setError(getResources().getString(R.string.enter_medicine_name));
                    medicineNameET.requestFocus();
                    return;
                }
                if(medicineStrengthET.getText().toString().equals(""))
                {
                    medicineStrengthET.setError(getResources().getString(R.string.enter_medicine_strength));
                    medicineStrengthET.requestFocus();
                    return;
                }
                if(tabletsET.getText().toString().equals(""))
                {
                    tabletsET.setError(getResources().getString(R.string.enter_tablets));
                    tabletsET.requestFocus();
                    return;
                }
                if (medicineRuteSP.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_medicine_rute), Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(EditMedicineOne.this, EditMedicineTwo.class);
                intent.putExtra("MEDICINE_NAME",medicineNameET.getText().toString());
                intent.putExtra("MEDICINE_STRENGTH",medicineStrengthET.getText().toString());
                intent.putExtra("TABLETS",tabletsET.getText().toString());
                intent.putExtra("ROUTE_TYPE",medicineRuteSP.getSelectedItem().toString());
                intent.putExtra("PATIENT_ID", patient_id);
                intent.putExtra("PATIENT_NAME", patient_name);

                startActivityForResult(intent, NEXT_ACTIVITY);

            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

 //       new EditMedicine().execute();


//      Check internet connection

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new EditMedicine().execute();

            } else {
                Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == NEXT_ACTIVITY && resultCode == RESULT_OK) {
            Intent intent1 = getIntent();
            setResult(RESULT_OK, intent1);
            finish();
        }
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


    public class EditMedicine extends AsyncTask<Void, Void, JSONObject> {


        EditMedicine() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.MEDICINE_DETAILS, charset);
                multipart.addFormField("action", "get_medicine_details");
                multipart.addFormField("medicine_id",medicine_id);
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

                        String med_name = jsonChildNode.optString("med_name").toString();
                        String med_strength = jsonChildNode.optString("med_strength").toString();
                        String med_tablets = jsonChildNode.optString("med_tablets").toString();
                        String med_rute = jsonChildNode.optString("med_rute").toString();
                        String med_frequency = jsonChildNode.optString("med_frequency").toString();
                        String med_week_sun = jsonChildNode.optString("med_week_sun").toString();
                        String med_week_mon = jsonChildNode.optString("med_week_mon").toString();
                        String med_week_tus = jsonChildNode.optString("med_week_tus").toString();
                        String med_week_wed = jsonChildNode.optString("med_week_wed").toString();
                        String med_week_thus = jsonChildNode.optString("med_week_thus").toString();
                        String med_week_fri = jsonChildNode.optString("med_week_fri").toString();
                        String med_week_sat = jsonChildNode.optString("med_week_sat").toString();
                        String med_start_date = jsonChildNode.optString("med_start_date").toString();
                        String med_end_date = jsonChildNode.optString("med_end_date").toString();



                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_ID, medicine_id);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_NAME, med_name);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_STRENGTH, med_strength);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_UNITS, med_tablets);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_RUTE, med_rute);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_FREQUENCY, med_frequency);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_SUN, med_week_sun);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_MON, med_week_mon);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_TUE, med_week_tus);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_WED, med_week_wed);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_THU, med_week_thus);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_FRI, med_week_fri);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_SAT, med_week_sat);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_START_DATE,med_start_date);
                        editor.putString(PreferencesConstants.EditMedicine.EDIT_END_DATE,med_end_date);

                        editor.commit();



                        medicineNameET.setText(sharedpreferences.getString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_NAME, "NA"));
                        medicineStrengthET.setText(sharedpreferences.getString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_STRENGTH, "NA"));
                        tabletsET.setText(sharedpreferences.getString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_UNITS, "NA"));
                        List<String> blood_group_arrays = Arrays.asList(getResources().getStringArray(R.array.route_arrays));
                        medicineRuteSP.setSelection(blood_group_arrays.indexOf(sharedpreferences.getString(PreferencesConstants.EditMedicine.EDIT_MEDICINE_RUTE, "NA")));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();

        }

    }

}

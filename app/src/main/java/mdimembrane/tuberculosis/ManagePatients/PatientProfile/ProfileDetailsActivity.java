package mdimembrane.tuberculosis.ManagePatients.PatientProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    TabLayout tabLayout;
    String patient_id = null;
    private ProgressDialog mProgress;
    private ProfileDetailsActivity.PatientProfile mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
        }
        Log.i("patient_id","'"+patient_id+"'");
        mProgress = new ProgressDialog(this);
        String titleId = "Loading Data...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));        //indicate the current tab

        tabLayout.addTab(tabLayout.newTab().setText("General Informatiom"));
        tabLayout.addTab(tabLayout.newTab().setText("Other Details"));
        tabLayout.addTab(tabLayout.newTab().setText("Medical Details"));
        tabLayout.addTab(tabLayout.newTab().setText("CheckUp Data"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        new PatientProfile().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.patient_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            return true;
        }
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void LoadData() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public class PatientProfile extends AsyncTask<Void, Void, JSONObject> {


        PatientProfile() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.PATIENT_DATA, charset);
                multipart.addFormField("action", "get_patient_profile");
                multipart.addFormField("patient_id", patient_id);
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
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                 if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        String p_image = json.getString("image_data");
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(0);

                        String patient_unique_generated_id = jsonChildNode.optString("P_Unique_Generated_Id").toString();
                        String patient_name = jsonChildNode.optString("P_Name").toString();
                        String category_no = jsonChildNode.optString("P_category_no").toString();
                        String category_type = jsonChildNode.optString("P_category_type").toString();
                        String status = jsonChildNode.optString("P_status").toString();
                        String patient_guardian_type = jsonChildNode.optString("P_Guardian_Type").toString();
                        String patient_guardian_name = jsonChildNode.optString("P_Guardian_Name").toString();
                        String patient_gender = jsonChildNode.optString("P_Gender").toString();
                        String patient_age = jsonChildNode.optString("P_Age").toString();
                        String patient_adhar_card_no = jsonChildNode.optString("P_Adhar_card_no").toString();
                        String patient_phone = jsonChildNode.optString("P_Phone_no").toString();
                        String patient_relative_phn_no = jsonChildNode.optString("P_Relative_phn_no").toString();
                        String patient_state = jsonChildNode.optString("P_State").toString();
                        String patient_district = jsonChildNode.optString("P_District").toString();
                        String patient_tehsil = jsonChildNode.optString("P_Tehsil").toString();
                        String patient_address1 = jsonChildNode.optString("P_Address1").toString();
                        String patient_address2 = jsonChildNode.optString("P_Address2").toString();
                      //  String patient_current_symptoms = jsonChildNode.optString("P_Current_Symptoms").toString();
                        String patient_blood_group = jsonChildNode.optString("P_Blood_Group").toString();
                        String patient_weight = jsonChildNode.optString("P_Weight").toString();
                        String patient_height = jsonChildNode.optString("P_Height").toString();
                        String patient_other_diseases = jsonChildNode.optString("P_Other_Diseases").toString();
                        String patient_any_comment = jsonChildNode.optString("P_Any_comment").toString();
                        String patient_date = jsonChildNode.optString("P_Registration_Date_time").toString();



                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(PreferencesConstants.PatientProfile.PATIENT_ID, patient_unique_generated_id);
                        editor.putString(PreferencesConstants.PatientProfile.PATIENT_NAME, patient_name);
                        editor.putString(PreferencesConstants.PatientProfile.CATEGORY_NO, category_no);
                        editor.putString(PreferencesConstants.PatientProfile.CATEGORY_TYPE, category_type);
                        editor.putString(PreferencesConstants.PatientProfile.STATUS, status);
                        editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_TYPE, patient_guardian_type);
                        editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_NAME, patient_guardian_name);
                        editor.putString(PreferencesConstants.PatientProfile.IMAGE, p_image);
                        editor.putString(PreferencesConstants.PatientProfile.AGE, patient_age);
                        editor.putString(PreferencesConstants.PatientProfile.GENDER, patient_gender);
                        editor.putString(PreferencesConstants.PatientProfile.PATIENT_AADHAR_NO, patient_adhar_card_no);
                        editor.putString(PreferencesConstants.PatientProfile.PATIENT_PHONE, patient_phone);
                        editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_PHONE, patient_relative_phn_no);
                        editor.putString(PreferencesConstants.PatientProfile.P_STATE, patient_state);
                        editor.putString(PreferencesConstants.PatientProfile.P_DISTT, patient_district);
                        editor.putString(PreferencesConstants.PatientProfile.P_TEHSIL, patient_tehsil);
                        editor.putString(PreferencesConstants.PatientProfile.ADDRESS1, patient_address1);
                        editor.putString(PreferencesConstants.PatientProfile.ADDRESS2, patient_address2);
                      //  editor.putString(PreferencesConstants.PatientProfile.SYMPTOMS_LIST, patient_current_symptoms);
                        editor.putString(PreferencesConstants.PatientProfile.BLOOD_GROUP, patient_blood_group);
                        editor.putString(PreferencesConstants.PatientProfile.WEIGHT, patient_weight);
                        editor.putString(PreferencesConstants.PatientProfile.HEIGHT, patient_height);
                        editor.putString(PreferencesConstants.PatientProfile.ANY_OTHER_DISEASES, patient_other_diseases);
                        editor.putString(PreferencesConstants.PatientProfile.COMMENTS, patient_any_comment);
                        editor.putString(PreferencesConstants.PatientProfile.DATE, patient_date);
                        editor.commit();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
            LoadData();
        }


    }
}

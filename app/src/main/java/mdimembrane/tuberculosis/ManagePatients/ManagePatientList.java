package mdimembrane.tuberculosis.ManagePatients;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ManageMedicines.AddNewMedicine.MedicineList;
import mdimembrane.tuberculosis.ManagePatients.DailyCheckup.CheckUpRecord;
import mdimembrane.tuberculosis.ManagePatients.EditPatient.EditPatientDetailsOne;
import mdimembrane.tuberculosis.ManagePatients.PatientProfile.ProfileDetailsActivity;
import mdimembrane.tuberculosis.ManageSamples.AddNewSampleOne;
import mdimembrane.tuberculosis.ManageSamples.EditSample.EditSampleList;
import mdimembrane.tuberculosis.ManageSamples.SampleList;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.ListViewAdapters.ListViewAdapter;
import mdimembrane.tuberculosis.ListViewAdapters.PatientListModel;

import static mdimembrane.tuberculosis.main_fragments.MedicineMainFragment.ADD_NEW_MEDICINE;
import static mdimembrane.tuberculosis.main_fragments.MedicineMainFragment.EDIT_MEDICINE;
import static mdimembrane.tuberculosis.main_fragments.PatientMainFragment.DAILY_RECORDS;
import static mdimembrane.tuberculosis.main_fragments.PatientMainFragment.EDIT_PATIENTS;
import static mdimembrane.tuberculosis.main_fragments.PatientMainFragment.PATIENT_PROFILE;
import static mdimembrane.tuberculosis.main_fragments.ReportMainFragment.ADD_NEW_REPORT;
import static mdimembrane.tuberculosis.main_fragments.ReportMainFragment.REPORT_RESULT;
import static mdimembrane.tuberculosis.main_fragments.SampleMainFragment.ADD_NEW_SAMPLE;
import static mdimembrane.tuberculosis.main_fragments.SampleMainFragment.EDIT_SAMPLE;
import static mdimembrane.tuberculosis.main_fragments.SampleMainFragment.SAMPLE_RESULT;

public class ManagePatientList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ManagePatientList.class.getSimpleName();

    ListView patientLV;
    ListViewAdapter lviewAdapter;
    SharedPreferences sharedpreferences;
    ArrayList<PatientListModel> patientData = new ArrayList<PatientListModel>();
    int OPEN_ACIVITY_WAY = 0;
    private ProgressDialog mProgress;
    // ArrayList<String> data=new ArrayList<String>();
    //  ArrayList<String> index=new ArrayList<String>();

    //  Context context;
    private ManagePatientList.PatientData mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_list);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            OPEN_ACIVITY_WAY = bundle.getInt("OPEN_ACTIVITY");
        }
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        mProgress = new ProgressDialog(this);
        String titleId = "Loading info...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");
        //   context = this;

        patientLV = (ListView) findViewById(R.id.patientListview);
        lviewAdapter = new ListViewAdapter(this, patientData);
//        System.out.println("adapter => "+lviewAdapter.getCount());

        patientLV.setAdapter(lviewAdapter);

        patientLV.setOnItemClickListener(this);

        //new PatientData().execute();

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new PatientData().execute();

            } else {
                Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_patient);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("hello", newText);
                lviewAdapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


        final TextView textview1 = (TextView) view.findViewById(R.id.textView1);
        final TextView textview2 = (TextView) view.findViewById(R.id.textView2);
        TextView textview3 = (TextView) view.findViewById(R.id.textView3);

        Toast.makeText(getApplicationContext(), "Selected  " + textview1.getText().toString() + "    " + textview2.getText().toString(), Toast.LENGTH_SHORT).show();

        switch (OPEN_ACIVITY_WAY) {
            case PATIENT_PROFILE:

                Intent intent = new Intent(ManagePatientList.this, ProfileDetailsActivity.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                startActivity(intent);
                break;
            case DAILY_RECORDS:

                intent = new Intent(ManagePatientList.this, CheckUpRecord.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            case EDIT_PATIENTS:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setTitle("Edit Patients");
                alertDialogBuilder.setMessage("You Want To Delete Or Update");

                alertDialogBuilder.setPositiveButton("UPDATE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {


                                Intent intent = new Intent(ManagePatientList.this, AddNewSampleOne.class);
                                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                                startActivity(intent);

                            }
                        });
                alertDialogBuilder.setNegativeButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ManagePatientList.this);
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setTitle("Delete Patient");
                                alertDialogBuilder.setMessage("Do you want to delete selected patient");

                                alertDialogBuilder.setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {


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

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            case ADD_NEW_SAMPLE:

                intent = new Intent(ManagePatientList.this, AddNewSampleOne.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            case SAMPLE_RESULT:

                intent = new Intent(ManagePatientList.this, SampleList.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            case ADD_NEW_MEDICINE:

                intent = new Intent(ManagePatientList.this, MedicineList.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            case EDIT_MEDICINE:

                intent = new Intent(ManagePatientList.this, MedicineList.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            case ADD_NEW_REPORT:

                break;

            case REPORT_RESULT:

                break;

            case EDIT_SAMPLE:

                intent = new Intent(ManagePatientList.this, EditSampleList.class);
                intent.putExtra("PATIENT_ID", textview1.getText().toString());
                intent.putExtra("PATIENT_NAME", textview2.getText().toString());
                startActivity(intent);
                break;

            default:
                //default intents
                break;
        }


        //Toast.makeText(this,"ID => "+ patient_id.get(position) +"=> Name"+ patient_name.get(position) +"phone No => "+ patient_phone.get(position) +"Date => "+ patient_date.get(position), Toast.LENGTH_SHORT).show();

    }

    public class PatientData extends AsyncTask<Void, Void, JSONObject> {


        PatientData() {
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
                multipart.addFormField("action", "get_patient_data");
                multipart.addFormField("user_id", sharedpreferences.getString(PreferencesConstants.SessionManager.USER_ID, "NA"));
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
            patientData.clear();
            // TODO: register the new account here.
            String MSG = "";
            boolean RESPONSE_CODE;
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode = jsonMainNode
                                    .getJSONObject(i);

                            String patient_id = jsonChildNode.optString("P_Unique_Generated_Id").toString();
                            String patient_name = jsonChildNode.optString("P_Name").toString();
                            String patient_phone = jsonChildNode.optString("P_Phone_no").toString();
                            String patient_date = jsonChildNode.optString("P_Registration_Date_time").toString();
                            patientData.add(new PatientListModel(patient_id, patient_name, patient_phone, patient_date));
                        }
                    }
                }
            } catch (JSONException e) {
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
                // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        lviewAdapter = new ListViewAdapter(ManagePatientList.this, patientData);
                        patientLV.setAdapter(lviewAdapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }

}
package mdimembrane.tuberculosis.ManageNotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ListViewAdapters.MedicineNotificationListModel;
import mdimembrane.tuberculosis.ListViewAdapters.MedicineNotificationListViewAdapter;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.main_fragments.HomeFragment;

public class MedicineNotificationList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MedicineNotificationList.class.getSimpleName();

    ListView medicineNotificationLV;
    MedicineNotificationListViewAdapter lviewAdapter;
    SharedPreferences sharedpreferences;
    ArrayList<MedicineNotificationListModel> medicineNotificationData = new ArrayList<MedicineNotificationListModel>();
    medicineNotificationData mAuthTask = null;
    Button backBTN;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_notification_list);

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        medicineNotificationLV = (ListView) findViewById(R.id.medicineNotificationList);
        lviewAdapter = new MedicineNotificationListViewAdapter(this, medicineNotificationData);

        medicineNotificationLV.setAdapter(lviewAdapter);

        medicineNotificationLV.setOnItemClickListener(this);

        backBTN = (Button) findViewById(R.id.notifiBackButton);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }


        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
             //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new medicineNotificationData().execute();

            } else {
                Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

        }


       // new medicineNotificationData().execute();


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
                //  Log.i("hello", newText);
                lviewAdapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


        TextView textview1 = (TextView) view.findViewById(R.id.patientIDTextView);
        TextView textview2 = (TextView) view.findViewById(R.id.patientNameTextView);

        Toast.makeText(getApplicationContext(), "Selected  " + textview1.getText().toString() + "    " + textview2.getText().toString(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MedicineNotificationList.this, PatientMedNotifiList.class);
        intent.putExtra("PATIENT_ID", textview1.getText().toString());
        intent.putExtra("PATIENT_NAME", textview2.getText().toString());
        startActivity(intent);
    }

    public class medicineNotificationData extends AsyncTask<Void, Void, JSONObject> {


        medicineNotificationData() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.MEDICINE_DETAILS, charset);
                multipart.addFormField("action", "get_patient_notifis");
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
            medicineNotificationData.clear();
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

                            String patient_id = jsonChildNode.optString("m_patient_id").toString();
                            String patient_name = jsonChildNode.optString("m_patient_name").toString();
                            medicineNotificationData.add(new MedicineNotificationListModel(patient_id, patient_name));
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
                if (RESPONSE_CODE) if (MSG.equals("OK")) {
                    lviewAdapter = new MedicineNotificationListViewAdapter(MedicineNotificationList.this, medicineNotificationData);
                    medicineNotificationLV.setAdapter(lviewAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }

}

package mdimembrane.tuberculosis.ManageNotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import mdimembrane.tuberculosis.ListViewAdapters.PatientMedNotifiListModel;
import mdimembrane.tuberculosis.ListViewAdapters.PatientMedNotifiListViewAdapter;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class PatientMedNotifiList extends AppCompatActivity  implements AdapterView.OnItemClickListener{

    private static final String TAG = PatientMedNotifiList.class.getSimpleName();

    ListView patientMedNotifiLV;
    PatientMedNotifiListViewAdapter lviewAdapter;
    SharedPreferences sharedpreferences;
    String patient_id, patient_name = null;
    ArrayList<PatientMedNotifiListModel> PatientMedNotifiData = new ArrayList<PatientMedNotifiListModel>();
    PatientMedNotifiList.PatientMedNotifiData mAuthTask = null;
    Button backBTN;
    TextView idTV, nameTV;
    private ProgressDialog mProgress;
    final int NEXT_ACTIVITY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_med_notifi_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        patientMedNotifiLV = (ListView) findViewById(R.id.patientMedNotifiList);
        lviewAdapter = new PatientMedNotifiListViewAdapter(this, PatientMedNotifiData);

        patientMedNotifiLV.setAdapter(lviewAdapter);

        patientMedNotifiLV.setOnItemClickListener(this);

        backBTN = (Button) findViewById(R.id.notifiBackButton);

        idTV = (TextView) findViewById(R.id.idTextView);
        nameTV = (TextView) findViewById(R.id.nameTextView);

        idTV.setText(patient_id);
        nameTV.setText(patient_name);


       //new PatientMedNotifiData().execute();

        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new PatientMedNotifiData().execute();

            } else {
                Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

        }


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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == NEXT_ACTIVITY && resultCode == RESULT_OK) {
            new PatientMedNotifiData().execute();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


        TextView textview1 = (TextView) view.findViewById(R.id.medicineIDTextView);
        TextView textview2 = (TextView) view.findViewById(R.id.medicineNameTextView);

        Toast.makeText(getApplicationContext(), "Selected  " + textview1.getText().toString() + "    " + textview2.getText().toString(), Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(PatientMedNotifiList.this, PatientMedDetails.class);
        intent.putExtra("MEDICINE_ID", textview1.getText().toString());
        intent.putExtra("MEDICINE_NAME", textview2.getText().toString());
        startActivityForResult(intent, NEXT_ACTIVITY);

    }

    public class PatientMedNotifiData extends AsyncTask<Void, Void, JSONObject> {


        PatientMedNotifiData() {
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
                multipart.addFormField("action", "get_patient_med_notifi");
                multipart.addFormField("patient_id", patient_id);
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
            PatientMedNotifiData.clear();
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

                            String medicine_id = jsonChildNode.optString("m_s_no").toString();
                            String medicine_name = jsonChildNode.optString("m_name").toString();
                            PatientMedNotifiData.add(new PatientMedNotifiListModel(medicine_id, medicine_name));
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
                    lviewAdapter = new PatientMedNotifiListViewAdapter(PatientMedNotifiList.this, PatientMedNotifiData);
                    patientMedNotifiLV.setAdapter(lviewAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }
}

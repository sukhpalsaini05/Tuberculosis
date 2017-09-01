package mdimembrane.tuberculosis.ManageMedicines.AddNewMedicine;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import mdimembrane.tuberculosis.ManageMedicines.EditMedicine.EditMedicineOne;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.ListViewAdapters.MedicineListModel;
import mdimembrane.tuberculosis.ListViewAdapters.MedicineListViewAdapter;

public class MedicineList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MedicineList.class.getSimpleName();
    final int ADD_MEDICINE_ACTIVITY = 10;

    ListView medicineLV;
    MedicineListViewAdapter lviewAdapter;
    SharedPreferences sharedpreferences;
    ArrayList<MedicineListModel> medicineData = new ArrayList<MedicineListModel>();
    private ProgressDialog mProgress;
    
    medicineData mAuthTask = null;

    TextView idTV, nameTV;
    String patient_id, patient_name = null;
    Button addBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);


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
        String titleId = "Loading info...";
        mProgress.setTitle(titleId);
        mProgress.setMessage("Please Wait...");

        medicineLV = (ListView) findViewById(R.id.medicineListview);
        lviewAdapter = new MedicineListViewAdapter(this, medicineData);

        medicineLV.setAdapter(lviewAdapter);

        medicineLV.setOnItemClickListener(this);



        addBTN = (Button) findViewById(R.id.addMedButton);

        idTV = (TextView) findViewById(R.id.idTextView);
        nameTV = (TextView) findViewById(R.id.nameTextView);

        idTV.setText(patient_id);
        nameTV.setText(patient_name);

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MedicineList.this, AddMedicineOne.class);
                intent.putExtra("PATIENT_ID", patient_id);
                intent.putExtra("PATIENT_NAME", patient_name);
                startActivityForResult(intent, ADD_MEDICINE_ACTIVITY);
            }
        });
        new medicineData().execute();
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

        if (requestCode == ADD_MEDICINE_ACTIVITY && resultCode == RESULT_OK) {
            new medicineData().execute();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


        TextView textview1 = (TextView) view.findViewById(R.id.medicineIdTextView);
        TextView textview2 = (TextView) view.findViewById(R.id.medicineNameTextView);

        Toast.makeText(getApplicationContext(), "Selected  " + textview1.getText().toString() + "    " + textview2.getText().toString(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MedicineList.this, EditMedicineOne.class);
        intent.putExtra("MEDICINE_ID", textview1.getText().toString());
        intent.putExtra("PATIENT_ID", patient_id);
        intent.putExtra("PATIENT_NAME", patient_name);
        startActivity(intent);

    }

    public class medicineData extends AsyncTask<Void, Void, JSONObject> {


        medicineData() {
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
                multipart.addFormField("action", "get_medicine_data");
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
            medicineData.clear();
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

                            String medicine_id = jsonChildNode.optString("med_id").toString();
                            String medicine_name = jsonChildNode.optString("med_name").toString();
                            String medicine_start_date = jsonChildNode.optString("med_start_date").toString();
                            String medicine_end_date = jsonChildNode.optString("med_end_date").toString();
                            medicineData.add(new MedicineListModel(medicine_id, medicine_name, medicine_start_date, medicine_end_date));
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
                    lviewAdapter = new MedicineListViewAdapter(MedicineList.this, medicineData);
                    medicineLV.setAdapter(lviewAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }
    
    
}

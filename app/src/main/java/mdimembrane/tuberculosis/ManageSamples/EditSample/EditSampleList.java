package mdimembrane.tuberculosis.ManageSamples.EditSample;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ListViewAdapters.SampleListModel;
import mdimembrane.tuberculosis.ListViewAdapters.SampleListViewAdapter;
import mdimembrane.tuberculosis.ManageSamples.SampleList;
import mdimembrane.tuberculosis.ManageSamples.SampleResult;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class EditSampleList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = SampleList.class.getSimpleName();

    final int SAMPLE_RESULT_ACTIVITY = 10;

    ListView sampleLV;
    SampleListViewAdapter lviewAdapter;
    SharedPreferences sharedpreferences;
    ArrayList<SampleListModel> sampleData = new ArrayList<SampleListModel>();
    private ProgressDialog mProgress;
    SampleList.sampleData mAuthTask = null;
    TextView idTV, nameTV;
    String patient_id, patient_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sample_list);

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

        sampleLV = (ListView) findViewById(R.id.sampleListview);
        lviewAdapter = new SampleListViewAdapter(this, sampleData);

        sampleLV.setAdapter(lviewAdapter);
        sampleLV.setOnItemClickListener(this);

        idTV = (TextView) findViewById(R.id.idTextView);
        nameTV = (TextView) findViewById(R.id.nameTextView);

        idTV.setText(patient_id);
        nameTV.setText(patient_name);


//        sampleLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Toast.makeText(getApplicationContext(), "Selected" ,Toast.LENGTH_SHORT).show();
//
//                view.setSelected(true);
//
//                return true;
//            }
//        });

        new sampleData().execute();
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

                lviewAdapter.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

//        if (requestCode == NEXT_ACTIVITY && resultCode == RESULT_OK) {
//            Intent intent1 = getIntent();
//            setResult(RESULT_OK, intent1);
//            new sampleData().execute();
//        }
        if (requestCode == SAMPLE_RESULT_ACTIVITY && resultCode == RESULT_OK) {
            Intent intent1 = getIntent();
            setResult(RESULT_OK, intent1);
            new sampleData().execute();
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        TextView textview1 = (TextView) view.findViewById(R.id.sampleNoTextView);
        TextView textview2 = (TextView) view.findViewById(R.id.sampleNameTextView);
        TextView textview3 = (TextView) view.findViewById(R.id.sampleDateTextView);
        TextView textview4 = (TextView) view.findViewById(R.id.resultStatusTextView);

        Toast.makeText(getApplicationContext(), "Selected  " + textview1.getText().toString() + "    " + textview2.getText().toString(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(EditSampleList.this, EditSamples.class);
        intent.putExtra("SAMPLE_NO", textview1.getText().toString());
        intent.putExtra("SAMPLE_NAME", textview2.getText().toString());
        intent.putExtra("SAMPLE_DATE", textview3.getText().toString());
        intent.putExtra("RESULT_STATUS", textview4.getText().toString());
        intent.putExtra("PATIENT_ID", patient_id);
        intent.putExtra("PATIENT_NAME", patient_name);
        startActivityForResult(intent, SAMPLE_RESULT_ACTIVITY);

    }

    public class sampleData extends AsyncTask<Void, Void, JSONObject> {


        sampleData() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.GET_TEST_NAME, charset);
                multipart.addFormField("action", "get_test_sample_data");
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
            sampleData.clear();
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

                            String Sample_no = jsonChildNode.optString("test_sample_no").toString();
                            String Sample_name = jsonChildNode.optString("test_name").toString();
                            String Sample_date = jsonChildNode.optString("test_added_date_time").toString();
                            String result_status = jsonChildNode.optString("test_result_status").toString();
                            sampleData.add(new SampleListModel(Sample_no, Sample_name, Sample_date, result_status));
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
                    lviewAdapter = new SampleListViewAdapter(EditSampleList.this, sampleData);
                    sampleLV.setAdapter(lviewAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }

    }
}
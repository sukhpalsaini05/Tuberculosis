package mdimembrane.tuberculosis.ManageSamples;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class SampleResult extends AppCompatActivity {

    TextView sampleNoTV,sampleNameTV,sampleDateTV,sampleUnitsTV,sampleMinRangeTV,sampleMaxRangeTV,
            resultStatusTV,ExpectedDateTV, testResultTV,resultAddedDateTV,labNameTV,testAddedByTV;

    String Sample_no, Sample_name, Sample_date, Result_status = null;



    private ProgressDialog mProgress;
    private SampleResult.ResultData mAuthTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_result);

        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Sample_no = bundle.getString("SAMPLE_NO");
            Sample_name = bundle.getString("SAMPLE_NAME");
            Sample_date = bundle.getString("SAMPLE_DATE");
            Result_status = bundle.getString("RESULT_STATUS");
        }

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sampleNoTV=(TextView)findViewById(R.id.sampleNoTextView);
        sampleNameTV=(TextView)findViewById(R.id.sampleNameTextView);
        sampleDateTV=(TextView)findViewById(R.id.sampleDateTextView);
        sampleUnitsTV=(TextView)findViewById(R.id.sampleUnitsTextView);
        sampleMinRangeTV=(TextView)findViewById(R.id.sampleMinRangeTextView);
        sampleMaxRangeTV=(TextView)findViewById(R.id.sampleMaxRangeTextView);
        resultStatusTV=(TextView)findViewById(R.id.statusTextView);
        ExpectedDateTV=(TextView)findViewById(R.id.expectedDateTextView);
        testResultTV=(TextView)findViewById(R.id.resultTextView);
        resultAddedDateTV=(TextView)findViewById(R.id.resultDateTextView);
        labNameTV=(TextView)findViewById(R.id.labTextView);
        testAddedByTV=(TextView)findViewById(R.id.addedByTextView);



        sampleNoTV.setText(Sample_no);
        sampleNameTV.setText(Sample_name);


        new ResultData().execute();
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
    public void backButton(View view)
    {
        finish();
    }

    public void okButton(View view)
    {
        finish();
    }




    public class ResultData extends AsyncTask<Void, Void, JSONObject> {


        ResultData() {
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
                multipart.addFormField("action", "get_test_result_data");
                multipart.addFormField("sample_no", Sample_no);
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
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(0);

                        String Sample_date = jsonChildNode.optString("test_added_date_time").toString();
                        String Result_status = jsonChildNode.optString("test_result_status").toString();
                        String sample_units = jsonChildNode.optString("test_units").toString();
                        String sample_max_range = jsonChildNode.optString("test_max_range").toString();
                        String sample_min_range = jsonChildNode.optString("test_min_range").toString();
                        String result_expected_date = jsonChildNode.optString("test_result_expected_date").toString();
                        String test_result = jsonChildNode.optString("test_result_remarks").toString();
                        String result_added_date = jsonChildNode.optString("test_result_added_date").toString();
                        String test_lab_name = jsonChildNode.optString("test_result_lab_name").toString();
                        String result_added_by = jsonChildNode.optString("test_result_added_by").toString();

                        sampleDateTV.setText(Sample_date);
                        resultStatusTV.setText(Result_status);
                        sampleUnitsTV.setText(sample_units);
                        sampleMaxRangeTV.setText(sample_max_range);
                        sampleMinRangeTV.setText(sample_min_range);
                        ExpectedDateTV.setText(result_expected_date);
                        testResultTV.setText(test_result);
                        resultAddedDateTV.setText(result_added_date);
                        labNameTV.setText(test_lab_name);
                        testAddedByTV.setText(result_added_by);

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
        }
    }

}

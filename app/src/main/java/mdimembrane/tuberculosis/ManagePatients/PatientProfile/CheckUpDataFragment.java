package mdimembrane.tuberculosis.ManagePatients.PatientProfile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;


public class CheckUpDataFragment extends Fragment {

    TableLayout dataTable;

    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> weightList = new ArrayList<>();
    ArrayList<String> heightList = new ArrayList<>();
    ArrayList<String> bpList = new ArrayList<>();
    ArrayList<String> tempratureList = new ArrayList<>();
    SharedPreferences sharedpreferences;
    private ProgressDialog mProgress;
    private CheckUpDataFragment mAuthTask = null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_check_up_data, container, false);

        sharedpreferences = getActivity().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        mProgress = new ProgressDialog(getActivity());
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        dataTable = (TableLayout) view.findViewById(R.id.checkUpDataTable);

        new DailyCheckuoData().execute();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Medical Details");
    }

    public void addTableData() {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View convertView = inflater.inflate(R.layout.checkup_data_table, null);

        TextView checkUpDate = (TextView) convertView.findViewById(R.id.checkupDateTextView);
        checkUpDate.setText(getResources().getString(R.string.checkup_date_data));
        checkUpDate.setBackgroundResource(R.drawable.cell_border);


        TextView weight = (TextView) convertView.findViewById(R.id.weightTextView);
        weight.setText(getResources().getString(R.string.weight_data));
        weight.setGravity(Gravity.CENTER);

        TextView height = (TextView) convertView.findViewById(R.id.heightTextView);
        height.setText(getResources().getString(R.string.height_data));
        height.setGravity(Gravity.CENTER);

        TextView bp = (TextView) convertView.findViewById(R.id.bpTextView);
        bp.setText(getResources().getString(R.string.bp_data));
        bp.setGravity(Gravity.CENTER);

        TextView temprature = (TextView) convertView.findViewById(R.id.tempratureTextView);
        temprature.setText(getResources().getString(R.string.temprature_data));
        temprature.setGravity(Gravity.CENTER);

        dataTable.addView(convertView);

        for (int i = 0; i < dateList.size(); i++) {

            View convertView1 = inflater.inflate(R.layout.checkup_data_table, null);

            checkUpDate = (TextView) convertView1.findViewById(R.id.checkupDateTextView);
            checkUpDate.setText(dateList.get(i) + "");

            weight = (TextView) convertView1.findViewById(R.id.weightTextView);
            weight.setText(weightList.get(i) + "");
            weight.setGravity(Gravity.CENTER);
            weight.setTextColor(Color.BLACK);

            height = (TextView) convertView1.findViewById(R.id.heightTextView);
            height.setText(heightList.get(i) + "");
            height.setGravity(Gravity.CENTER);
            height.setTextColor(Color.BLACK);

            bp = (TextView) convertView1.findViewById(R.id.bpTextView);
            bp.setText(bpList.get(i) + "");
            bp.setGravity(Gravity.CENTER);
            bp.setTextColor(Color.BLACK);

            temprature = (TextView) convertView1.findViewById(R.id.tempratureTextView);
            temprature.setText(tempratureList.get(i) + "");
            temprature.setGravity(Gravity.CENTER);
            temprature.setTextColor(Color.BLACK);

            // Add the TableRow to the TableLayout
            dataTable.addView(convertView1);
        }
    }

    public class DailyCheckuoData extends AsyncTask<Void, Void, JSONObject> {

        DailyCheckuoData() {
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
                multipart.addFormField("action", "get_checkup_data");
                multipart.addFormField("patient_id", sharedpreferences.getString(PreferencesConstants.PatientProfile.PATIENT_ID, "NA"));
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
            mProgress.dismiss();
            dateList.clear();
            weightList.clear();
            heightList.clear();
            tempratureList.clear();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        for (int i = 0; i < jsonMainNode.length(); i++) {
                            JSONObject jsonChildNode = jsonMainNode
                                    .getJSONObject(i);

                            String patient_id = jsonChildNode.optString("Rec_patient_id").toString();
                            String data_date_time = jsonChildNode.optString("Rec_added_date_time").toString();
                            String patient_weight = jsonChildNode.optString("Rec_weight").toString();
                            String patient_height = jsonChildNode.optString("Rec_height").toString();
                            String patient_bp = jsonChildNode.optString("Rec_bp").toString();
                            String patient_temp = jsonChildNode.optString("Rec_temrature").toString();

                            dateList.add(data_date_time);
                            weightList.add(patient_weight);
                            heightList.add(patient_height);
                            bpList.add(patient_bp);
                            tempratureList.add(patient_temp);
                        }

                        addTableData();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
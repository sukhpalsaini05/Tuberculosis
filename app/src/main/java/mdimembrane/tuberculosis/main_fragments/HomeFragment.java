package mdimembrane.tuberculosis.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mdimembrane.tuberculosis.ManageNotification.MedicineNotificationList;
import mdimembrane.tuberculosis.ManageNotification.SamplePatientList;
import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    Button medicineAlertBT, sampleAlertBT, resultsAlertBT;
    TextView medicineNotificationTV, sampleNotificationTV, testNotificationTV;

    SharedPreferences sharedpreferences;
    private HomeFragment.Notification mAuthTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedpreferences = getActivity().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        medicineAlertBT = (Button) view.findViewById(R.id.medicineAlertBT);
        sampleAlertBT = (Button) view.findViewById(R.id.sampleAlertBT);
        resultsAlertBT = (Button) view.findViewById(R.id.resultsAlertBT);

        medicineNotificationTV = (TextView) view.findViewById(R.id.badge_notification_1TV);
        sampleNotificationTV = (TextView) view.findViewById(R.id.badge_notification_2TV);
        testNotificationTV = (TextView) view.findViewById(R.id.badge_notification_3TV);

        medicineNotificationTV.setVisibility(View.GONE);
        sampleNotificationTV.setVisibility(View.GONE);
        testNotificationTV.setVisibility(View.GONE);

        medicineAlertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Medicines Alert" +"   " + medicineNotificationTV.getText().toString(), Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getActivity(), MedicineNotificationList.class);
                startActivity(intent);

            }
        });
        sampleAlertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Samples Alert" +"   " + sampleNotificationTV.getText().toString(), Toast.LENGTH_LONG).show();

                Intent intent=new Intent(getActivity(), SamplePatientList.class);
                startActivity(intent);
            }
        });
        resultsAlertBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Results Alert", Toast.LENGTH_LONG).show();
            }
        });

     //   new Notification().execute();


        ConnectivityManager cn=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                Toast.makeText(getActivity(), "Internet Connection Available", Toast.LENGTH_LONG).show();

                new Notification().execute();

            } else {
                Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }




    public class Notification extends AsyncTask<Void, Void, JSONObject> {

        Notification() {

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.HOME_SCREEN, charset);
                multipart.addFormField("action", "get_notification");
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

                        String medicine_notification = json.getString("medicines").toString();
                        String sample_notification = json.getString("samples").toString();
                        String test_notification = json.getString("results").toString();

                        if (!medicine_notification.equals("0")) {
                            medicineNotificationTV.setText(medicine_notification);
                            medicineNotificationTV.setVisibility(View.VISIBLE);
                        }

                        if (!sample_notification.equals("0")) {
                            sampleNotificationTV.setText(sample_notification);
                            sampleNotificationTV.setVisibility(View.VISIBLE);
                        }
                        if (!test_notification.equals("0")) {
                            testNotificationTV.setText(test_notification);
                            testNotificationTV.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}

package mdimembrane.tuberculosis.NewAccount;

import android.app.ProgressDialog;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.HttpConnection;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class NewAccountTwo extends AppCompatActivity {

    Button NextButton,BackButton;
    List<String> stateList = new ArrayList<String>();
    List<String> disttList = new ArrayList<String>();
    List<String> tehsilList = new ArrayList<String>();
    List<String> townVillageList = new ArrayList<String>();

    String stateSTR="null",disttSTR="",tehsilSTR="",townSTR="",specs_id="";
    EditText OtherVillageET,AddressET,PincodeET;
    Spinner stateSP, disttSP, tehsilSP, townSP;

    JSONObject jsonObject;
    String MSG = "";
    boolean RESPONSE_CODE;

    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        allStates();

        NextButton=(Button)findViewById(R.id.button5);
        BackButton=(Button)findViewById(R.id.button);
        OtherVillageET=(EditText) findViewById(R.id.EditOtherVillage);
        AddressET=(EditText)findViewById(R.id.AddressET);
        PincodeET=(EditText)findViewById(R.id.PincodeET);

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(stateSTR.equals(""))
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.TostState),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(disttSTR.equals(""))
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.TostDistt),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(tehsilSTR.equals(""))
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.SelectTehsils),Toast.LENGTH_SHORT).show();
                    return;
                }

                if(OtherVillageET.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.SelectVillages),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(AddressET.getText().toString().equals("")){

                    AddressET.setError(getResources().getString(R.string.address_validation));
                    AddressET.requestFocus();
                    return;
                }
                if(PincodeET.getText().toString().length()<6){

                    PincodeET.setError(getResources().getString(R.string.pin_code_validation));
                    PincodeET.requestFocus();
                    return;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(PreferencesConstants.AddNewAccount.USER_STATE, stateSTR);
                editor.putString(PreferencesConstants.AddNewAccount.USER_DISTT, disttSTR);
                editor.putString(PreferencesConstants.AddNewAccount.USER_TEHSIL, tehsilSTR);
                editor.putString(PreferencesConstants.AddNewAccount.USER_VILLAGE, OtherVillageET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.USER_ADDRESS, AddressET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.USER_PINCODE, PincodeET.getText().toString());
                editor.commit();


                Intent intent=new Intent(getApplicationContext(),NewAccountThree.class);
                startActivity(intent);

            }
        });

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });


        jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("action", "getStates");
            jsonObject.accumulate("state", "null");
        } catch (Exception e) {
            // TODO: handle exception
        }
        new SendRequest().execute(ServerConstants.GET_LOCATION);
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


    private void allStates() {
        // TODO Auto-generated method stub
        stateList.add(0,getResources().getString(R.string.SelectStates));
        stateSP = (Spinner) findViewById(R.id.statesSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stateList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        stateSP.setAdapter(dataAdapter);
        stateSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stateSTR="";
                if(position>0)
                {
                    jsonObject = new JSONObject();
                    try {
                        stateSTR=stateList.get(position);
                        jsonObject.accumulate("action", "getDistts");
                        jsonObject.accumulate("state", stateSTR);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    new SendRequest().execute(ServerConstants.GET_LOCATION);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void allDistts() {
        // TODO Auto-generated method stub
        disttList.add(0,getResources().getString(R.string.SelectDistts));
        disttSP = (Spinner) findViewById(R.id.disttSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, disttList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        disttSP.setAdapter(dataAdapter);
        disttSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                disttSTR="";
                if(position>0)
                {
                    jsonObject = new JSONObject();
                    try {
                        disttSTR=disttList.get(position);
                        jsonObject.accumulate("action", "getTehsils");
                        jsonObject.accumulate("state", stateSTR);
                        jsonObject.accumulate("distt", disttSTR);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    new SendRequest().execute(ServerConstants.GET_LOCATION);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void allTehsil() {
        // TODO Auto-generated method stub
        tehsilList.add(0,getResources().getString(R.string.SelectTehsils));
        tehsilSP = (Spinner) findViewById(R.id.tehsilSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, tehsilList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        tehsilSP.setAdapter(dataAdapter);
        tehsilSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tehsilSTR="";
                if(position>0)
                {
                    jsonObject = new JSONObject();
                    try {
                        tehsilSTR=tehsilList.get(position);
                        jsonObject.accumulate("action", "getTownVillage");
                        jsonObject.accumulate("state", stateSTR);
                        jsonObject.accumulate("distt", disttSTR);
                        jsonObject.accumulate("tehsil", tehsilSTR);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    new SendRequest().execute(ServerConstants.GET_LOCATION);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void allTownVillage() {
        // TODO Auto-generated method stub
        townVillageList.add(0,getResources().getString(R.string.SelectVillages));
        townSP = (Spinner) findViewById(R.id.townSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, townVillageList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        townSP.setAdapter(dataAdapter);
        townSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OtherVillageET.setText("");
                if(position>0)
                {

                    if(position==townVillageList.size()-1)
                    {
                        OtherVillageET.setVisibility(View.VISIBLE);
                        OtherVillageET.requestFocus();
                    }else {
                        OtherVillageET.setVisibility(View.GONE);
                        OtherVillageET.setText(townVillageList.get(position));
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private class SendRequest extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(NewAccountTwo.this);
            pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            HttpConnection jParser = new HttpConnection();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(args[0], jsonObject);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
               // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    try {
                        if (MSG.equals("states")) {
                            stateList.clear();
                            disttList.clear();
                            tehsilList.clear();
                            townVillageList.clear();
                            allDistts();
                            allTehsil();
                            allTownVillage();

                        } else if (MSG.equals("distts")) {
                            disttList.clear();
                            tehsilList.clear();
                            townVillageList.clear();
                            allTehsil();
                            allTownVillage();

                        } else if (MSG.equals("tehsils")) {
                            tehsilList.clear();
                            townVillageList.clear();
                            allTownVillage();
                        } else if (MSG.equals("town_village")) {
                            townVillageList.clear();
                        }
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode = jsonMainNode
                                    .getJSONObject(i);
                            String data = jsonChildNode.optString("data")
                                    .toString();
                            if (MSG.equals("states")) {
                                stateList.add(data);
                            } else if (MSG.equals("distts")) {
                                disttList.add(data);
                            } else if (MSG.equals("tehsils")) {
                                tehsilList.add(data);
                            } else if (MSG.equals("town_village")) {
                                townVillageList.add(data);
                            }
                        }
                        if (MSG.equals("states")) {
                            allStates();
                        } else if (MSG.equals("distts")) {
                            allDistts();
                        } else if (MSG.equals("tehsils")) {
                            allTehsil();
                        } else if (MSG.equals("town_village")) {
                            townVillageList.add(getResources().getString(R.string.other_village));
                            allTownVillage();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}

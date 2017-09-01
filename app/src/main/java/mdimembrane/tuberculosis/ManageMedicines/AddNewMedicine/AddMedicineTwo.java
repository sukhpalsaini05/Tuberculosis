package mdimembrane.tuberculosis.ManageMedicines.AddNewMedicine;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class AddMedicineTwo extends AppCompatActivity {

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    String patient_id, patient_name = null;
    String frequencySTR, startDateSTR, endDateSTR;
    boolean sunBLN, monBLN, tueBLN, wedBLN, thuBLN, friBLN, satBLN;

    SharedPreferences sharedpreferences;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    EditText startDateET, endDateET;
    Spinner frequencyTypeSP;
    TableRow weekdaysRow, startDateRow, endDateRow;
    CheckBox sunCB, monCB, tueCB, wedCB, thuCB, friCB, satCB;
    Button submitBTN, backBTN;
    String medicineNameSTR, medicineStrengthStr, tabletsSTR, medicineRouteSTR;
    Calendar startDateCalendar, endDateCalendar;
    JSONArray jsonArray = new JSONArray();

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            startDateET.setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", day));
            startDateCalendar.set(year, month, day);
          //  Toast.makeText(getApplicationContext(), "Start date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        }
    };
    DatePickerDialog.OnDateSetListener onDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
            endDateET.setText(year + "-" + String.format("%02d", month+1) + "-" + String.format("%02d", day));
            endDateCalendar.set(year, month, day);
           // Toast.makeText(getApplicationContext(), "End date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        }
    };
    private AddMedicineTwo mAuthTask = null;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_two);

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.saving_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.sub_title_please_wait));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            medicineNameSTR = bundle.getString("MEDICINE_NAME");
            medicineStrengthStr = bundle.getString("MEDICINE_STRENGTH");
            tabletsSTR = bundle.getString("TABLETS");
            medicineRouteSTR = bundle.getString("ROUTE_TYPE");
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");

        }

        submitBTN = (Button) findViewById(R.id.submitbutton);
        backBTN = (Button) findViewById(R.id.backbutton);


        startDateET = (EditText) findViewById(R.id.pickStartDateButton);
        endDateET = (EditText) findViewById(R.id.pickEndDateButton);

        frequencyTypeSP = (Spinner) findViewById(R.id.frequencyTypeSpinner);

        weekdaysRow = (TableRow) findViewById(R.id.weekDaysRow);
        startDateRow = (TableRow) findViewById(R.id.startDateRow);
        endDateRow = (TableRow) findViewById(R.id.endDateRow);

        weekdaysRow.setVisibility(View.GONE);
        startDateRow.setVisibility(View.GONE);
        endDateRow.setVisibility(View.GONE);


        sunCB = (CheckBox) findViewById(R.id.sunCheckBox);
        monCB = (CheckBox) findViewById(R.id.monCheckBox);
        tueCB = (CheckBox) findViewById(R.id.tueCheckBox);
        wedCB = (CheckBox) findViewById(R.id.wedCheckBox);
        thuCB = (CheckBox) findViewById(R.id.thuCheckBox);
        friCB = (CheckBox) findViewById(R.id.friCheckBox);
        satCB = (CheckBox) findViewById(R.id.satCheckBox);

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (frequencyTypeSP.getSelectedItemPosition() == 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_medicine_frequency), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (frequencyTypeSP.getSelectedItemPosition() == 1) {
                    if(startDateET.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_starting_date), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (frequencyTypeSP.getSelectedItemPosition() == 2) {
                    if(startDateET.getText().toString().equals("") || endDateET.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.select_starting_and_ending_date), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if (frequencyTypeSP.getSelectedItemPosition() == 3) {
                    if(startDateET.getText().toString().equals("") || endDateET.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),getResources().getString(R.string.select_starting_and_ending_date), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!sunCB.isChecked()&
                            !monCB.isChecked()&
                            !tueCB.isChecked()&
                            !wedCB.isChecked()&
                            !thuCB.isChecked()&
                            !friCB.isChecked()&
                            !satCB.isChecked())
                    {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_week_days), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                frequencySTR = frequencyTypeSP.getSelectedItem().toString();
                startDateSTR = startDateET.getText().toString();
                endDateSTR = endDateET.getText().toString();
                sunBLN = sunCB.isChecked();
                monBLN = monCB.isChecked();
                tueBLN = tueCB.isChecked();
                wedBLN = wedCB.isChecked();
                thuBLN = thuCB.isChecked();
                friBLN = friCB.isChecked();
                satBLN = satCB.isChecked();

                final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.dosageListLL);
                EditText instruction = null;
                EditText doseTime = null;
                boolean flag = true;
                jsonArray=new JSONArray();
                for (int i = 0; i < linearLayoutForm.getChildCount(); i++) {
                    LinearLayout innerLayout = (LinearLayout) linearLayoutForm
                            .getChildAt(i);
                    instruction = (EditText) innerLayout.findViewById(R.id.instructionET);
                    doseTime = (EditText) innerLayout.findViewById(R.id.doseTimeET);

                    if (instruction.getText().toString().equals("")) {

                        instruction.requestFocus();
                        flag = false;
                        break;
                    }
                    if (doseTime.getText().toString().equals("")) {

                        doseTime.requestFocus();
                        flag = false;
                        break;
                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.accumulate("instruction", instruction.getText().toString());
                        jsonObject.accumulate("dose_time", doseTime.getText().toString());
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                    jsonArray.put(jsonObject);
                }
                if (!flag) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.please_fill_dosage_properly),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                new AddMedicine().execute();

            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        frequencyTypeSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sunCB.setChecked(false);
                monCB.setChecked(false);
                tueCB.setChecked(false);
                wedCB.setChecked(false);
                thuCB.setChecked(false);
                friCB.setChecked(false);
                satCB.setChecked(false);
                startDateET.setText("");
                endDateET.setText("");
                weekdaysRow.setVisibility(View.GONE);
                startDateRow.setVisibility(View.GONE);
                endDateRow.setVisibility(View.GONE);
                if (position > 0) {
                    switch (position) {
                        case 1:
                            weekdaysRow.setVisibility(View.GONE);
                            startDateRow.setVisibility(View.VISIBLE);
                            endDateRow.setVisibility(View.GONE);
                            break;
                        case 2:
                            weekdaysRow.setVisibility(View.GONE);
                            startDateRow.setVisibility(View.VISIBLE);
                            endDateRow.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            weekdaysRow.setVisibility(View.VISIBLE);
                            startDateRow.setVisibility(View.VISIBLE);
                            endDateRow.setVisibility(View.VISIBLE);
                            break;
                    }


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();

        startDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(onDateSetListener, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(Calendar.MONTH), startDateCalendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        endDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = DatePickerDialog.newInstance(onDateSetListener2, endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(Calendar.MONTH), endDateCalendar.get(Calendar.DAY_OF_MONTH), false);
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        addDynamicDosage("", "");
    }

    public void addDosage(View view) {
        addDynamicDosage("", "");
    }

    public void addDynamicDosage(String flowValue, String digitalValue) {

        final LinearLayout linearLayoutForm = (LinearLayout) findViewById(R.id.dosageListLL);
        EditText instruction = null;
        EditText doseTime = null;
        boolean flag = true;
        for (int i = 0; i < linearLayoutForm.getChildCount(); i++) {
            LinearLayout innerLayout = (LinearLayout) linearLayoutForm
                    .getChildAt(i);
            instruction = (EditText) innerLayout.findViewById(R.id.instructionET);
            doseTime = (EditText) innerLayout.findViewById(R.id.doseTimeET);
            if (instruction.getText().toString().equals("")) {

                instruction.requestFocus();
                flag = false;
                break;
            }
            if (doseTime.getText().toString().equals("")) {

                doseTime.requestFocus();
                flag = false;
                break;
            }
        }
        if (!flag) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.please_fill),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        final LinearLayout newView = (LinearLayout) AddMedicineTwo.this
                .getLayoutInflater().inflate(R.layout.dosage_listview, null);
        final EditText instructionET = (EditText) newView.findViewById(R.id.instructionET);
        instructionET.setText(digitalValue);

        instructionET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String[] items = getResources().getStringArray(R.array.instruction_arrays);
                AlertDialog.Builder ab = new AlertDialog.Builder(AddMedicineTwo.this);
                ab.setTitle(getResources().getString(R.string.select_intruction));
                ab.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface d, int choice) {
                        instructionET.setText(items[choice]);
                    }
                });
                AlertDialog alert = ab.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });

        final EditText doseTimeET = (EditText) newView.findViewById(R.id.doseTimeET);
        doseTimeET.setText(flowValue);
        final Calendar calendar = Calendar.getInstance();
        doseTimeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {
                        doseTimeET.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                        //Toast.makeText(getApplicationContext(), "new time:" + hourOfDay + ":" + minute, Toast.LENGTH_LONG).show();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });


        Button btnRemove = (Button) newView
                .findViewById(R.id.removeDoseBT);
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("ddd", "" + newView);
                if (linearLayoutForm.getChildCount() > 1) {
                    linearLayoutForm.removeView(newView);

                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_allowed),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        linearLayoutForm.addView(newView);

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


    public class AddMedicine extends AsyncTask<Void, Void, JSONObject> {


        AddMedicine() {
            try {
                mProgress.show();
            }
            catch (Exception e){

            }

        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.MEDICINE_DETAILS, charset);
                multipart.addFormField("action", "insert_medicine_details");
                multipart.addFormField("med_name", medicineNameSTR);
                multipart.addFormField("med_strength", medicineStrengthStr);
                multipart.addFormField("med_tablets", tabletsSTR);
                multipart.addFormField("med_rute", medicineRouteSTR);
                multipart.addFormField("med_frequency", frequencySTR);
                multipart.addFormField("med_start_date", startDateSTR);
                multipart.addFormField("med_end_date", endDateSTR);
                multipart.addFormField("med_week_sun", sunBLN + "");
                multipart.addFormField("med_week_mon", monBLN + "");
                multipart.addFormField("med_week_tus", tueBLN + "");
                multipart.addFormField("med_week_wed", wedBLN + "");
                multipart.addFormField("med_week_thus", thuBLN + "");
                multipart.addFormField("med_week_fri", friBLN + "");
                multipart.addFormField("med_week_sat", satBLN + "");
                multipart.addFormField("med_patient_id", patient_id);
                multipart.addFormField("med_patient_name", patient_name);
                multipart.addFormField("med_added_by", sharedpreferences.getString(PreferencesConstants.SessionManager.MY_PERSON_NAME, "NA"));
                multipart.addFormField("med_added_by_id", sharedpreferences.getString(PreferencesConstants.SessionManager.USER_ID, "NA"));
                multipart.addFormField("dosage", jsonArray.toString());

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
            mProgress.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("success")) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.medicine_added_successfully), Toast.LENGTH_LONG).show();
                        Intent intent1 = getIntent();
                        setResult(RESULT_OK, intent1);
                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}

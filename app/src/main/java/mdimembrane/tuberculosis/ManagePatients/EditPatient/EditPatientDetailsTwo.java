package mdimembrane.tuberculosis.ManagePatients.EditPatient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class EditPatientDetailsTwo extends AppCompatActivity {

    EditText aadharNoET,phoneNoET,relativeMobileNoET,address1ET,address2ET;
    RadioButton MaleRB, FemaleRB, OtherRB;
    SharedPreferences sharedpreferences;
    final int NEXT_ACTIVITY = 10;
    private RadioGroup genderRadioGroup;
    String genderSTR = "Male";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_details_two);

        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }
        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);



        aadharNoET=(EditText)findViewById(R.id.aadharEditText);
        phoneNoET=(EditText)findViewById(R.id.mobileEditText);
        relativeMobileNoET=(EditText)findViewById(R.id.relativeMobileEditText);
        address1ET=(EditText)findViewById(R.id.address1EditText);
        address2ET=(EditText)findViewById(R.id.address2EditText);

        genderRadioGroup = (RadioGroup) findViewById(R.id.genderRadioGroup);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.radioButton) {
                    genderSTR = "Male";
                } else if (checkedId == R.id.radioButton2) {
                    genderSTR = "Female";
                } else {
                    genderSTR = "Other";
                }
            }

        });


        aadharNoET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_AADHAR_NO,"NA"));
        phoneNoET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_PHONE,"NA"));
        address1ET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ADDRESS1,"NA"));
        address2ET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_ADDRESS2,"NA"));

        if(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GENDER,"NA").equals("Male"))
        {
            findViewById(R.id.radioButton).setSelected(true);
        }else if(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GENDER,"NA").equals("Female"))
        {
            findViewById(R.id.radioButton2).setSelected(true);
        }else if(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GENDER,"NA").equals("Other"))
        {
            findViewById(R.id.radioButton3).setSelected(true);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


        if (requestCode == NEXT_ACTIVITY && resultCode == RESULT_OK) {
            Intent intent1 = getIntent();
            setResult(RESULT_OK, intent1);
            finish();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void backButton(View view)
    {
        finish();
    }
    public void nextButton(View view)
    {
        if(aadharNoET.getText().toString().length()<12){

            aadharNoET.setError(getResources().getString(R.string.aadhar_no_validation));
            aadharNoET.requestFocus();
            return;
        }
        if(phoneNoET.getText().toString().length()<10){

            phoneNoET.setError(getResources().getString(R.string.phone_no_validation));
            phoneNoET.requestFocus();
            return;
        }
        if(address1ET.getText().toString().equals("")){

            address1ET.setError(getResources().getString(R.string.address1_validation));
            address1ET.requestFocus();
            return;
        }
        if(address2ET.getText().toString().equals("")){

            address2ET.setError(getResources().getString(R.string.address2_validation));
            address2ET.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_AADHAR_NO, aadharNoET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_PHONE, phoneNoET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_PHONE, relativeMobileNoET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_ADDRESS1, address1ET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_ADDRESS2, address2ET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_GENDER, genderSTR);
        editor.commit();

        Intent intent=new Intent(getApplicationContext(),EditPatientDetailsThree.class);
        startActivityForResult(intent, NEXT_ACTIVITY);
    }

}

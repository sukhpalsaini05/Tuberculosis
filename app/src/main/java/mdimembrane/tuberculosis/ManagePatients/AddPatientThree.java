package mdimembrane.tuberculosis.ManagePatients;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class AddPatientThree extends AppCompatActivity {

    CheckBox coughCB,hemoptysisCB,feverCB,weightLoseCB,wheezingCB,nightSweatsCB,fatigueCB,lymphadenpopathyCB,headacheCB,jointPainCB;
    EditText othersymptomsET;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_three);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        coughCB=(CheckBox)findViewById(R.id.coughCheckBox);
        hemoptysisCB=(CheckBox)findViewById(R.id.hemoptysisCheckBox);
        feverCB=(CheckBox)findViewById(R.id.feverCheckBox);
        weightLoseCB=(CheckBox)findViewById(R.id.weightLoseCheckBox);
        wheezingCB=(CheckBox)findViewById(R.id.wheezingCheckBox);
        nightSweatsCB=(CheckBox)findViewById(R.id.nightSweatsCheckBox);
        fatigueCB=(CheckBox)findViewById(R.id.fatigueCheckBox);
        lymphadenpopathyCB=(CheckBox)findViewById(R.id.lymphadenpopathyCheckBox);
        headacheCB=(CheckBox)findViewById(R.id.headacheCheckBox);
        jointPainCB=(CheckBox)findViewById(R.id.jointPainCheckBox);

        othersymptomsET=(EditText)findViewById(R.id.symptomsEditText);
    }

    public void nextButton(View view)
    {
        StringBuilder result=new StringBuilder();
        result.append("Selected Symptoms:\n");
        if(coughCB.isChecked()){
            result.append("\nCough/Sputum > 3 Weeks ");
        }
        if(hemoptysisCB.isChecked()){
            result.append("\nHemoptysis ");
        }
        if(feverCB.isChecked()){
            result.append("\nFever ");
        }
        if(weightLoseCB.isChecked()){
            result.append("\nWeight Lose / Anorexia ");
        }
        if(wheezingCB.isChecked()){
            result.append("\nWheezing/SOB ");
        }
        if(nightSweatsCB.isChecked()){
            result.append("\nNight Sweats ");
        }
        if(fatigueCB.isChecked()){
            result.append("\nFatigue ");
        }
        if(lymphadenpopathyCB.isChecked()){
            result.append("\nLymphadenpopathy ");
        }
        if(headacheCB.isChecked()){
            result.append("\nHeadache ");
        }
        if(jointPainCB.isChecked()){
            result.append("\nJoint Pain ");
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.AddNewPatient.OTHER_SYMPTOMS, othersymptomsET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.SYMPTOMS_LIST, String.valueOf(result));
        editor.commit();

        Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();

        Intent intent=new Intent(getApplicationContext(),AddPatientFour.class);
        startActivity(intent);
    }
    public void backButton(View view)
    {
        finish();
    }
}

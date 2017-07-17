package mdimembrane.tuberculosis.ManagePatients;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import mdimembrane.tuberculosis.main.R;

public class AddPatientThree extends AppCompatActivity {

    CheckBox coughCB,hemoptysisCB,feverCB,weightLoseCB,wheezingCB,nightSweatsCB,fatigueCB,lymphadenpopathyCB,headacheCB,jointPainCB;

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

    }

    public void nextButton(View view)
    {


        Intent intent=new Intent(getApplicationContext(),AddPatientFour.class);
        startActivity(intent);
    }
    public void backButton(View view)
    {
        finish();
    }
}

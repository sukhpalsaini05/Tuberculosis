package mdimembrane.tuberculosis.ManagePatients;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mdimembrane.tuberculosis.main.R;

public class AddPatientThree extends AppCompatActivity {
    Spinner bloodGroupSP;
    EditText weightET,heightET,otherDiseaseET,commentET;

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

        bloodGroupSP=(Spinner)findViewById(R.id.bloodGroupSpinner);
        weightET=(EditText)findViewById(R.id.weightEditText);
        heightET=(EditText)findViewById(R.id.heightEditText);
        otherDiseaseET=(EditText)findViewById(R.id.otherDiseaseEditText);
        commentET=(EditText)findViewById(R.id.commentEditText);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    public void backButton(View view)
    {
        finish();
    }
    public void nextButton(View view)
    {


        if(bloodGroupSP.getSelectedItemPosition()==0)
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_select_blood_group),Toast.LENGTH_SHORT).show();
            return;
        }
        if(weightET.getText().toString().equals("")){

            weightET.setError(getResources().getString(R.string.weight_validation));
            weightET.requestFocus();
            return;
        }
        if(heightET.getText().toString().equals("")){

            heightET.setError(getResources().getString(R.string.height_validation));
            heightET.requestFocus();
            return;
        }

    }
}

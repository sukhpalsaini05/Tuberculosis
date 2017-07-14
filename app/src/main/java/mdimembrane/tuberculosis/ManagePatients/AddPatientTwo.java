package mdimembrane.tuberculosis.ManagePatients;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import mdimembrane.tuberculosis.main.R;

public class AddPatientTwo extends AppCompatActivity {

    EditText aadharNoET,phoneNoET,relativeMobileNoET,address1ET,address2ET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_two);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }


        aadharNoET=(EditText)findViewById(R.id.aadharEditText);
        phoneNoET=(EditText)findViewById(R.id.mobileEditText);
        relativeMobileNoET=(EditText)findViewById(R.id.relativeMobileEditText);
        address1ET=(EditText)findViewById(R.id.address1EditText);
        address2ET=(EditText)findViewById(R.id.address2EditText);

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





        Intent intent=new Intent(getApplicationContext(),AddPatientThree.class);
        startActivity(intent);
    }
}

package mdimembrane.tuberculosis.NewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import mdimembrane.tuberculosis.main.LoginActivity;
import mdimembrane.tuberculosis.main.R;

public class NewAccountOne extends AppCompatActivity {

    Button NextButton, CancleButton;
    Spinner AccountTypeSP;
    EditText NameET,EmployeeCodeET;
    RadioButton MaleRB,FemaleRB,OtherRB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     //   Toast.makeText(getApplicationContext(),"Account Request",Toast.LENGTH_LONG).show();
     //   Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
    //    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();


        AccountTypeSP=(Spinner)findViewById(R.id.AccountTypeSP);

        NameET=(EditText)findViewById(R.id.nameEditText);
        EmployeeCodeET=(EditText)findViewById(R.id.employeeEditText);

        NextButton=(Button)findViewById(R.id.button5);
        CancleButton=(Button)findViewById(R.id.button);


        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AccountTypeSP.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.TostAccountType),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(NameET.getText().toString().equals("")){

                    NameET.setError("Please Enter Name");
                    NameET.requestFocus();
                    return;
                }
                if(EmployeeCodeET.getText().toString().equals("")){

                    EmployeeCodeET.setError("Please Enter Employee Code");
                    EmployeeCodeET.requestFocus();
                    return;
                }
                Intent intent=new Intent(getApplicationContext(),NewAccountTwo.class);
                startActivity(intent);
            }
        });

        CancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              finish();
            }
        });
    }
}

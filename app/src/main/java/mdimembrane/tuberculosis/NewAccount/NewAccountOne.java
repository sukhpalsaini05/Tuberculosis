package mdimembrane.tuberculosis.NewAccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mdimembrane.tuberculosis.main.LoginActivity;
import mdimembrane.tuberculosis.main.R;

public class NewAccountOne extends AppCompatActivity {

    Button NextButton, CancleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

     //   Toast.makeText(getApplicationContext(),"Account Request",Toast.LENGTH_LONG).show();

     //   Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
    //    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();


        NextButton=(Button)findViewById(R.id.button5);
        CancleButton=(Button)findViewById(R.id.button);


        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

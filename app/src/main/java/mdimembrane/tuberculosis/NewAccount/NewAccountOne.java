package mdimembrane.tuberculosis.NewAccount;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import mdimembrane.tuberculosis.ManagePatients.AddPatientTwo;
import mdimembrane.tuberculosis.main.LoginActivity;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;

public class NewAccountOne extends AppCompatActivity {

    final int TAKE_PICTURE = 1;
    final int CAMERA_REQUEST_CODE=1;
    ImageButton UserImageIMB;
    File image;

    Button NextButton, CancleButton;
    Spinner AccountTypeSP;
    EditText NameET,EmployeeCodeET;
    RadioButton MaleRB,FemaleRB,OtherRB;
    SharedPreferences sharedpreferences;
    private RadioGroup genderRadioGroup;
    String genderSTR="Male";
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean takePicFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        UserImageIMB=(ImageButton)findViewById(R.id.userImageIMB);


        //   Toast.makeText(getApplicationContext(),"Account Request",Toast.LENGTH_LONG).show();
     //   Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_LONG).show();
    //    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();


        AccountTypeSP=(Spinner)findViewById(R.id.AccountTypeSP);

        NameET=(EditText)findViewById(R.id.nameEditText);
        EmployeeCodeET=(EditText)findViewById(R.id.employeeEditText);

        NextButton=(Button)findViewById(R.id.button5);
        CancleButton=(Button)findViewById(R.id.button);

        genderRadioGroup=(RadioGroup) findViewById(R.id.genderRadioGroup);

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton) {
                    genderSTR="Male";
                } else if(checkedId == R.id.radioButton2) {
                    genderSTR="Female";
                } else {
                    genderSTR="Other";
                }
            }

        });
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(AccountTypeSP.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.TostAccountType),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!takePicFlag)
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_take_image),Toast.LENGTH_SHORT).show();
                    return;
                }
                if(NameET.getText().toString().equals("")){

                    NameET.setError(getResources().getString(R.string.name_validation));
                    NameET.requestFocus();
                    return;
                }
                if(EmployeeCodeET.getText().toString().equals("")){

                    EmployeeCodeET.setError(getResources().getString(R.string.employee_code_validation));
                    EmployeeCodeET.requestFocus();
                    return;
                }

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(PreferencesConstants.AddNewAccount.ACCOUNT_TYPE, AccountTypeSP.getSelectedItem().toString());
                editor.putString(PreferencesConstants.AddNewAccount.USER_NAME, NameET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.EMPLOYEE_CODE, EmployeeCodeET.getText().toString());
                editor.putString(PreferencesConstants.AddNewAccount.GENDER, genderSTR);
                editor.commit();


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

    public void TakePicture(View view)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else
        {
            CameraPermission();
        }
    }

    public void CameraPermission()
    {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        }else
        {
            TakeUserPicture();
        }
    }
    public void TakeUserPicture()
    {
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        image = new File(imagesFolder, "user_pic.jpg");
        Uri uriSavedImage = Uri.fromFile(image);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(cameraIntent, TAKE_PICTURE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                } else {
                    TakeUserPicture();
                }
                return;
            }
            case REQUEST_EXTERNAL_STORAGE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {
                } else {
                    CameraPermission();
                }
                return;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {

            try {

                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.fromFile(image));
                UserImageIMB.setImageBitmap(photo);
                UserImageIMB.setRotation(90);
                takePicFlag=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}

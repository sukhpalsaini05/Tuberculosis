package mdimembrane.tuberculosis.ManagePatients;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.util.CompressFile;

public class AddPatientOne extends AppCompatActivity {

    EditText nameET,guardianNameET,ageET;
    Spinner guardianTypeSP;
    RadioButton MaleRB,FemaleRB,OtherRB;
    private RadioGroup genderRadioGroup;
    SharedPreferences sharedpreferences;
    String genderSTR="Male";


    final int TAKE_PICTURE = 1;
    final int CAMERA_REQUEST_CODE=1;
    ImageButton patientImageIMB;
    File image;
    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    boolean takePicFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_one);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch(NullPointerException e){
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        patientImageIMB=(ImageButton)findViewById(R.id.patientImageIMB);
        nameET=(EditText)findViewById(R.id.nameEditText);
        guardianNameET=(EditText)findViewById(R.id.guardianNameEditText);
        ageET=(EditText)findViewById(R.id.ageEditText);
        guardianTypeSP=(Spinner)findViewById(R.id.guardianTypeSpinner);
        genderRadioGroup=(RadioGroup)findViewById(R.id.genderRadioGroup);


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
                new CompressFile(getApplicationContext()).compressImageFile(image.toString());
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(),Uri.fromFile(image));

                patientImageIMB.setImageBitmap(photo);
                //patientImageIMB.setRotation(90);
                takePicFlag=true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void backButton(View view)
    {
        finish();
    }


    public void nextButton(View view)
    {

        if(!takePicFlag)
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_take_image),Toast.LENGTH_SHORT).show();
            return;
        }
        if(nameET.getText().toString().equals("")){

            nameET.setError(getResources().getString(R.string.patient_name_validation));
            nameET.requestFocus();
            return;
        }
        if(guardianTypeSP.getSelectedItemPosition()==0)
        {
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_select_guardian_type),Toast.LENGTH_SHORT).show();
            return;
        }


        if(guardianNameET.getText().toString().equals("")){

            guardianNameET.setError(getResources().getString(R.string.guardian_name_validation));
            guardianNameET.requestFocus();
            return;
        }
        if(ageET.getText().toString().equals("")){

            ageET.setError(getResources().getString(R.string.patient_age_validation));
            ageET.requestFocus();
            return;
        }


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.AddNewPatient.PATIENT_NAME, nameET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.GAURDIAN_TYPE, guardianTypeSP.getSelectedItem().toString());
        editor.putString(PreferencesConstants.AddNewPatient.GAURDIAN_NAME, guardianNameET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.AGE, ageET.getText().toString());
        editor.putString(PreferencesConstants.AddNewPatient.GENDER, genderSTR);
        editor.commit();


        Intent intent=new Intent(getApplicationContext(),AddPatientTwo.class);
        startActivity(intent);
    }


}




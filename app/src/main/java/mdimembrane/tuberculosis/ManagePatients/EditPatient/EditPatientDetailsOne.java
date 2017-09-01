package mdimembrane.tuberculosis.ManagePatients.EditPatient;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.ServerConfiguration.MultipartUtility;
import mdimembrane.tuberculosis.ServerConfiguration.ServerConstants;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.util.CompressFile;

public class EditPatientDetailsOne extends AppCompatActivity {


    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    final int TAKE_PICTURE = 1;
    final int NEXT_ACTIVITY = 10;
    final int CAMERA_REQUEST_CODE = 1;
    File image;
    boolean takePicFlag = false;

    EditText nameET,guardianNameET,ageET;
    Spinner categorySP,guardianTypeSP;
    ImageButton patientImageIMB;

    String categorySTR="null";
    List<String> categoryList = new ArrayList<String>();
    List<String> categoryId = new ArrayList<String>();
    String categoryIdStr="0";
    String MSG = "";
    
    SharedPreferences sharedpreferences;
    private ProgressDialog mProgress;
    String patient_id, patient_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient_details_one);

        sharedpreferences = getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);
        
        mProgress = new ProgressDialog(this);
        String titleId = getResources().getString(R.string.loading_data);
        mProgress.setTitle(titleId);
        mProgress.setMessage(getResources().getString(R.string.please_wait));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            patient_id = bundle.getString("PATIENT_ID");
            patient_name = bundle.getString("PATIENT_NAME");
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        nameET=(EditText)findViewById(R.id.nameEditText);
        guardianNameET=(EditText)findViewById(R.id.guardianNameEditText);
        ageET=(EditText)findViewById(R.id.ageEditText);

        categorySP=(Spinner)findViewById(R.id.categoryTypeSP);
        guardianTypeSP=(Spinner)findViewById(R.id.guardianTypeSpinner);

        patientImageIMB = (ImageButton) findViewById(R.id.patientImageIMB);

        //  new categoryRequest().execute();




        ConnectivityManager cn=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(getApplicationContext(), "Internet Connection Available", Toast.LENGTH_LONG).show();

                new categoryRequest().execute();

            } else {
                Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();

            }

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

    public void SetProfilePicture()
    {
        Bitmap  bitmap2;
        ByteArrayOutputStream bytearrayoutputstream;
        bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] BYTE;
        Matrix matrix = new Matrix();


        byte[] qrimage = Base64.decode(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_IMAGE,"NA").getBytes(), 1);
        Bitmap sourceBitmap = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,20,bytearrayoutputstream);
        BYTE = bytearrayoutputstream.toByteArray();
        bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        imagesFolder.mkdirs(); // <----
        image = new File(imagesFolder, "user_pic.jpg");

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(image);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        patientImageIMB.setImageBitmap(bitmap2);
        takePicFlag=true;
    }
    
    public void backButton(View view) {
        finish();
    }
    public void nextButton(View view) {

        if (!takePicFlag) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_take_image), Toast.LENGTH_SHORT).show();
            return;
        }
        if (nameET.getText().toString().equals("")) {

            nameET.setError(getResources().getString(R.string.patient_name_validation));
            nameET.requestFocus();
            return;
        }
        if (guardianTypeSP.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_select_guardian_type), Toast.LENGTH_SHORT).show();
            return;
        }
        if (categorySP.getSelectedItemPosition() == 0){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_select_category_type), Toast.LENGTH_SHORT).show();
            return;
        }


        if (guardianNameET.getText().toString().equals("")) {

            guardianNameET.setError(getResources().getString(R.string.guardian_name_validation));
            guardianNameET.requestFocus();
            return;
        }
        if (ageET.getText().toString().equals("")) {

            ageET.setError(getResources().getString(R.string.patient_age_validation));
            ageET.requestFocus();
            return;
        }

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_NAME, nameET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_ID,patient_id);
        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_TYPE, guardianTypeSP.getSelectedItem().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_CATEGORY_TYPE, categorySP.getSelectedItem().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_CATEGORY_NO, categoryIdStr);
        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_NAME, guardianNameET.getText().toString());
        editor.putString(PreferencesConstants.EditPatient.EDIT_AGE, ageET.getText().toString());
        editor.commit();
        
        Intent intent = new Intent(getApplicationContext(), EditPatientDetailsTwo.class);
        startActivityForResult(intent, NEXT_ACTIVITY);

    }



    public void TakePicture(View view) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        } else {
            CameraPermission();
        }
    }

    public void CameraPermission() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_REQUEST_CODE);
        } else {
            TakeUserPicture();
        }
    }


    public void TakeUserPicture() {
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
                Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(image));

                patientImageIMB.setImageBitmap(photo);
                //patientImageIMB.setRotation(90);
                takePicFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == NEXT_ACTIVITY && resultCode == RESULT_OK) {
            finish();
        }
    }


    public class EditPatient extends AsyncTask<Void, Void, JSONObject> {


        EditPatient() {
            mProgress.show();
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.PATIENT_DATA, charset);
                multipart.addFormField("action", "get_patient_profile");
                multipart.addFormField("patient_id", patient_id);
                multipart.addFormField("close","close");
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
            
            String MSG = "";
            boolean RESPONSE_CODE;
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                if (RESPONSE_CODE) {
                    if (MSG.equals("OK")) {
                        String p_image = json.getString("image_data");
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        JSONObject jsonChildNode = jsonMainNode
                                .getJSONObject(0);
                        String patient_name = jsonChildNode.optString("P_Name").toString();
                        String category_type = jsonChildNode.optString("P_category_type").toString();
                        String category_no = jsonChildNode.optString("P_category_no").toString();
                        String patient_guardian_type = jsonChildNode.optString("P_Guardian_Type").toString();
                        String patient_guardian_name = jsonChildNode.optString("P_Guardian_Name").toString();
                        String patient_gender = jsonChildNode.optString("P_Gender").toString();
                        String patient_age = jsonChildNode.optString("P_Age").toString();
                        String patient_adhar_card_no = jsonChildNode.optString("P_Adhar_card_no").toString();
                        String patient_phone = jsonChildNode.optString("P_Phone_no").toString();
                        String patient_relative_phn_no = jsonChildNode.optString("P_Relative_phn_no").toString();
                        String patient_state = jsonChildNode.optString("P_State").toString();
                        String patient_district = jsonChildNode.optString("P_District").toString();
                        String patient_tehsil = jsonChildNode.optString("P_Tehsil").toString();
                        String patient_address1 = jsonChildNode.optString("P_Address1").toString();
                        String patient_address2 = jsonChildNode.optString("P_Address2").toString();
                        String patient_blood_group = jsonChildNode.optString("P_Blood_Group").toString();
                        String patient_weight = jsonChildNode.optString("P_Weight").toString();
                        String patient_height = jsonChildNode.optString("P_Height").toString();
                        String patient_other_diseases = jsonChildNode.optString("P_Other_Diseases").toString();
                        String patient_any_comment = jsonChildNode.optString("P_Any_comment").toString();


                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_NAME, patient_name);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_CATEGORY_TYPE, category_type);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_CATEGORY_NO, category_no);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_TYPE, patient_guardian_type);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_NAME, patient_guardian_name);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_IMAGE, p_image);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_GENDER, patient_age);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_AGE, patient_gender);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_AADHAR_NO, patient_adhar_card_no);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_PATIENT_PHONE, patient_phone);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_PHONE, patient_relative_phn_no);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_P_STATE, patient_state);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_P_DISTT, patient_district);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_P_TEHSIL, patient_tehsil);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_ADDRESS1, patient_address1);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_ADDRESS2, patient_address2);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_BLOOD_GROUP, patient_blood_group);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_WEIGHT, patient_weight);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_HEIGHT, patient_height);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_ANY_OTHER_DISEASES, patient_other_diseases);
                        editor.putString(PreferencesConstants.EditPatient.EDIT_COMMENTS, patient_any_comment);
                        editor.commit();


                        if(patient_guardian_type.equals("S/O"))
                        {
                            guardianTypeSP.setSelection(1);
                        }else if(patient_guardian_type.equals("D/O"))
                        {
                            guardianTypeSP.setSelection(2);
                        }
                        else if(patient_guardian_type.equals("W/O"))
                        {
                            guardianTypeSP.setSelection(3);
                        }

                        nameET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_PATIENT_NAME,"NA"));
                        guardianNameET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_GAURDIAN_NAME,"NA"));
                        ageET.setText(sharedpreferences.getString(PreferencesConstants.EditPatient.EDIT_AGE,"NA"));
                        categorySP.setSelection(categoryList.indexOf(category_type));

                        SetProfilePicture();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mProgress.dismiss();
    
        }

    }


    private void allCategory() {
        // TODO Auto-generated method stub
        categoryList.add(0,getResources().getString(R.string.select_category));
        categorySP = (Spinner) findViewById(R.id.categoryTypeSP);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryList);
        dataAdapter.setDropDownViewResource(R.layout.drpdown_item);
        categorySP.setAdapter(dataAdapter);
        categorySP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorySTR="";
                categorySTR=categoryList.get(position);

                if(position>0)
                {
                    categoryIdStr=categoryId.get(position-1);
                    Log.i("Selected Id",categoryIdStr);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class categoryRequest extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPatientDetailsOne.this);
            pDialog.setMessage(getResources().getString(R.string.DiaglogBox));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            // TODO: attempt authentication against a network service.

            JSONObject json = null;
            String responseSTR = "";
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(ServerConstants.PATIENT_DATA, charset);
                multipart.addFormField("action", "get_categories");
                multipart.addFormField("close", "close");
                List<String> response = multipart.finish();

                Log.v("rht", "SERVER REPLIED:");

                for (String line : response) {
                    Log.v("rht", "Line : " + line);
                    responseSTR = line;
                }
                json = new JSONObject(responseSTR);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            boolean RESPONSE_CODE;
            pDialog.dismiss();
            try {
                RESPONSE_CODE = json.getBoolean("response");
                MSG = json.getString("message");
                // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
                if (RESPONSE_CODE) {
                    try {
                        if (MSG.equals("OK")) {
                            categoryList.clear();
                            categoryId.clear();
                        }
                        JSONArray jsonMainNode = json.optJSONArray("data");
                        int lengthJsonArr = jsonMainNode.length();
                        for (int i = 0; i < lengthJsonArr; i++) {
                            JSONObject jsonChildNode = jsonMainNode
                                    .getJSONObject(i);
                            String id = jsonChildNode.optString("category_no")
                                    .toString();
                            String data = jsonChildNode.optString("category_type")
                                    .toString();
                            categoryId.add(id);
                            categoryList.add(data);

                        }
                        allCategory();
                        new EditPatient().execute();

                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

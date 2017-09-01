package mdimembrane.tuberculosis.ManagePatients.PatientProfile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import mdimembrane.tuberculosis.ManagePatients.ManagePatientList;
import mdimembrane.tuberculosis.main.FileHandling;
import mdimembrane.tuberculosis.main.LoginActivity;
import mdimembrane.tuberculosis.main.MainScreen;
import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;


public class GeneralInformationFragment extends Fragment {

    SharedPreferences sharedpreferences;
    ImageView profileIMV;
    private static final String TAG = LoginActivity.class.getSimpleName();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_general_information, container, false);

       // profileIMB=(ImageButton) view.findViewById(R.id.patient_profile_photo);

        profileIMV=(ImageView) view.findViewById(R.id.patient_profile_photo);

        sharedpreferences = getActivity().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        TextView idTV=(TextView) view.findViewById(R.id.idTextView);
        TextView nameTV=(TextView) view.findViewById(R.id.nameTextView);
        TextView categoryNoTV=(TextView) view.findViewById(R.id.categoryNoTextView);
        TextView categoryTypeTV=(TextView) view.findViewById(R.id.categoryTypeTextView);
        TextView statusTV=(TextView) view.findViewById(R.id.patientStatusTextView);
        TextView gaurdianTypeTV=(TextView) view.findViewById(R.id.gTypeTextView);
        TextView gaurdianNameTV=(TextView) view.findViewById(R.id.gNameTextView);
        TextView ageTV=(TextView) view.findViewById(R.id.ageTextView);
        TextView genderTV=(TextView) view.findViewById(R.id.genderTextView);


        idTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.PATIENT_ID,"NA"));
        nameTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.PATIENT_NAME,"NA"));
        categoryNoTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.CATEGORY_NO,"NA"));
        categoryTypeTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.CATEGORY_TYPE,"NA"));
        statusTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.STATUS,"NA"));
        gaurdianTypeTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.GAURDIAN_TYPE,"NA"));
        gaurdianNameTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.GAURDIAN_NAME,"NA"));
        ageTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.AGE,"NA"));
        genderTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.GENDER,"NA"));


        SetProfilePicture();
        Log.i("hello","General");
        return view;
    }

    public void SetProfilePicture()
    {
        Bitmap  bitmap2;
        ByteArrayOutputStream bytearrayoutputstream;
        bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] BYTE;
        Matrix matrix = new Matrix();


        byte[] qrimage = Base64.decode(sharedpreferences.getString(PreferencesConstants.PatientProfile.IMAGE,"NA").getBytes(), 1);
        Bitmap sourceBitmap = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);

        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG,20,bytearrayoutputstream);
        BYTE = bytearrayoutputstream.toByteArray();
        bitmap2 = BitmapFactory.decodeByteArray(BYTE,0,BYTE.length);

        profileIMV.setImageBitmap(bitmap2);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
      //  getActivity().setTitle("General Informatiom");
    }

//        protected void onPostExecute(JSONObject json) {
//       // mAuthTask = null;
//        String MSG = "";
//        boolean RESPONSE_CODE;
//        try {
//            RESPONSE_CODE = json.getBoolean("response");
//            MSG = json.getString("message");
//            // Log.i("dfdfdf", ""+MSG+"   "+RESPONSE_CODE);
//            if (RESPONSE_CODE) {
//                if (MSG.equals("OK")) {
//                    try {
//                        String Qrimage = json.getString("image_data");
//                        byte[] qrimage = Base64.decode(Qrimage.getBytes(), 1);
//                        Bitmap bmp = BitmapFactory.decodeByteArray(qrimage, 0, qrimage.length);
//
//                    }catch (Exception e){
//                        Log.d(TAG, "Error accessing file: " + e.getMessage());
//                    }
//
//                    SharedPreferences.Editor editor = sharedpreferences.edit();
//                    editor.putString(PreferencesConstants.PatientProfile.PATIENT_ID, json.getString("P_Unique_Generated_Id"));
//                    editor.putString(PreferencesConstants.PatientProfile.PATIENT_NAME, json.getString("P_Name"));
//                    editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_TYPE, json.getString("P_Guardian_Type"));
//                    editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_NAME, json.getString("P_Guardian_Name"));
//                    editor.putString(PreferencesConstants.PatientProfile.Image, json.getString("P_image"));
//                    editor.putString(PreferencesConstants.PatientProfile.GENDER, json.getString("P_Gender"));
//                    editor.putString(PreferencesConstants.PatientProfile.AGE, json.getString("P_Age"));
//                    editor.putString(PreferencesConstants.PatientProfile.PATIENT_AADHAR_NO, json.getString("P_Phone_no"));
//                    editor.putString(PreferencesConstants.PatientProfile.PATIENT_PHONE, json.getString("P_Adhar_card_no"));
//                    editor.putString(PreferencesConstants.PatientProfile.GAURDIAN_PHONE, json.getString("P_Relative_phn_no"));
//                    editor.putString(PreferencesConstants.PatientProfile.DATE, json.getString("P_Registration_Date_time"));
//                    editor.commit();
//
//
//                } else {
//                   // ErrorAlert(json.getString("data").toString());
//                }
//
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//    }

}

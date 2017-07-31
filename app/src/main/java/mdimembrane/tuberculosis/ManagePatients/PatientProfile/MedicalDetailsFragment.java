package mdimembrane.tuberculosis.ManagePatients.PatientProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mdimembrane.tuberculosis.main.PreferencesConstants;
import mdimembrane.tuberculosis.main.R;


public class MedicalDetailsFragment extends Fragment {

    SharedPreferences sharedpreferences;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_medical__details, container, false);

        sharedpreferences = getActivity().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        TextView symptomsTV=(TextView) view.findViewById(R.id.symptomsTextView);
        TextView bloodGroupTV=(TextView) view.findViewById(R.id.bloodGroupTextView);
        TextView weightTV=(TextView) view.findViewById(R.id.weightTextView);
        TextView heightTV=(TextView) view.findViewById(R.id.heightTextView);
        TextView otherDiseaseTV=(TextView) view.findViewById(R.id.otherDiseaseTextView);
        TextView commentsTV=(TextView) view.findViewById(R.id.commentsTextView);


        symptomsTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.SYMPTOMS_LIST,"NA"));
        bloodGroupTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.BLOOD_GROUP,"NA"));
        weightTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.WEIGHT,"NA"));
        heightTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.HEIGHT,"NA"));
        otherDiseaseTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.ANY_OTHER_DISEASES,"NA"));
        commentsTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.COMMENTS,"NA"));


        Log.i("hello","Medical");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getActivity().setTitle("Medical Details");
    }
}

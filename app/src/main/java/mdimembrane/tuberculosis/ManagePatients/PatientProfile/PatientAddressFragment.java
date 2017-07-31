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


public class PatientAddressFragment extends Fragment {

    SharedPreferences sharedpreferences;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_patient_address, container, false);

        sharedpreferences = getActivity().getSharedPreferences(PreferencesConstants.APP_MAIN_PREF, Context.MODE_PRIVATE);

        TextView stateTV=(TextView) view.findViewById(R.id.stateTextView);
        TextView districtTV=(TextView) view.findViewById(R.id.districtTextView);
        TextView tehsilTV=(TextView) view.findViewById(R.id.tehsilTextView);
        TextView address1TV=(TextView) view.findViewById(R.id.address1TextView);
        TextView address2TV=(TextView) view.findViewById(R.id.address2TextView);


        stateTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.P_STATE,"NA"));
        districtTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.P_DISTT,"NA"));
        tehsilTV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.P_TEHSIL,"NA"));
        address1TV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.ADDRESS1,"NA"));
        address2TV.setText(sharedpreferences.getString(PreferencesConstants.PatientProfile.ADDRESS2,"NA"));


        Log.i("hello","Address");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
    //    getActivity().setTitle("Patient Address");
    }
}

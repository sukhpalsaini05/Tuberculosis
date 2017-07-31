package mdimembrane.tuberculosis.main_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mdimembrane.tuberculosis.ManagePatients.ManagePatientList;
import mdimembrane.tuberculosis.ManageSamples.AddNewSampleOne;
import mdimembrane.tuberculosis.ManageSamples.AddSampleResultOne;
import mdimembrane.tuberculosis.main.R;


public class SampleMainFragment extends Fragment {

    public static final int ADD_NEW_SAMPLE = 4 , ADD_SAMPLE_RESULT = 5 ;

     @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_sample_main, container, false);

         Button newSampleBT = (Button) view.findViewById(R.id.addSampleBT);
         newSampleBT.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(getActivity(), ManagePatientList.class);
                 intent.putExtra("OPEN_ACTIVITY",ADD_NEW_SAMPLE);
                 startActivity(intent);
             }
         });

         Button sampleResultBT = (Button) view.findViewById(R.id.SampleResult);
         sampleResultBT.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(getActivity(), ManagePatientList.class);
                 intent.putExtra("OPEN_ACTIVITY",ADD_SAMPLE_RESULT);
                 startActivity(intent);
             }
         });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Samples");
    }
}

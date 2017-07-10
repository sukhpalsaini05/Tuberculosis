package mdimembrane.tuberculosis.main_fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mdimembrane.tuberculosis.main.R;

    public class ReportMainFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //returning our layout file
            //change R.layout.yourlayoutfilename for each of your fragments
            View view = inflater.inflate(R.layout.fragment_report_main, container, false);

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //you can set the title for your toolbar here for different fragments different titles
            getActivity().setTitle("Reports");
        }
    }

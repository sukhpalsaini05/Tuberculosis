package mdimembrane.tuberculosis.ManagePatients.PatientProfile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    GeneralInformationFragment tab1;
    PatientAddressFragment tab2;
    MedicalDetailsFragment tab3;
    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        tab1 = new GeneralInformationFragment();
        tab2 = new PatientAddressFragment();
        tab3 = new MedicalDetailsFragment();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                return tab1;
            case 1:

                return tab2;
            case 2:

                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
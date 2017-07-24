package mdimembrane.tuberculosis.ManagePatients;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import java.util.ArrayList;

import mdimembrane.tuberculosis.main.R;

public class ManagePatientList extends AppCompatActivity {
    ListView patientLV;
    ArrayList<String> data=new ArrayList<String>();
    ArrayList<String> index=new ArrayList<String>();


    Context context;

   // ArrayList prgmName;
   // public static int [] prgmImages={R.drawable.images,R.drawable.images1,R.drawable.images2,R.drawable.images3,R.drawable.images4,R.drawable.images5,R.drawable.images6,R.drawable.images7,R.drawable.images8};
    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_list);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }

        context = this;

        patientLV = (ListView) findViewById(R.id.patientListview);


    }

}

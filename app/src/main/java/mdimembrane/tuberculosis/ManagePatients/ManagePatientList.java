package mdimembrane.tuberculosis.ManagePatients;

import android.app.SearchManager;
import android.content.Context;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mdimembrane.tuberculosis.main.R;
import mdimembrane.tuberculosis.util.ListViewAdapter;
import mdimembrane.tuberculosis.util.PatientListModel;

public class ManagePatientList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView patientLV;
    ListViewAdapter lviewAdapter;


    ArrayList<PatientListModel> patientData=new ArrayList<PatientListModel>();
   // ArrayList<String> data=new ArrayList<String>();
  //  ArrayList<String> index=new ArrayList<String>();

  //  Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_patient_list);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
        }


        patientData.add(new PatientListModel("P_1","Anisha","9658478524","08/Aug/2017"));
        patientData.add(new PatientListModel("P_2","Ritika","8587459635","18/aug/2007"));
        patientData.add(new PatientListModel("P_3","Sukhpal","9568745258","10/sep/2017"));
        patientData.add(new PatientListModel("P_4","Bunty","9658745123","15/mar/2017"));
        patientData.add(new PatientListModel("P_5","Munish","9874512586","22/dec/2017"));
        patientData.add(new PatientListModel("P_6","Naman","7206063162","22/Aug/2017"));
        patientData.add(new PatientListModel("P_7","Sakshi","8567415869","23/april/2017"));
        patientData.add(new PatientListModel("P_8","Rajiv","8957458699","16/may/2017"));
        patientData.add(new PatientListModel("P_9","Rohan","9965845522","09/june/2017"));
        patientData.add(new PatientListModel("P_10","Karan","9878556622","05/May/2017"));
        patientData.add(new PatientListModel("P_11","Raman","7589663325","07/oct/2017"));
        patientData.add(new PatientListModel("P_12","Kamal","9685748599","19/sep/2017"));
        patientData.add(new PatientListModel("P_13","Lokesh","9685995588","28/aug/2017"));


        //   context = this;

        patientLV = (ListView) findViewById(R.id.patientListview);
        lviewAdapter = new ListViewAdapter(this, patientData);

        System.out.println("adapter => "+lviewAdapter.getCount());

        patientLV.setAdapter(lviewAdapter);

        patientLV.setOnItemClickListener(this);


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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_patient);
        SearchView searchView = (SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("hello",newText);
                lviewAdapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        Toast.makeText(getApplicationContext() ,"Selected", Toast.LENGTH_LONG).show();

        //Toast.makeText(this,"ID => "+ patient_id.get(position) +"=> Name"+ patient_name.get(position) +"phone No => "+ patient_phone.get(position) +"Date => "+ patient_date.get(position), Toast.LENGTH_SHORT).show();
    }


}

package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 26/7/17.
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import mdimembrane.tuberculosis.ListViewAdapters.PatientListModel;
import mdimembrane.tuberculosis.main.R;

public class ListViewAdapter extends BaseAdapter implements Filterable
{
    Activity context;
    ArrayList<PatientListModel> patinetData;
    ArrayList<PatientListModel> patinetDataOriginal;

    public ListViewAdapter(Activity context,  ArrayList<PatientListModel> patinetData) {
        super();
        this.context = context;
        this.patinetData = patinetData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return patinetData.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                patinetData = (ArrayList<PatientListModel>) results.values; // has

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                // results of a
                // filtering
                // operation in
                // values
                // List<String> FilteredArrList = new ArrayList<String>();
                List<PatientListModel> FilteredArrList = new ArrayList<PatientListModel>();

                if (patinetDataOriginal == null) {
                    patinetDataOriginal = new ArrayList<PatientListModel>(patinetData); // saves

                }

                /********
                 *
                 * If constraint(CharSequence that is received) is null returns
                 * the mOriginalValues(Original) values else does the Filtering
                 * and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = patinetDataOriginal.size();
                    results.values = patinetDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < patinetDataOriginal.size(); i++) {
                        PatientListModel model = patinetDataOriginal.get(i);

                        String data = model.getPatientName();
                        if (data.toLowerCase(locale).contains(constraint.toString())) {
                            FilteredArrList.add(model);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    private class ViewHolder {
        TextView patient_id;
        TextView patient_name;
        TextView patient_phone;
        TextView patient_date;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_patient_list, null);
            holder = new ViewHolder();
            holder.patient_id = (TextView) convertView.findViewById(R.id.textView1);
            holder.patient_name = (TextView) convertView.findViewById(R.id.textView2);
            holder.patient_phone = (TextView) convertView.findViewById(R.id.textView3);
            holder.patient_date = (TextView) convertView.findViewById(R.id.textView4);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        PatientListModel patientListModel=patinetData.get(position);
        holder.patient_id.setText(patientListModel.getPatientID());
        holder.patient_name.setText(patientListModel.getPatientName());
        holder.patient_phone.setText(patientListModel.getPatientPhone());
        holder.patient_date.setText(patientListModel.getPatientDate());

        return convertView;
    }


}

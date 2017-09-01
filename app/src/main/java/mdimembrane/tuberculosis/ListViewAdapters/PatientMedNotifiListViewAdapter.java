package mdimembrane.tuberculosis.ListViewAdapters;

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

import mdimembrane.tuberculosis.main.R;

/**
 * Created by root on 16/8/17.
 */

public class PatientMedNotifiListViewAdapter extends BaseAdapter implements Filterable {


    Activity context;
    ArrayList<PatientMedNotifiListModel> PatientMedNotifiData;
    ArrayList<PatientMedNotifiListModel> PatientMedNotifiDataOriginal;

    public PatientMedNotifiListViewAdapter(Activity context, ArrayList<PatientMedNotifiListModel> PatientMedNotifiData) {
        super();
        this.context = context;
        this.PatientMedNotifiData = PatientMedNotifiData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return PatientMedNotifiData.size();
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

                PatientMedNotifiData = (ArrayList<PatientMedNotifiListModel>) results.values; // has

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
                List<PatientMedNotifiListModel> FilteredArrList = new ArrayList<PatientMedNotifiListModel>();

                if (PatientMedNotifiDataOriginal == null) {
                    PatientMedNotifiDataOriginal = new ArrayList<PatientMedNotifiListModel>(PatientMedNotifiData); // saves

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
                    results.count = PatientMedNotifiDataOriginal.size();
                    results.values = PatientMedNotifiDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < PatientMedNotifiDataOriginal.size(); i++) {
                        PatientMedNotifiListModel model = PatientMedNotifiDataOriginal.get(i);

                        String data = model.getMedicineName();
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

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_patient_med_notifi_list, null);
            holder = new ViewHolder();
            holder.medicine_id = (TextView) convertView.findViewById(R.id.medicineIDTextView);
            holder.medicine_name = (TextView) convertView.findViewById(R.id.medicineNameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PatientMedNotifiListModel patientMedNotifiListModel = PatientMedNotifiData.get(position);
        holder.medicine_id.setText(patientMedNotifiListModel.getMedicineID());
        holder.medicine_name.setText(patientMedNotifiListModel.getMedicineName());

        return convertView;
    }

    private class ViewHolder {

        TextView medicine_id;
        TextView medicine_name;
    }

}

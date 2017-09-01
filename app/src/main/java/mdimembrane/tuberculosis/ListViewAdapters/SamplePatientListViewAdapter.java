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
 * Created by root on 17/8/17.
 */

public class SamplePatientListViewAdapter extends BaseAdapter implements Filterable {




    Activity context;
    ArrayList<SamlePatientListModel> samplePatientData;
    ArrayList<SamlePatientListModel> samplePatientDataOriginal;

    public SamplePatientListViewAdapter(Activity context, ArrayList<SamlePatientListModel> samplePatientData) {
        super();
        this.context = context;
        this.samplePatientData = samplePatientData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return samplePatientData.size();
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

                samplePatientData = (ArrayList<SamlePatientListModel>) results.values; // has

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
                List<SamlePatientListModel> FilteredArrList = new ArrayList<SamlePatientListModel>();

                if (samplePatientDataOriginal == null) {
                    samplePatientDataOriginal = new ArrayList<SamlePatientListModel>(samplePatientData); // saves

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
                    results.count = samplePatientDataOriginal.size();
                    results.values = samplePatientDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < samplePatientDataOriginal.size(); i++) {
                        SamlePatientListModel model = samplePatientDataOriginal.get(i);

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

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_medicine_notification_list, null);
            holder = new ViewHolder();
            holder.patient_id = (TextView) convertView.findViewById(R.id.patientIDTextView);
            holder.patient_name = (TextView) convertView.findViewById(R.id.patientNameTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SamlePatientListModel samlePatientListModel = samplePatientData.get(position);
        holder.patient_id.setText(samlePatientListModel.getPatientID());
        holder.patient_name.setText(samlePatientListModel.getPatientName());

        return convertView;
    }

    private class ViewHolder {

        TextView patient_id;
        TextView patient_name;
    }
}

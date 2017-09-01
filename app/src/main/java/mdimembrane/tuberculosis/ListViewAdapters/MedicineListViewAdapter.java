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
 * Created by root on 4/8/17.
 */

public class MedicineListViewAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<MedicineListModel> medicineData;
    ArrayList<MedicineListModel> medicineDataOriginal;

    public MedicineListViewAdapter(Activity context, ArrayList<MedicineListModel> medicineData) {
        super();
        this.context = context;
        this.medicineData = medicineData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return medicineData.size();
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

                medicineData = (ArrayList<MedicineListModel>) results.values; // has

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
                List<MedicineListModel> FilteredArrList = new ArrayList<MedicineListModel>();

                if (medicineDataOriginal == null) {
                    medicineDataOriginal = new ArrayList<MedicineListModel>(medicineData); // saves

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
                    results.count = medicineDataOriginal.size();
                    results.values = medicineDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < medicineDataOriginal.size(); i++) {
                        MedicineListModel model = medicineDataOriginal.get(i);

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


    private class ViewHolder {
        TextView medicine_id;
        TextView medicine_name;
        TextView medicine_start_date;
        TextView medicine_end_date;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_medicine_list, null);
            holder = new ViewHolder();
            holder.medicine_id = (TextView) convertView.findViewById(R.id.medicineIdTextView);
            holder.medicine_name = (TextView) convertView.findViewById(R.id.medicineNameTextView);
            holder.medicine_start_date = (TextView) convertView.findViewById(R.id.medicineStartDateTextView);
            holder.medicine_end_date = (TextView) convertView.findViewById(R.id.medicineEndDateTextView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        MedicineListModel medicineListModel=medicineData.get(position);
        holder.medicine_id.setText(medicineListModel.getMedicineID());
        holder.medicine_name.setText(medicineListModel.getMedicineName());
        holder.medicine_start_date.setText(medicineListModel.getMedicineStartDate());
        holder.medicine_end_date.setText(medicineListModel.getMedicineEndDate());

        return convertView;
    }
}

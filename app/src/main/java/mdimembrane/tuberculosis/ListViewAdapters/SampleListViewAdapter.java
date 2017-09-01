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
 * Created by root on 11/8/17.
 */

public class SampleListViewAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<SampleListModel> sampleData;
    ArrayList<SampleListModel> sampleDataOriginal;

    public SampleListViewAdapter(Activity context, ArrayList<SampleListModel> sampleData) {
        super();
        this.context = context;
        this.sampleData = sampleData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return sampleData.size();
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

                sampleData = (ArrayList<SampleListModel>) results.values; // has

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
                List<SampleListModel> FilteredArrList = new ArrayList<SampleListModel>();

                if (sampleDataOriginal == null) {
                    sampleDataOriginal = new ArrayList<SampleListModel>(sampleData); // saves

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
                    results.count = sampleDataOriginal.size();
                    results.values = sampleDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < sampleDataOriginal.size(); i++) {
                        SampleListModel model = sampleDataOriginal.get(i);

                        String data = model.getSampleName();
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


    static class ViewHolder {
        TextView sample_no;
        TextView sample_name;
        TextView sample_date;
        TextView result_status;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        SampleListViewAdapter.ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_sample_list, null);
            holder = new SampleListViewAdapter.ViewHolder();
            holder.sample_no = (TextView) convertView.findViewById(R.id.sampleNoTextView);
            holder.sample_name = (TextView) convertView.findViewById(R.id.sampleNameTextView);
            holder.sample_date = (TextView) convertView.findViewById(R.id.sampleDateTextView);
            holder.result_status = (TextView) convertView.findViewById(R.id.resultStatusTextView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (SampleListViewAdapter.ViewHolder) convertView.getTag();
        }
        SampleListModel sampleListModel=sampleData.get(position);
        holder.sample_no.setText(sampleListModel.getSampleNo());
        holder.sample_name.setText(sampleListModel.getSampleName());
        holder.sample_date.setText(sampleListModel.getSampleDate());
        holder.result_status.setText(sampleListModel.getResultStatus());
        return convertView;
    }
}

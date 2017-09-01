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

public class SampleNotifiListViewAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<SampleNotifiListModel> sampleNotifiData;
    ArrayList<SampleNotifiListModel> sampleNotifiDataOriginal;

    public SampleNotifiListViewAdapter(Activity context, ArrayList<SampleNotifiListModel> sampleNotifiData) {
        super();
        this.context = context;
        this.sampleNotifiData = sampleNotifiData;

    }


    public int getCount() {
        // TODO Auto-generated method stub
        return sampleNotifiData.size();
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

                sampleNotifiData = (ArrayList<SampleNotifiListModel>) results.values; // has

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
                List<SampleNotifiListModel> FilteredArrList = new ArrayList<SampleNotifiListModel>();

                if (sampleNotifiDataOriginal == null) {
                    sampleNotifiDataOriginal = new ArrayList<SampleNotifiListModel>(sampleNotifiData); // saves

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
                    results.count = sampleNotifiDataOriginal.size();
                    results.values = sampleNotifiDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < sampleNotifiDataOriginal.size(); i++) {
                        SampleNotifiListModel model = sampleNotifiDataOriginal.get(i);

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


    private class ViewHolder {
        TextView sample_no;
        TextView sample_name;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        SampleNotifiListViewAdapter.ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_sample_notifi_list, null);
            holder = new SampleNotifiListViewAdapter.ViewHolder();
            holder.sample_no = (TextView) convertView.findViewById(R.id.sampleNoTextView);
            holder.sample_name = (TextView) convertView.findViewById(R.id.sampleNameTextView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (SampleNotifiListViewAdapter.ViewHolder) convertView.getTag();
        }
        SampleNotifiListModel sampleNotifiListModel=sampleNotifiData.get(position);
        holder.sample_no.setText(sampleNotifiListModel.getSampleNo());
        holder.sample_name.setText(sampleNotifiListModel.getSampleName());
        return convertView;
    }
}

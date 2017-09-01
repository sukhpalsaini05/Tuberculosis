package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 17/8/17.
 */

public class SampleNotifiListModel {


    public String Sample_no;
    public String Sample_name;


    public SampleNotifiListModel(String Sample_no, String Sample_name) {


        this.Sample_no = Sample_no;
        this.Sample_name = Sample_name;

    }

    public String getSampleNo() {
        return Sample_no;
    }

    public String getSampleName() {
        return Sample_name;
    }

}

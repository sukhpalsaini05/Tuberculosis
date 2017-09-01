package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 11/8/17.
 */

public class SampleListModel {

    public String Sample_no;
    public String Sample_name;
    public String Sample_date;
    public String result_status;


    public SampleListModel(String Sample_no, String Sample_name,String Sample_date, String result_status) {


        this.Sample_no = Sample_no;
        this.Sample_name = Sample_name;
        this.Sample_date = Sample_date;
        this.result_status = result_status;

    }

    public String getSampleNo() {
        return Sample_no;
    }

    public String getSampleName() {
        return Sample_name;
    }

    public String getSampleDate() {
        return Sample_date;
    }

    public String getResultStatus() {
        return result_status;
    }
}

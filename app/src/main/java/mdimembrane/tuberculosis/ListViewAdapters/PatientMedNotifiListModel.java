package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 16/8/17.
 */

public class PatientMedNotifiListModel {

    public String medicine_id;
    public String medicine_name;

    public PatientMedNotifiListModel(String medicine_id,String medicine_name) {


        this.medicine_id=medicine_id;
        this.medicine_name=medicine_name;
    }

    public String getMedicineID(){
        return medicine_id;
    }

    public String getMedicineName(){
        return medicine_name;
    }


}

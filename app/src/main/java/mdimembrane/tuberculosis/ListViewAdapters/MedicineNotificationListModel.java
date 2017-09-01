package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 14/8/17.
 */

public class MedicineNotificationListModel {




    public String patient_id;
    public String patient_name;

    public MedicineNotificationListModel(String patient_id,String patient_name) {


        this.patient_id=patient_id;
        this.patient_name=patient_name;
    }

    public String getPatientID(){
        return patient_id;
    }

    public String getPatientName(){
        return patient_name;
    }



}

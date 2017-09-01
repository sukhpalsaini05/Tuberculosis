package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 26/7/17.
 */

public class PatientListModel {



    public String patient_id;
    public String patient_name;
    public String patient_phone;
    public String patient_date;

    public PatientListModel(String patient_id, String patient_name,String patient_phone,String patient_date) {

        this.patient_id=patient_id;
        this.patient_name=patient_name;
        this.patient_phone=patient_phone;
        this.patient_date=patient_date;
    }

    public String getPatientID(){
        return patient_id;
    }

    public String getPatientName(){
        return patient_name;
    }

    public String getPatientPhone(){
        return patient_phone;
    }

    public String getPatientDate(){
        return patient_date;
    }

}

package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 4/8/17.
 */

public class MedicineListModel {


    public String medicine_id;
    public String medicine_name;
    public String medicine_start_date;
    public String medicine_end_date;

    public MedicineListModel(String medicine_id, String medicine_name,String medicine_start_date,String medicine_end_date) {


        this.medicine_id=medicine_id;
        this.medicine_name=medicine_name;
        this.medicine_start_date=medicine_start_date;
        this.medicine_end_date=medicine_end_date;
    }

    public String getMedicineID(){
        return medicine_id;
    }

    public String getMedicineName(){
        return medicine_name;
    }

    public String getMedicineStartDate(){
        return medicine_start_date;
    }

    public String getMedicineEndDate(){
        return medicine_end_date;
    }

}

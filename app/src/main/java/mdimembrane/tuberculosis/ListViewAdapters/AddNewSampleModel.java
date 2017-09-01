package mdimembrane.tuberculosis.ListViewAdapters;

/**
 * Created by root on 10/8/17.
 */

public class AddNewSampleModel {



    public String test_id;
    public String test_name;
    public String test_description;
    public String test_units;
    public String test_min_range;
    public String test_max_range;
    public String test_instructions;




    public AddNewSampleModel (String test_id,String test_name, String test_description,String test_units,
                              String test_max_range,String test_instructions,String test_min_range) {

        this.test_id=test_id;
        this.test_name=test_name;
        this.test_description=test_description;
        this.test_units=test_units;
        this.test_min_range=test_min_range;
        this.test_max_range=test_max_range;
        this.test_instructions=test_instructions;

    }

    public String getTestName(){
        return test_name;
    }
    public String getTestId(){
        return test_id;
    }
    public String getTestDescription(){
        return test_description;
    }

    public String getTestUnits(){
        return test_units;
    }

    public String getTestMinRange(){
        return test_min_range;
    }

    public String getTestMaxRange(){
        return test_max_range;
    }

    public String getTestInstructions(){
        return test_instructions;
    }


}

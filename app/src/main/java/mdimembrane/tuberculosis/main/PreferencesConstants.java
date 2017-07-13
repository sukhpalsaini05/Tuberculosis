package mdimembrane.tuberculosis.main;

import android.provider.BaseColumns;


//constants used for database
public interface PreferencesConstants extends BaseColumns {
	public static final String APP_MAIN_PREF = "TuberCulosis";


	public static class AddNewAccount
	{
		public static final String ACCOUNT_TYPE = "account_type";
		public static final String USER_NAME = "user_name";
		public static final String EMPLOYEE_CODE = "employee_code";
		public static final String GENDER = "gender";
		public static final String USER_STATE = "user_state";
		public static final String USER_DISTT = "user_distt";
		public static final String USER_TEHSIL = "user_tehsil";
		public static final String USER_VILLAGE = "user_village";
		public static final String USER_ADDRESS = "user_address";
		public static final String USER_PINCODE = "user_pincode";
		public static final String HOSPITAL_TYPE = "hospital_type";
		public static final String HOSPITAL_NAME = "hospital_name";
		public static final String USER_PHONE = "user_phone";
		public static final String USER_AADHAR_NO = "user_aadhar_no";

	}
	
}
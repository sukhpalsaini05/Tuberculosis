package mdimembrane.tuberculosis.ServerConfiguration;

import android.provider.BaseColumns;


//constants used for database
public interface ServerConstants extends BaseColumns {
	public static final String SERVER_ADDRESS = "http://192.51.11.177/TB_API/";
	public static final String GET_LOCATION= SERVER_ADDRESS + "get_location.php";
	public static final String NEW_ACCOUNT= SERVER_ADDRESS + "account_request.php";
	public static final String PATIENT_DATA= SERVER_ADDRESS + "patient_data.php";
	public static final String USER_LOGIN= SERVER_ADDRESS + "user_login.php";
	public static final String SERVER_ADDRESS_MONITORING_MASTER = SERVER_ADDRESS + "monitoring_master.php";
	
	
}
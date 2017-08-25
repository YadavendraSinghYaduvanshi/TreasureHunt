package com.cpm.Constants;

public class CommonString {

	// preferenec keys
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";

	public static final String KEY_PATH = "path";
	public static final String KEY_VERSION = "version";
	public static final String KEY_STORE_IN_TIME = "Store_in_time";

	public static final String KEY_DATE = "date";
	public static final String MID = "MID";
	public static final String KEY_P = "P";
	public static final String KEY_C = "C";
	public static final String KEY_D = "D";
	public static final String KEY_U = "U";

	public static final String KEY_ID = "_id";

	// webservice constants
	public static final String KEY_SUCCESS = "Success";
	public static final String KEY_FAILURE = "Failure";
	public static final String KEY_FALSE = "False";
	public static final String KEY_CHANGED = "Changed";
	
	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String METHOD_STORE_VISIT = "MessagedetailPOST";
	public static final String SOAP_ACTION_STORE_VISIT = "http://tempuri.org/"
			+ METHOD_STORE_VISIT;
	public static final String KEY_NO_DATA = "NoData";
	//public static final String URL = "http://th.eemaindia.in/AppDemoRest.svc/";
	public static final String URL = "http://th.parinaam.in/AppDemoRest.svc/";
	//public static final String URL = "http://10.200.20.133/RestAppDemo/AppDemoRest.svc/";

	public static final String METHOD_UPLOAD_IMAGE = "AddGeoTagImage";

	public static final String METHOD_LOGIN = "Logindetail";

	public static final String TABLE_INSERT_GEO_TAG = "INSERT_GEO_TAG_DATA";

	public static final String METHOD_UPLOAD_GEOTAG = "UploadTreasure_Hunt_DataPOST";

	public static final String METHOD_UPLOAD_service = "UploadTreasure_Hunt_DataPOST";

	public static final String ERROR = " PROBLEM OCCURED IN ";

	public static final String KEY_STORE_ID = "STORE_ID";
	public static final String KEY_STORE_NAME = "STORE_NAME";

	public static final String KEY_ADDRESS = "ADDRESS";
	public static final String KEY_USER_ID = "USER_ID";
	public static final String KEY_IN_TIME = "IN_TIME";
	public static final String KEY_OUT_TIME = "OUT_TIME";
	public static final String KEY_VISIT_DATE = "VISIT_DATE";
	public static final String KEY_LATITUDE = "LATITUDE";
	public static final String KEY_LONGITUDE = "LONGITUDE";

	public static final String KEY_STATUS = "STATUS";
	public static final String TABLE_GEOTAG_DETAIL = "GEOTAG_MASTER";

	public static final String KEY_CITY = "CITY";

	public static final String KEY_IMAGE_PATH1 = "IMAGE_PATH1";

	public static final String KEY_TYPE = "TYPE";



	// Geo tag

	public static final String CREATE_TABLE_INSERT_GEOTAG = "CREATE TABLE "
			+ TABLE_INSERT_GEO_TAG + " (" + KEY_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT ," + KEY_STORE_NAME
			+ " VARCHAR," + KEY_LATITUDE + " VARCHAR," + KEY_LONGITUDE
			+ " VARCHAR," + KEY_STATUS + " VARCHAR," + KEY_DATE + " VARCHAR,"
			+ KEY_TYPE + " VARCHAR," + KEY_IMAGE_PATH1 + " VARCHAR)";

}

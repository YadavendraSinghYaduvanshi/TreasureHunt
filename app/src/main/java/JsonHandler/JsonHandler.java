package JsonHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cpm.xmlGetterSetter.FailureGetterSetter;
import com.cpm.xmlGetterSetter.LoginGetterSetter;
import com.cpm.xmlGetterSetter.MessageSetterGetter;

public class JsonHandler {

	private static final String TAG_DATE = "CURRENTDATE";
	private static final String TAG_RESULT = "RESULT";
	private static final String TAG_APP_VERSION = "APP_VERSION";
	private static final String TAG_APP_PATH = "APP_PATH";

	static JSONArray contacts = null;

	public static LoginGetterSetter loginXMLHandler(String jsonString) {
		LoginGetterSetter lgs = new LoginGetterSetter();

		jsonString = jsonString.replace("\\", "");
		jsonString = jsonString.replace("\"[", "[");
		jsonString = jsonString.replace("]\"", "]");
		try {
			if (jsonString != null) {

				contacts = new JSONArray(jsonString);

				// Getting JSON Array node
				// contacts = jsonObj.getJSONArray(TAG_DATA);

				// looping through All Contacts
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					lgs.setVERSION(c.getString(TAG_APP_VERSION));
					lgs.setPATH(c.getString(TAG_APP_PATH));
					lgs.setDATE(c.getString(TAG_DATE));

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lgs;

	}
	
	
	public static MessageSetterGetter MessageXMLHandler(String jsonString) {
		MessageSetterGetter lgs = new MessageSetterGetter();

		jsonString = jsonString.replace("\\", "");
		jsonString = jsonString.replace("\"[", "[");
		jsonString = jsonString.replace("]\"", "]");
		try {
			if (jsonString != null) {

				contacts = new JSONArray(jsonString);

				// Getting JSON Array node
				// contacts = jsonObj.getJSONArray(TAG_DATA);

				// looping through All Contacts
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					lgs.setMessage(c.getString("MESSAGE"));
					lgs.setSentDate(c.getString("SENTDATE"));
			

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lgs;

	}

	public static FailureGetterSetter FailureXMLHandler(String jsonString) {
		FailureGetterSetter lgs = new FailureGetterSetter();

		try {
			if (jsonString != null) {

				JSONObject jsonObj = new JSONObject(jsonString);

				// Getting JSON Array node
				contacts = jsonObj.getJSONArray(jsonString);

				// looping through All Contacts
				for (int i = 0; i < contacts.length(); i++) {
					JSONObject c = contacts.getJSONObject(i);

					if (c.getString("STATUS").equals("STATUS")) {
						lgs.setStatus(c.getString("STATUS"));
					}
					if (c.getString("ERRORMSG").equals("ERRORMSG")) {
						lgs.setErrorMsg(c.getString("ERRORMSG"));
					}

				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lgs;

	}

}

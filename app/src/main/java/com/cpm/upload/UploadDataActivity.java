package com.cpm.upload;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cpm.Constants.CommonString;
import com.cpm.database.TreasureHunt_Database;
import com.cpm.delegates.GeotaggingBeans;
import com.cpm.geotagging.LocationActivity;
import com.cpm.geotagging.SharingActivity;
import com.cpm.message.AlertMessage;
import com.cpm.treasurehunt.LoginActivity;
import com.cpm.treasurehunt.MainMenuActivity;
import com.cpm.treasurehunt.R;
import com.cpm.xmlGetterSetter.FailureGetterSetter;

public class UploadDataActivity extends Activity {

	private Dialog dialog;
	private ProgressBar pb;
	private TextView percentage, message;
	private String visit_date, username;
	JSONObject jo1 = new JSONObject();
	private SharedPreferences preferences;
	private TreasureHunt_Database database;
	String jo = "";
	private String reasonid, faceup, stock, length;
	private int factor, k;
	String datacheck = "";
	String[] words;
	String validity, storename;
	int mid;
	String sod = "";
	String total_sku = "";
	String sku = "";
	String sos_data = "";
	String category_data = "";
	String result;
	String errormsg = "";
	int count;
	int eventType;

	ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();

	private FailureGetterSetter failureGetterSetter = null;
	static int counter = 1;
	String version;

	// boolean upload_status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		visit_date = preferences.getString(CommonString.KEY_DATE, null);
		username = preferences.getString(CommonString.KEY_USERNAME, null);

		try {
			version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		database = new TreasureHunt_Database(this);
		database.open();

		new UploadTask(this).execute();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// database.close();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent i = new Intent(this, MainMenuActivity.class);
		startActivity(i);
		UploadDataActivity.this.finish();
	}

	private class UploadTask extends AsyncTask<Void, Void, String> {
		private Context context;

		UploadTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dialog = new Dialog(context, R.style.CustomDialog);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Uploading");
			dialog.setCancelable(false);
			dialog.show();
			pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
			percentage = (TextView) dialog.findViewById(R.id.percentage);
			message = (TextView) dialog.findViewById(R.id.message);
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {

				HttpParams myParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(myParams, 10000);
				HttpConnectionParams.setSoTimeout(myParams, 10000);
				HttpClient httpclient = new DefaultHttpClient();
				InputStream inputStream = null;

				geotaglist = database.getGeotaggingData("Y");

				for (int i = 0; i < geotaglist.size(); i++) {

					runOnUiThread(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							k = k + factor;
							pb.setProgress(20);

							message.setText("Uploading Geotag Data...");
						}
					});

					myParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(myParams, 10000);
					HttpConnectionParams.setSoTimeout(myParams, 10000);
					httpclient = new DefaultHttpClient();
					inputStream = null;

					JSONObject geoTag = new JSONObject();

					geoTag.put("LOCATION", geotaglist.get(i).getStorename());
					geoTag.put("PHOTO_URL", geotaglist.get(i).getUrl1());
					geoTag.put("LATITUDE", geotaglist.get(i).getLatitude());
					geoTag.put("LOGITUDE", geotaglist.get(i).getLongitude());
					geoTag.put("GPS_MODE", geotaglist.get(i).getType());
					geoTag.put("CREATED_BY", username);

					runOnUiThread(new Runnable() {

						public void run() {
							// TODO Auto-generated method stub
							k = k + factor;
							pb.setProgress(50);

						}
					});

					String final_geotag = geoTag.toString();
					final_geotag = makeJson(final_geotag);

					HttpPost httppost = new HttpPost(CommonString.URL
							+ CommonString.METHOD_UPLOAD_GEOTAG);
					httppost.setHeader("Accept", "application/json");
					httppost.setHeader("Content-type", "application/json");

					StringEntity se = new StringEntity(final_geotag);

					httppost.setEntity(se);

					HttpResponse response = httpclient.execute(httppost);

					inputStream = response.getEntity().getContent();

					if (inputStream != null) {
						result = convertInputStreamToString(inputStream);
						result = result.replace("\"", "");

					}

					if (result.toString().equalsIgnoreCase(
							CommonString.KEY_NO_DATA)) {
						return CommonString.METHOD_UPLOAD_GEOTAG;
					}
					if (result.toString().equalsIgnoreCase(
							CommonString.KEY_FALSE)) {
						return CommonString.METHOD_UPLOAD_GEOTAG;
					}

					if (result.toString().equalsIgnoreCase(
							CommonString.KEY_FAILURE)) {
						return CommonString.METHOD_UPLOAD_GEOTAG;
					}

					if (result.toString().equalsIgnoreCase(
							CommonString.KEY_SUCCESS)) {

						runOnUiThread(new Runnable() {

							public void run() {
								// TODO Auto-generated method stub
								k = k + factor;
								pb.setProgress(80);

							}
						});

						if (geotaglist.get(i).getUrl1() != null
								&& !geotaglist.get(i).getUrl1()
										.equalsIgnoreCase("")) {

							if (new File("/mnt/sdcard/Treasure_hunt/"
									+ geotaglist.get(i).getUrl1()).exists()) {
								result = UploadSODImage(geotaglist.get(i)
										.getUrl1());

								if (result.toString().equalsIgnoreCase(
										CommonString.KEY_FALSE)) {

									return CommonString.METHOD_UPLOAD_IMAGE;

								} else if (result
										.equalsIgnoreCase(CommonString.KEY_FAILURE)) {

									return CommonString.METHOD_UPLOAD_IMAGE;
								}

								runOnUiThread(new Runnable() {

									public void run() {
										// TODO Auto-generated method stub

										message.setText("Image Upload");
										pb.setProgress(100);

									}
								});
							}
						}

						database.open();
						database.updateGeoTagData(geotaglist.get(i).getId(),
								CommonString.KEY_U);
						// database.deleteGeoTagData(geotaglist.get(i).getId());
						// database.open();

						// database.updateGeoTagData(geotaglist.get(i).getId(),
						// CommonString.KEY_D);

					}

				}

			}

			catch (MalformedURLException e) {

				final AlertMessage message = new AlertMessage(
						UploadDataActivity.this,
						AlertMessage.MESSAGE_EXCEPTION, "download", e);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						message.showMessage();
					}
				});

			} catch (IOException e) {
				final AlertMessage message = new AlertMessage(
						UploadDataActivity.this,
						AlertMessage.MESSAGE_SOCKETEXCEPTION, "socket", e);

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						message.showMessage();

					}
				});
			}

			catch (Exception e) {
				final AlertMessage message = new AlertMessage(
						UploadDataActivity.this,
						AlertMessage.MESSAGE_EXCEPTION, "download", e);

				e.getMessage();
				e.printStackTrace();
				e.getCause();
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						message.showMessage();
					}
				});
			}
			return CommonString.KEY_SUCCESS;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			dialog.dismiss();

			if (result.equals(CommonString.KEY_SUCCESS)) {

				Intent intent = new Intent(UploadDataActivity.this,
						SharingActivity.class);
				startActivity(intent);
				UploadDataActivity.this.finish();

			} else if (!result.equals("")) {

				final Dialog dialog = new Dialog(UploadDataActivity.this,
						R.style.DialogNoTitle);
				dialog.setContentView(R.layout.loginmsg);
				dialog.setCancelable(false);
				dialog.show();
				TextView msg = (TextView) dialog.findViewById(R.id.msg);
				Button ok = (Button) dialog.findViewById(R.id.cancel);
				msg.setText(CommonString.ERROR + result);
				ok.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						Intent i = new Intent(UploadDataActivity.this,
								MainMenuActivity.class);
						startActivity(i);

					}
				});

			}

		}
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public String UploadSODImage(String path) throws Exception {

		errormsg = "";
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile("/mnt/sdcard/Treasure_hunt/" + path, o);

		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;

		while (true) {
			if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
				break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}

		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/Treasure_hunt/"
				+ path, o2);

		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		// below we have string value of img

		String ba1 = Base64.encodeBytes(ba);
		JSONObject jsonObject = new JSONObject();

		try {

			jsonObject.accumulate("img", ba1);

			jsonObject.accumulate("name", path);

			HttpParams myParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(myParams, 10000);
			HttpConnectionParams.setSoTimeout(myParams, 10000);
			HttpClient httpclient = new DefaultHttpClient();
			String json = jsonObject.toString();

			InputStream inputStream = null;
			HttpPost httppost = new HttpPost(CommonString.URL
					+ CommonString.METHOD_UPLOAD_IMAGE);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");

			// posting data to server with parameter

			// ****************************************************

			StringEntity se = new StringEntity(json);

			httppost.setEntity(se);

			HttpResponse response = httpclient.execute(httppost);

			inputStream = response.getEntity().getContent();

			if (inputStream != null) {
				result = convertInputStreamToString(inputStream);
				result = result.replace("\"", "");
			}
			if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {

				return CommonString.METHOD_UPLOAD_IMAGE;
			}
			if (result.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

				// new File("/mnt/sdcard/Treasure_hunt/" + path).delete();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	String makeJson(String json) {
		json = json.replace("\\", "");
		json = json.replace("\"[", "[");
		json = json.replace("]\"", "]");

		return json;
	}

	public JSONArray makeJsonArray(JSONArray json) {
		JSONArray jason = new JSONArray();

		for (int i = 0; i < json.length(); i++) {

		}

		return json;
	}

}

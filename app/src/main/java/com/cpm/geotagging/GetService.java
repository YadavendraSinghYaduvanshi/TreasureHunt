package com.cpm.geotagging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.cpm.Constants.CommonString;

public class GetService extends Service implements LocationListener {
	private ProgressDialog dialog1;
	private LocationManager m_gpsManager = null;
	private String Text;
	private Double currLatitude = 0.0;
	private Double currLongitude = 0.0;
	private String provider;
	private String username, password, visit_date;
	private SharedPreferences preferences;
	private int versionCode;
	private Timer timer;
	Handler handler;
	private boolean status = false;
	String app_ver;
	String result;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new Binder();
	}

	public void onCreate() {
		super.onCreate();

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		username = preferences.getString(CommonString.KEY_USERNAME, null);
		password = preferences.getString(CommonString.KEY_PASSWORD, null);
		visit_date = preferences.getString(CommonString.KEY_DATE, null);

		handler = new Handler();

		m_gpsManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		m_gpsManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
				this);
		provider = m_gpsManager.GPS_PROVIDER;

		if (!m_gpsManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
			intent.putExtra("enabled", true);
			sendBroadcast(intent);

		}

		startService();
	}

	private void startService() {

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Service", "YES");
		editor.commit();

		final Handler handler = new Handler();
		timer = new Timer();

		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {

				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if (status == true) {
							timer.cancel();
						} else {
							if (!currLatitude.equals(0.0)
									&& !currLongitude.equals(0.0)) {

								new MyTask().execute();
							} else {
								openNetwork();
								new MyTask().execute();
							}
						}
					}
				}, 20000);
			}
		};

		timer.schedule(doAsynchronousTask, 0, 1200000);

	}

	public void openNetwork() {

		if (currLatitude.equals(0.0) && currLongitude.equals(0.0)) {

			m_gpsManager = (LocationManager) getSystemService(LOCATION_SERVICE);
			m_gpsManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			provider = m_gpsManager.NETWORK_PROVIDER;
			Location location = m_gpsManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			if (location != null) {
				currLatitude = location.getLatitude();
				currLongitude = location.getLongitude();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("Service", "NO");
		editor.commit();
		stopSelf();
		status = true;

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		currLatitude = location.getLatitude();
		currLongitude = location.getLongitude();

	}

	public String getCurrentTime() {

		Calendar m_cal = Calendar.getInstance();
		int hour = m_cal.get(Calendar.HOUR_OF_DAY);
		int min = m_cal.get(Calendar.MINUTE);

		String intime = "";

		if (hour == 0) {
			intime = "" + 12 + ":" + min + " AM";
		} else if (hour == 12) {
			intime = "" + 12 + ":" + min + " PM";
		} else {

			if (hour > 12) {
				hour = hour - 12;
				intime = "" + hour + ":" + min + " PM";
			} else {
				intime = "" + hour + ":" + min + " AM";
			}
		}
		return intime;

	}

	private class MyTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

		}

		@Override
		protected void onPostExecute(String result) {

			Text = "My current location is: " + "Latitude = " + currLatitude
					+ "Longitude = " + currLongitude + " provider is "
					+ provider + "Time is" + getCurrentTime();

			// Toast.makeText(GetService.this, "" + Text, Toast.LENGTH_SHORT)
			// .show();

			currLatitude = 0.0;
			currLongitude = 0.0;

		}

		@Override
		protected String doInBackground(Void... params) {

			try {

				HttpParams myParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(myParams, 10000);
				HttpConnectionParams.setSoTimeout(myParams, 10000);
				HttpClient httpclient = new DefaultHttpClient();
				InputStream inputStream = null;

				JSONObject geoTag = new JSONObject();

				geoTag.put("LOCATION", "");
				geoTag.put("PHOTO_URL", "");
				geoTag.put("LATITUDE", currLatitude);
				geoTag.put("LOGITUDE", currLongitude);
				geoTag.put("GPS_MODE", provider);
				geoTag.put("CREATED_BY", username);

				String final_geotag = geoTag.toString();
				final_geotag = makeJson(final_geotag);

				HttpPost httppost = new HttpPost(CommonString.URL
						+ CommonString.METHOD_UPLOAD_service);
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

				if (result.toString()
						.equalsIgnoreCase(CommonString.KEY_NO_DATA)) {
					// return "";
				}
				if (result.toString().equalsIgnoreCase(CommonString.KEY_FALSE)) {
					// return "";
				}

				if (result.toString()
						.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
					// return "";
				}

				if (result.toString()
						.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

				}
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return CommonString.KEY_SUCCESS;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	String makeJson(String json) {
		json = json.replace("\\", "");
		json = json.replace("\"[", "[");
		json = json.replace("]\"", "]");

		return json;
	}

	private String convertInputStreamToString(InputStream inputStream)
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

}

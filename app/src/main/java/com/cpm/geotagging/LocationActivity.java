package com.cpm.geotagging;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.TreasureHunt_Database;
import com.cpm.delegates.GeotaggingBeans;
import com.cpm.treasurehunt.MainMenuActivity;

import com.cpm.upload.UploadDataActivity;


public class LocationActivity //extends MapActivity implements OnMapReadyCallback,ConnectionCallbacks, OnConnectionFailedListener, LocationListener
 {

/*	private static final String TAG = "LocationActivity";
	protected static final String PHOTO_TAKEN = "photo_taken";
	LocationManager locationManager;
	Geocoder geocoder;
	TextView locationText;
	MapView map;
	protected Button _button;
	protected Button _buttonsave;
	public Camera camera;
	protected ImageView _image;
	protected TextView _field;
	Button imagebtn;
	MapController mapController;
	GeoPoint point;
	protected boolean _taken;
	ImageView capture_1;
	EditText storename, adres, city;
	public String text;
	public View view;
	Location location;
	GeotaggingBeans data = new GeotaggingBeans();

	protected String diskpath = "";
	protected String _path;
	String provider;
	String date = "", username = "";

	*//**//*
	 * String storeid; String storeaddress = "";
	 *//**//*
	String storelatitude = "";
	String storelongitude = "";
	protected int resultCode;
	public BitmapDrawable bitmapDrawable;
	int abc;
	int abd;
	int abe;
	// TextView Stroename;
	static Editor e1, e2, e3;
	double lat;
	double longitude;
	private Bitmap mBubbleIcon, mShadowIcon;
	ProgressBar progress;
	private SharedPreferences preferences;

	class MapOverlay extends Overlay {
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			super.draw(canvas, mapView, shadow);

			// ---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			mapView.getProjection().toPixels(point, screenPts);

			// ---add the marker---
			progress.setVisibility(View.INVISIBLE);
			Bitmap mBubbleIcon = BitmapFactory.decodeResource(getResources(),
					R.drawable.pointer);
			canvas.drawBitmap(mBubbleIcon, screenPts.x - mBubbleIcon.getWidth()
					/ 2, screenPts.y - mBubbleIcon.getHeight(), null);
			return true;
		}
	}

	*//**//** Called when the activity is first created. *//**//*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_location);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		_buttonsave = (Button) findViewById(R.id.geotag_sumbitbtn);
		capture_1 = (ImageView) findViewById(R.id.camera);
		adres = (EditText) findViewById(R.id.address);
		storename = (EditText) findViewById(R.id.storename);
		city = (EditText) findViewById(R.id.city);

		progress = (ProgressBar) findViewById(R.id.progressBar1);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		date = preferences.getString(CommonString.KEY_DATE, null);
		username = preferences.getString(CommonString.KEY_USERNAME, null);

		map = (MapView) this.findViewById(R.id.mapview);
		map.setBuiltInZoomControls(true);
		map.setSatellite(true);

		mapController = map.getController();
		mapController.setZoom(15);

		locationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);
		geocoder = new Geocoder(this);

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, this);

		provider = locationManager.GPS_PROVIDER;

		getLocation();

		_buttonsave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (!(data.getLatitude() == 0) && !(data.getLongitude() == 0)) {

					if (Validate()) {

						if (new File("/mnt/sdcard/Treasure_hunt/" + _path)
								.exists())

						{
							lat = data.getLatitude();
							longitude = data.getLongitude();
							String status = "Y";
							TreasureHunt_Database data = new TreasureHunt_Database(
									getApplicationContext());
							data.open();

							data.InsertStoregeotagging(storename.getText()
									.toString().replaceAll("[&^<>{}'$]", " "),
									lat, longitude, _path,

									provider, status, date);
							data.close();

							Intent intent = new Intent(LocationActivity.this,
									UploadDataActivity.class);

							startActivity(intent);

							// LocationActivity.this.finish();

						} else {
							showToast("Please Capture Images");

						}
					} else {

						showToast("Please Enter Location");

					}
				} else {
					showToast("Wait For Geo Location");

				}

			}
		});

		capture_1.setOnClickListener(new ButtonClickHandler());

	}

	private void showToast(String message) {

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toastlayout,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}

	public void getLocation() {
		if (!(data.getLatitude() == 0) && !(data.getLongitude() == 0)) {

		} else {

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {

					if (!(data.getLatitude() == 0)
							&& !(data.getLongitude() == 0)) {

					} else {
						provider = locationManager.NETWORK_PROVIDER;
						openNetwork();
					}

				}
			}, 35000);

		}
	}

	public void showSettingsAlert(String provider) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				LocationActivity.this);

		alertDialog.setTitle(provider + " SETTINGS");

		alertDialog.setMessage(provider
				+ " is not enabled! Want to go to settings menu?");

		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						LocationActivity.this.startActivity(intent);
					}
				});

		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		alertDialog.show();
	}

	public String getCurrentTime() {

		Calendar m_cal = Calendar.getInstance();

		String intime = m_cal.get(Calendar.HOUR_OF_DAY) + "_"
				+ m_cal.get(Calendar.MINUTE) + "_" + m_cal.get(Calendar.SECOND);

		return intime;

	}

	public boolean Validate() {

		boolean result = true;

		if (storename.getText().toString().equals("")) {
			result = false;
		}
		return result;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		try {

			// Convert latitude and longitude into int that the GeoPoint
			// constructor can understand
			int latiti;
			int longi;

			data.setLatitude((location.getLatitude()));
			data.setLongitude((location.getLongitude()));

			latiti = (int) (location.getLatitude() * 1000000);
			longi = (int) (location.getLongitude() * 1000000);

			point = new GeoPoint(latiti, longi);
			mapController.animateTo(point);
			MapOverlay mapOverlay = new MapOverlay();
			List<Overlay> listOfOverlays = map.getOverlays();
			listOfOverlays.clear();
			listOfOverlays.add(mapOverlay);

		} catch (Exception e) {
			Log.e("LocateMe", "Could not get Geocoder data", e);
		}
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void gobacktogeoselection() {
		Intent intent = new Intent(LocationActivity.this,
				MainMenuActivity.class);
		startActivity(intent);
		this.finish();

	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	protected void startCameraActivity() {
		Log.i("MakeMachine", "startCameraActivity()");
		File file = new File(diskpath);
		Uri outputFileUri = Uri.fromFile(file);

		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

		startActivityForResult(intent, 0);

	}

	public boolean isNetworkOnline() {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(0);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = false;
			} else {
				netInfo = cm.getNetworkInfo(1);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED)
					status = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("MakeMachine", "resultCode: " + resultCode);
		switch (resultCode) {
		case 0:
			Log.i("MakeMachine", "User cancelled");
			break;

		case -1:

			if (new File("/mnt/sdcard/Treasure_hunt/" + _path).exists()) {
				capture_1.setImageResource(R.drawable.camera_tick);

			}

			break;
		}
	}

	public class ButtonClickHandler implements View.OnClickListener {
		LocationActivity loc = new LocationActivity();

		public void onClick(View view) {

			if (view.getId() == R.id.camera) {

				diskpath = Environment.getExternalStorageDirectory()
						+ "/Treasure_hunt/" + username + getCurrentTime()
						+ "front.jpg";
				_path = username + getCurrentTime() + "front.jpg";

				startCameraActivity();

			}

		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Handle the back button
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit

			Intent intent = new Intent(LocationActivity.this,
					MainMenuActivity.class);
			startActivity(intent);
			// LocationActivity.this.finish();

			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void ShowToast(String message) {

		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}

	public void openNetwork() {

		provider = locationManager.NETWORK_PROVIDER;
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);

		Location location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		if (location != null) {

			try {

				// Convert latitude and longitude into int that the GeoPoint
				// constructor can understand
				int latiti;
				int longi;

				data.setLatitude((location.getLatitude()));
				data.setLongitude((location.getLongitude()));

				latiti = (int) (location.getLatitude() * 1000000);
				longi = (int) (location.getLongitude() * 1000000);

				point = new GeoPoint(latiti, longi);
				mapController.animateTo(point);
				MapOverlay mapOverlay = new MapOverlay();
				List<Overlay> listOfOverlays = map.getOverlays();
				listOfOverlays.clear();
				listOfOverlays.add(mapOverlay);

			} catch (Exception e) {
				Log.e("LocateMe", "Could not get Geocoder data", e);
			}

		} else {
			showSettingsAlert("NETWORK");

			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
			getLocation();
		}

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}*/

}
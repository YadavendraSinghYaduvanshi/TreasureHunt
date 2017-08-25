package com.cpm.treasurehunt;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cpm.Constants.CommonString;
import com.cpm.database.TreasureHunt_Database;
import com.cpm.delegates.GeotaggingBeans;
import com.cpm.geotagging.LocationActivity;
import com.cpm.geotagging.SharingActivity;
import com.cpm.geotagging.TagLocationActivityActivity;
import com.cpm.message.AlertMessage;
import com.cpm.upload.UploadDataActivity;

public class MainMenuActivity extends Activity {

	private Intent intent;
	private Dialog dialog;
	private ProgressBar pb;
	private TreasureHunt_Database database;
	private int versionCode;
	private SharedPreferences preferences;
	private String date;
	private TextView percentage, message;
	ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();
	String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		date = preferences.getString(CommonString.KEY_DATE, null);

		database = new TreasureHunt_Database(this);
		database.open();

	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.cpm.geotagging.GetService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// database.close();

	}

	@Override
	protected void onResume() {
		
		super.onResume();
		geotaglist = database.getGeotaggingData("U");
		{
			for (int i = 0; i < geotaglist.size(); i++) {
				new File("/mnt/sdcard/Treasure_hunt/"
						+ geotaglist.get(i).getUrl1()).delete();
				database.deleteGeoTagData(geotaglist.get(i).getId());

			}
		}

		if (!isMyServiceRunning()) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("Service", "NO");
			editor.commit();

		}

		String service_status = preferences.getString("Service", "");
		if (!service_status.equalsIgnoreCase("yes")) {
			Intent serviceIntent = new Intent(MainMenuActivity.this,
					com.cpm.geotagging.GetService.class);
			this.startService(serviceIntent);
		}

	}

	public void onButtonClick(View v) {
		

		switch (v.getId()) {

		case R.id.upload:
			if (CheckNetAvailability()) {

				geotaglist = database.getGeotaggingData("Y");

				if (geotaglist.size() == 0) {

					showToast(AlertMessage.MESSAGE_NO_DATA);

				} else {
					intent = new Intent(getBaseContext(),
							UploadDataActivity.class);
					startActivity(intent);

					//MainMenuActivity.this.finish();
				}

			} else {
				showToast("No Network Available");
			}
			break;

		case R.id.exit:

			LayoutInflater factory = LayoutInflater.from(this);
			final View deleteDialogView = factory.inflate(R.layout.exit_layout,
					null);
			final AlertDialog deleteDialog = new AlertDialog.Builder(this)
					.create();
			deleteDialog.setView(deleteDialogView);
			deleteDialogView.findViewById(R.id.share).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// your business logic

							deleteDialog.dismiss();
							/*Intent intent = new Intent(
									"android.location.GPS_ENABLED_CHANGE");
							intent.putExtra("enabled", false);
							sendBroadcast(intent);

							Intent serviceIntent = new Intent(
									MainMenuActivity.this,
									com.cpm.geotagging.GetService.class);
							MainMenuActivity.this.stopService(serviceIntent);

							SharedPreferences.Editor editor = preferences
									.edit();
							editor.putString("Service", "NO");
							editor.commit();*/

							Intent i = new Intent(MainMenuActivity.this,
									LoginActivity.class);
							startActivity(i);
							MainMenuActivity.this.finish();

						}
					});
			deleteDialogView.findViewById(R.id.cancel).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							deleteDialog.dismiss();

						}
					});

			deleteDialog.show();

			break;
		case R.id.location:

			intent = new Intent(getBaseContext(), TagLocationActivityActivity.class);
			startActivity(intent);

			break;
			
		case R.id.messages_act:
			
			intent = new Intent(getBaseContext(), MessageActivity.class);
			startActivity(intent);

		}
	}

	private void showToast(String message) {

		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.upload_toast,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(message);

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}

	public boolean CheckNetAvailability() {

		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState() == NetworkInfo.State.CONNECTED
				|| connectivityManager.getNetworkInfo(
						ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
			// we are connected to a network
			connected = true;
		}
		return connected;
	}

}

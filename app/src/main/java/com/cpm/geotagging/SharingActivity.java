package com.cpm.geotagging;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.cpm.database.TreasureHunt_Database;
import com.cpm.delegates.GeotaggingBeans;
import com.cpm.treasurehunt.MainMenuActivity;
import com.cpm.treasurehunt.R;

public class SharingActivity extends Activity {

	TreasureHunt_Database database;
	String image = "";
	double lat = 0.0, lon = 0.0;
	int cid = 0;
	ArrayList<GeotaggingBeans> geotaglist = new ArrayList<GeotaggingBeans>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authentication);

		database = new TreasureHunt_Database(this);
		database.open();

		geotaglist = database.getGeotaggingData("U");
		int l = geotaglist.size();
		if (l > 0) {
			image = geotaglist.get(l - 1).getUrl1();
			lat = geotaglist.get(l - 1).getLatitude();
			lon = geotaglist.get(l - 1).getLongitude();
			cid = geotaglist.get(l - 1).getId();

		}

		LayoutInflater factory = LayoutInflater.from(this);
		final View deleteDialogView = factory.inflate(R.layout.dialoglayout,
				null);
		final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
		deleteDialog.setView(deleteDialogView);
		deleteDialogView.findViewById(R.id.share).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// your business logic
						deleteDialog.dismiss();
						initShareIntent("face", image);
					}
				});
		deleteDialogView.findViewById(R.id.cancel).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						deleteDialog.dismiss();
						//new File("/mnt/sdcard/Treasure_hunt/" + image).delete();
						database.deleteGeoTagData(cid);
						Intent i = new Intent(SharingActivity.this,
								MainMenuActivity.class);
						startActivity(i);
						SharingActivity.this.finish();

					}
				});

		deleteDialog.show();

	}

	private void initShareIntent(String type, String image) {

		boolean found = false;
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("image/jpeg");
		share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Treasure Hunt");
		share.putExtra(android.content.Intent.EXTRA_TEXT,
				"Treasure Hunt. My Location is " + lat + "," + lon);
		share.putExtra(Intent.EXTRA_TITLE, "My Location");
		share.putExtra(Intent.EXTRA_STREAM,
				Uri.parse("file:///sdcard/Treasure_hunt/" + image));
		startActivity(Intent.createChooser(share, "Select"));

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		database.deleteGeoTagData(cid);
		Intent i = new Intent(SharingActivity.this,
				MainMenuActivity.class);
		startActivity(i);
		SharingActivity.this.finish();

	}
}

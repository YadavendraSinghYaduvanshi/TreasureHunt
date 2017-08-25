package com.cpm.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.cpm.Constants.CommonString;
import com.cpm.delegates.GeotaggingBeans;

public class TreasureHunt_Database extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "TreasureHunt_Database";
	public static final int DATABASE_VERSION = 1;

	private SQLiteDatabase db;

	public TreasureHunt_Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	public void open() {
		try {
			db = this.getWritableDatabase();
		} catch (Exception e) {
		}
	}

	public void close() {
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		db.execSQL(CommonString.CREATE_TABLE_INSERT_GEOTAG);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + CommonString.TABLE_INSERT_GEO_TAG);

		onCreate(db);
	}

	public void deleteAllTables() {

		db.delete(CommonString.TABLE_INSERT_GEO_TAG, null, null);

	}

	public void InsertStoregeotagging(String STORENAME, double lat,
			double longitude, String path, 
			String type, String status, String date) {

		ContentValues values = new ContentValues();

		try {

			values.put(CommonString.KEY_STORE_NAME, STORENAME);
			values.put(CommonString.KEY_LATITUDE, Double.toString(lat));
			values.put(CommonString.KEY_LONGITUDE, Double.toString(longitude));
			values.put(CommonString.KEY_IMAGE_PATH1, path);
			values.put(CommonString.KEY_TYPE, type);
			values.put(CommonString.KEY_STATUS, status);
			values.put(CommonString.KEY_DATE, date);

			db.insert(CommonString.TABLE_INSERT_GEO_TAG, null, values);

		} catch (Exception ex) {
			Log.d("Database Exception while Insert Closes Data ",
					ex.getMessage());
		}

	}

	public void deleteGeoTagData(int id) {

		try {
			db.delete(CommonString.TABLE_INSERT_GEO_TAG, CommonString.KEY_ID
					+ "='" + id + "'", null);
		} catch (Exception e) {

		}
	}

	public void updateGeoTagData(int id, String status) {

		try {

			ContentValues values = new ContentValues();
			values.put(CommonString.KEY_STATUS, status);

			int l = db.update(CommonString.TABLE_INSERT_GEO_TAG, values,
					CommonString.KEY_ID + "=" + id, null);

			System.out.println("update : " + l);
		} catch (Exception e) {
			Log.d("Database Exception while Insert Posm Master Data ",
					e.getMessage());
		}
	}

	public ArrayList<GeotaggingBeans> getGeotaggingData(String status) {

		Log.d("FetchingStoredata--------------->Start<------------",
				"------------------");

		ArrayList<GeotaggingBeans> geodata = new ArrayList<GeotaggingBeans>();

		Cursor dbcursor = null;

		try {
			dbcursor = db.rawQuery("SELECT  * from "
					+ CommonString.TABLE_INSERT_GEO_TAG + "  WHERE STATUS = '"
					+ status + "'", null);

			if (dbcursor != null) {
				int numrows = dbcursor.getCount();

				dbcursor.moveToFirst();
				for (int i = 0; i < numrows; i++) {

					GeotaggingBeans data = new GeotaggingBeans();

					data.setId(dbcursor.getInt(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_ID)));
					data.setStorename(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_STORE_NAME)));
					data.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_LATITUDE))));
					data.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE))));
					data.setUrl1(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_IMAGE_PATH1)));
					data.setType(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_TYPE)));
					data.setDate(dbcursor.getString(dbcursor
							.getColumnIndexOrThrow(CommonString.KEY_DATE)));

					geodata.add(data);
					dbcursor.moveToNext();

				}

				dbcursor.close();

			}

		} catch (Exception e) {
			Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
					e.getMessage());
		}

		Log.d("FetchingStoredat---------------------->Stop<-----------",
				"-------------------");
		return geodata;

	}

}

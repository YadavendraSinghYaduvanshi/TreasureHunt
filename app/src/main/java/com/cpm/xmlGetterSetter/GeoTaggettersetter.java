package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class GeoTaggettersetter {

	ArrayList<String> store_cd = new ArrayList<String>();
	ArrayList<String> store_name = new ArrayList<String>();
	ArrayList<String> addres = new ArrayList<String>();
	ArrayList<String> storetype = new ArrayList<String>();
	ArrayList<String> city = new ArrayList<String>();
	ArrayList<String> latitude = new ArrayList<String>();
	ArrayList<String> longitude = new ArrayList<String>();
	ArrayList<String> storetype_status = new ArrayList<String>();

	public ArrayList<String> getStore_cd() {
		return store_cd;
	}

	public void setStore_cd(String store_cd) {
		this.store_cd.add(store_cd);
	}

	public ArrayList<String> getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name.add(store_name);
	}

	public ArrayList<String> getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres.add(addres);
	}

	public ArrayList<String> getStoretype() {
		return storetype;
	}

	public void setStoretype(String storetype) {
		this.storetype.add(storetype);
	}

	public ArrayList<String> getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city.add(city);
	}

	public ArrayList<String> getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude.add(latitude);
	}

	public ArrayList<String> getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude.add(longitude);
	}

	public ArrayList<String> getStoretype_status() {
		return storetype_status;
	}

	public void setStoretype_status(String storetype_status) {
		this.storetype_status.add(storetype_status);
	}

}

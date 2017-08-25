package com.cpm.delegates;

public class GeotaggingBeans {
	
	public String storeid,storename,adres,city,type,date;
	int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getStorename() {
		return storename;
	}
	public void setStorename(String storename) {
		this.storename = storename;
	}
	public String getAdres() {
		return adres;
	}
	public void setAdres(String adres) {
		this.adres = adres;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String url1;
	public String url2;
	public String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String url3;
	public double Latitude ;
	public double Longitude;
	
	public String getStoreid() {
		return storeid;
	}
	public void setStoreid(String storeid) {
		this.storeid = storeid;
	}
	public double getLatitude() {
		return Latitude;
	}
	public void setLatitude(double d) {
		Latitude = d;
	}
	public double getLongitude() {
		return Longitude;
	}
	public void setLongitude(double d) {
		Longitude = d;
	}
	
	
	public void setUrl1(String url1)
	{
		
		this.url1=url1;
	}
	
	public String getUrl1()
	{
		
		return url1;
	}
	
	public void setUrl2(String url2)
	{
		
		this.url2=url2;
	}
	
	public String getUrl2()
	{
		
		return url2;
	}
	
	public void setUrl3(String url3)
	{
		
		this.url3=url3;
	}
	
	public String getUrl3()
	{
		
		return url3;
	}

}

package com.jorgegarcia.airvisual.model;

import com.opencsv.bean.CsvBindByName;

public class WaqiStation {
	
	private String id;
	
	private String status;
	
	private String latitude;
	
	private String longitude;
	
	private String city;
	
	private String state;
	
	private String country;
	
	private float frecuency;
	
	private String Alias;
	
	private String url;
	
	public String getId() {
		return id;
	}
	public String getStatus() {
		return status;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getCountry() {
		return country;
	}
	public String getUrl() {
		return url;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public float getFrecuency() {
		return frecuency;
	}
	public String getAlias() {
		return Alias;
	}
	public void setFrecuency(float frecuency) {
		this.frecuency = frecuency;
	}
	public void setAlias(String alias) {
		Alias = alias;
	}
	
	

}

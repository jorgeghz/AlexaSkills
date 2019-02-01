package com.jorgegarcia.airvisual.model;

import com.opencsv.bean.CsvBindByName;

public class Station {
	@CsvBindByName
	private String id;
	@CsvBindByName
	private String status;
	@CsvBindByName
	private String latitude;
	@CsvBindByName
	private String longitude;
	@CsvBindByName
	private String city;
	@CsvBindByName
	private String state;
	@CsvBindByName
	private String country;
	@CsvBindByName
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
	
	

}

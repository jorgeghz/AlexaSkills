package com.jorgegarcia.airvisual.model;

public class StationResultJSON {
	
	private String id;
	private String json;
	private String status;
	private String timestamp;
	private String city;
	private String state;
	private String alias;
	private String latitude;
	private String longitude;
	
	
	
	public String getId() {
		return id;
	}
	public String getJson() {
		return json;
	}
	public String getStatus() {
		return status;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getAlias() {
		return alias;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}

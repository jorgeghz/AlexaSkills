package com.jorgegarcia.monitorambiental.model;

public class StationResult {
	private String id;
	private String country;
	private String state;
	private String city;
	private int aqiUS;
	private int aqiCN;
	private String lastPollutionUpdate;
	private String lastWeatherUpdate;
	private int temperature;
	
	public String getId() {
		return id;
	}
	public String getCountry() {
		return country;
	}
	public String getState() {
		return state;
	}
	public String getCity() {
		return city;
	}
	public int getAqiUS() {
		return aqiUS;
	}
	public int getAqiCN() {
		return aqiCN;
	}
	public String getLastPollutionUpdate() {
		return lastPollutionUpdate;
	}
	public String getLastWeatherUpdate() {
		return lastWeatherUpdate;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setAqiUS(int aqiUS) {
		this.aqiUS = aqiUS;
	}
	public void setAqiCN(int aqiCN) {
		this.aqiCN = aqiCN;
	}
	public void setLastPollutionUpdate(String lastPollutionUpdate) {
		this.lastPollutionUpdate = lastPollutionUpdate;
	}
	public void setLastWeatherUpdate(String lastWeatherUpdate) {
		this.lastWeatherUpdate = lastWeatherUpdate;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", country=" + country + ", state=" + state + ", city=" + city + ", aqiUS="+ aqiUS+" aqiCN="+aqiCN+", lastPollutionUpdate="+lastPollutionUpdate+", lastWeatherUpdate="+lastWeatherUpdate +", temperature="+temperature+"]";
	}
	

}

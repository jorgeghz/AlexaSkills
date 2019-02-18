package com.jorgegarcia.airvisual.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.Station;
import com.mysql.cj.jdbc.JdbcPreparedStatement;

import software.amazon.ion.Timestamp;

public class MonitorAmbientalDBClient {

	static String url = "jdbc:mysql://monitorambientaldb.c0zqxtqqc7nd.us-west-2.rds.amazonaws.com:3306/monitorambientaldb?useSSL=false";
	static String user = "jorge";
	static String password = "esytk3dw";
	static String resultstTable = "station_results";
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//MonitorAmbientalDBClient.writeResult("id0001", "{ \"status\": \"fail\"}");
		MonitorAmbientalDBClient.getResults("id0001");

	}

	public static void getResults(String id) {

		Connection connection = null;

		String sql = "SELECT * FROM " + resultstTable;
		try {
			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {

				System.out.print("ID: "+rs.getString(1));
				System.out.print(" - JSON: "+rs.getString(2));
				System.out.println();
			
			}
			
			connection.close();
		} catch (Exception e) {

		}

	}

	public static void getResults(String lat, String lon) {

	}

	public static void writeResult(String id, String json) {

		String timestamp = Timestamp.nowZ().toString();
		Connection connection = null;

		String sql = "INSERT INTO " + resultstTable + " (id,result,status,timestamp) VALUES(?,?,?,?)";
		try {

			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setString(1, id);
			preparedStatement.setString(2, json);
			preparedStatement.setString(3, "succes");
			preparedStatement.setString(4, timestamp);

			preparedStatement.execute();

			System.out.println("A new result has been inserted");
			connection.close();

		} catch (SQLException ex) {

			Logger lgr = Logger.getLogger(JdbcPreparedStatement.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);

		}

	}

	public static void insertListToStationResults(List<StationResultJSON> stationResults) {
		
		Connection connection = null;

	
		try {
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO waqi_station_results (id,result,status,timestamp) VALUES (?, ?, ?,?);");
			for(StationResultJSON station : stationResults){
				statement.setString(1, station.getId());
				statement.setString(2, station.getJson());
				statement.setString(3, "success");
				statement.setString(4, station.getTimestamp());
				statement.addBatch();
			}
			
			statement.executeBatch();
			connection.commit();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
public static void insertListToWaqiStationResults(List<StationResultJSON> stationResults) {
		
		Connection connection = null;

	
		try {
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(false);
			PreparedStatement statement = connection.prepareStatement("INSERT INTO waqi_station_results (id,result,status,timestamp,city, state, alias, latitude, longitude) VALUES (?, ?, ?,?,?,?,?,?,?);");
			for(StationResultJSON station : stationResults){
				statement.setString(1, station.getId());
				statement.setString(2, station.getJson());
				statement.setString(3, "success");
				statement.setString(4, station.getTimestamp());
				statement.setString(5, station.getCity());
				statement.setString(6, station.getState());
				statement.setString(7, station.getAlias());
				statement.setString(8, station.getLatitude());
				statement.setString(9, station.getLongitude());
				statement.addBatch();
			}
			
			statement.executeBatch();
			connection.commit();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<Station> readAllStations() {
		
		Connection connection = null;
		List<Station> stations=new ArrayList<Station>();
		String sql = "SELECT * FROM monitorambientaldb.stations_waqi";
		try {
			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Station station=new Station();
				station.setId(rs.getString(1));
				station.setLatitude(rs.getString(2));
				station.setLongitude(rs.getString(3));
				station.setCity(rs.getString(4));
				station.setState(rs.getString(5));
				station.setCountry(rs.getString(6));
				station.setUrl(rs.getString(7));
				station.setFrecuency(rs.getFloat(8));
				station.setAlias(rs.getString(9));
				stations.add(station);
				
			}
			connection.close();
		} catch (Exception e) {
		
		
		}
		return stations;
	}

	public static StationResultJSON getLatestStationResult(Station station) {
		
		Connection connection = null;
		StationResultJSON lateStationResult=null;
		
		String sql="SELECT * FROM waqi_station_results WHERE UPPER(alias)='"+station.getAlias().toUpperCase()+"' order  by timestamp desc limit 1";
		try {
			connection = DriverManager.getConnection(url, user, password);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()) {
			
				lateStationResult=new StationResultJSON();
				lateStationResult.setId(rs.getString(1));
				lateStationResult.setJson(rs.getString(2));
				lateStationResult.setTimestamp(rs.getString(3));
				lateStationResult.setStatus(rs.getString(4));
				lateStationResult.setCity(rs.getString(5));
				lateStationResult.setState(rs.getString(6));
				lateStationResult.setAlias(rs.getString(7));
				lateStationResult.setLatitude(rs.getString(8));
				lateStationResult.setLongitude(rs.getString(9));
			}
			
		}catch(Exception e) {
			
		}
		
	return lateStationResult;
	}
		
	
}




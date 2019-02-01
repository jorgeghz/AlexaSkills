package com.jorgegarcia.monitorambiental.client;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.code.geocoder.Geocoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.jorgegarcia.monitorambiental.model.Station;
import com.jorgegarcia.monitorambiental.model.StationResultJSON;
import com.jorgegarcia.monitorambiental.model.WaqiStation;

import software.amazon.ion.Timestamp;

public class MonitorAmbientalClient {
	private int id;
	private String apiKey = "a3EQHRApv3Eaa52iX";

	Client client = ClientBuilder.newClient();
	String airVisualURL = "http://api.airvisual.com/v2";
	WebTarget target = client.target(airVisualURL);

	public MonitorAmbientalClient() {

	}

	public WaqiStation getNearestStation(final double latitude, final double longitude) {

		List<WaqiStation> allStations = MonitorAmbientalDBClient.readWaqiStations();
		List<Point> points = new ArrayList<Point>();
		List<List<Integer>> selectedRestaurants = new ArrayList<List<Integer>>();

		for (WaqiStation station : allStations) {
			double lat = Float.valueOf(station.getLatitude());
			double lon = Float.valueOf(station.getLongitude());
			Point point = new Point();
			point.setLocation(lat, lon);
			points.add(point);
			System.out.println("Lat: " + station.getLatitude() + ", Lon: " + station.getLongitude());

		}

		Collections.sort(allStations, new Comparator<WaqiStation>() {
			public int compare(WaqiStation station1, WaqiStation station2) {
				double distPoint1 = Point2D.distance(latitude, longitude, Double.valueOf(station1.getLatitude()),
						Double.valueOf(station1.getLongitude()));
				System.out.println("Distancia entre " + station1.getAlias() + " = " + distPoint1);
				double distPoint2 = Point2D.distance(latitude, longitude, Double.valueOf(station2.getLatitude()),
						Double.valueOf(station2.getLongitude()));
				System.out.println("Distancia entre " + station2.getAlias() + " = " + distPoint2);
				return Double.compare(distPoint1, distPoint2);
			}
		});

		return allStations.get(0);

	}

	public void getAllStationsResults() throws IllegalStateException, FileNotFoundException, InterruptedException {

		List<Station> stations;
		List<StationResultJSON> stationResults = new ArrayList<StationResultJSON>();
		ClassLoader classLoader = new MonitorAmbientalClient().getClass().getClassLoader();
		String filePath = classLoader.getResource("estaciones.csv").getPath();
		stations = StationsFileManager.readAllStations(filePath);
		int i = 0;
		/*
		 * for(Station station: stations) { String url=station.getUrl();
		 * StationResultJSON result=getDataFromStationURL2(station.getId(), url);
		 * stationResults.add(result); i++; System.out.println(i); //if(i>5) break;
		 * Thread.sleep(13000); }
		 * 
		 * insertResultstoDB(stationResults);
		 */

	}

	private void insertResultstoDB(List<StationResultJSON> stationResults) {

		// MonitorAmbientalDBClient.insertListToStationResults(stationResults);
		MonitorAmbientalDBClient.insertListToWaqiStationResults(stationResults);

	}

	public StationResultJSON getLatestStationResult(WaqiStation station) {

		StationResultJSON stationResult = MonitorAmbientalDBClient.getLatestStationResult(station);
		return stationResult;

	}

	public void getAllStationsData() throws IllegalStateException, FileNotFoundException, InterruptedException {

		// List<Station> stations;
		List<StationResultJSON> stationResults = new ArrayList<StationResultJSON>();
		/*
		 * ClassLoader classLoader = new AirVisualClient().getClass().getClassLoader();
		 * String filePath=classLoader.getResource("estaciones.csv").getPath(); stations
		 * = StationsFileManager.readAllStations(filePath);
		 */
		int i = 0;
		List<WaqiStation> stations = MonitorAmbientalDBClient.readWaqiStations();
		System.out.println("# Estaciones = "+stations.size());
		for (WaqiStation station : stations) {
			String url = station.getUrl();
			StationResultJSON result = getDataFromStationURL3(station);
			stationResults.add(result);
			i++;
			System.out.println(i);
			// if(i>5) break;
			// Thread.sleep(1000);
		}

		insertResultstoDB(stationResults);

	}

	private HashMap<String, Object> getDataFromStationURL(String url) {
		WebTarget target = client.target(url);
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();

		HashMap<String, Object> map = response.readEntity(new GenericType<HashMap<String, Object>>() {
		});
		HashMap<String, Object> dataMap = null;
		if (response.getStatus() == 200) {

			dataMap = (HashMap<String, Object>) map.get("data");
			// HashMap<String, Object>
			// dataMap=airVisual.getNearestCity("32.60363889","-115.4859444");
			// HashMap<String, Object> current= (HashMap<String, Object>)
			// dataMap.get("current");
			// HashMap<String, Object> pollution= (HashMap<String, Object>)
			// current.get("pollution");

		}
		System.out.println(response.getStatus());

		return dataMap;

	}

	private StationResultJSON getDataFromStationURL3(WaqiStation station) {
		StationResultJSON stationResult = new StationResultJSON();
		WebTarget target = client.target(station.getUrl());
		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.get();
		HashMap<String, Object> map = response.readEntity(new GenericType<HashMap<String, Object>>() {
		});
		HashMap<String, Object> dataMap = null;
		Gson gson = new Gson();

		Type typeOfHashMap = new TypeToken<Map<String, Object>>() {
		}.getType();
		String json = gson.toJson(map, typeOfHashMap);
		String status = "fail";

		if (response.getStatus() == 200) {
			status = "success";
		}
		stationResult.setId(station.getId());
		stationResult.setCity(station.getCity());
		stationResult.setState(station.getState());
		stationResult.setAlias(station.getAlias());
		stationResult.setJson(json);
		stationResult.setLatitude(station.getLatitude());
		stationResult.setLongitude(station.getLongitude());
		stationResult.setTimestamp(Timestamp.nowZ().toString());
		return stationResult;
	}

	public HashMap<String, Object> getNearestCity(String lat, String lon) {

		String path = "/nearest_city";

		WebTarget target = client.target(airVisualURL).path(path).queryParam("lat", lat).queryParam("lon", lon)
				.queryParam("key", apiKey);

		Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);

		Response response = invocationBuilder.get();

		HashMap<String, Object> map = response.readEntity(new GenericType<HashMap<String, Object>>() {
		});
		HashMap<String, Object> dataMap = new HashMap<String, Object>();
		if (response.getStatus() == 200)
			dataMap = (HashMap<String, Object>) map.get("data");
		System.out.println(response.getStatus());

		return dataMap;
	}

	public static LatLng getGeoLocation(String address) throws ApiException, InterruptedException, IOException {

		String googleApiKey = "AIzaSyAun7Ez_dNJ6QjmMDr7UrCLlaz9s9m_Btw";
		GeoApiContext context = new GeoApiContext.Builder().apiKey(googleApiKey).build();
		GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(results[0].addressComponents));
		LatLng latlng = results[0].geometry.location;
		return latlng;
	}

	public static void main(String[] args) {

		MonitorAmbientalClient airVisual = new MonitorAmbientalClient();
		try {
			
			 airVisual.getAllStationsData(); 
			 
			 
			 /*
			LatLng latlng=MonitorAmbientalClient.getGeoLocation("Rosa Navarro 579, Guadalajara");
			WaqiStation station=airVisual.getNearestStation(latlng.lat,latlng.lng);
			System.out.println("Estacion mas cercana: "+
					 station.getAlias()+" Lat: "+station.getLatitude()+" Long: "+station.
					 getLongitude());
			StationResultJSON result=airVisual.getLatestStationResult(station);
			Type type = new TypeToken<Map<String, Object>>(){}.getType();
			Gson gson=new Gson();
			Map<String, Object> myMap = gson.fromJson(result.getJson(), type);
			Map<String, Object> data = (Map<String, Object>) myMap.get("data");
			
			System.out.println("El indice de calidad del aire es " +data.get("aqi"));
			*/
			// airVisual.getAllStationsResults();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	// standard getters and setters
}

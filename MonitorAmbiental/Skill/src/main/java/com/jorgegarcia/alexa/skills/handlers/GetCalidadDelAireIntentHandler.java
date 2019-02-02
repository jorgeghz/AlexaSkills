package com.jorgegarcia.alexa.skills.handlers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.WaqiStation;

public class GetCalidadDelAireIntentHandler implements RequestHandler {
	MonitorAmbientalClient monitor;

	public GetCalidadDelAireIntentHandler() {
		monitor = new MonitorAmbientalClient();
	}

	public boolean canHandle(HandlerInput input) {
		return input.matches(Predicates.intentName("GetCalidadDelAireIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		String speechText = "No la se";
		DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory()
				.getDeviceAddressService();
		String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
		Address address = deviceAddressServiceClient.getFullAddress(deviceId);
		String addressStr = null;
		
		if (address.getAddressLine1() != null) {
			addressStr = address.getAddressLine1()+", "+address.getCity();
		} else if (address.getAddressLine2() != null) {
			addressStr = address.getAddressLine2()+", "+address.getCity();
		} else if (address.getAddressLine3() != null) {
			addressStr = address.getAddressLine3()+", "+address.getCity();
		} else {
			speechText = "No tienes ninguna dirección registrada en tu dispositivo";
		}
		
		
		if (addressStr != null) {

			if (address.getAddressLine1() != null) {
				addressStr = address.getAddressLine1();

			}
			try {

				LatLng geolocation = monitor.getGeoLocation(addressStr);
				WaqiStation station = getNearestStation(addressStr);

				double meters = monitor.distance(geolocation.lat, geolocation.lng,
						Double.valueOf(station.getLatitude()), Double.valueOf(station.getLongitude()), 0, 0);
				boolean isStationNear = false;

				if (meters < 15000) {
					isStationNear = true;

				}

				if (isStationNear) {

					String alias = station.getAlias();
					String city = station.getCity();
					String mentionCity = "de " + city;

					if (alias.toUpperCase().equals(city.toUpperCase())) {
						mentionCity = "";
					}

					Map<String, Object> stationData = getCalidadDelAire(station);
					double aqiValue = Math.round((Double) stationData.get("aqi"));
					int aqi = (int) aqiValue;
					if (aqi < 50) {
						speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
								+ " es buena, solo " + aqi + " puntos AQI de contaminación";
					}
					if (aqi >= 50 && aqi < 100) {
						speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
								+ " es moderadamente buena, actualmente hay " + aqi + " puntos AQI de contaminación";
					}
					if (aqi > 100 && aqi < 150) {
						speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
								+ " es mala, actualmente hay " + aqi + " puntos AQI de contaminación";
					}
					if (aqi >= 150 && aqi < 200) {
						speechText = "hay mucha contaminación cerca de " + addressStr + ", actualmente hay " + aqi
								+ " puntos AQI de contaminación. Evita el ejercicio al aire libre";
					}
					if (aqi >= 200) {
						speechText = " La contaminación del aire en en " + addressStr + " es extredamente alta, con "
								+ aqi
								+ " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario";
					}
				} else {

					speechText = "No hay ninguna estación de monitoreo cerca de tu ubicación";
				}
				System.out.println(speechText);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("Calidad del Aire", speechText).build();
	}

	private WaqiStation getNearestStation(String address) throws ApiException, InterruptedException, IOException {
		LatLng geolocation = monitor.getGeoLocation(address);
		WaqiStation station = monitor.getNearestStation(geolocation.lat, geolocation.lng);

		return station;
	}

	private Map<String, Object> getCalidadDelAire(WaqiStation station)
			throws ApiException, InterruptedException, IOException {

		StationResultJSON latestResult = monitor.getLatestStationResult(station);
		Type type = new TypeToken<Map<String, Object>>() {
		}.getType();
		Gson gson = new Gson();
		Map<String, Object> stationMap = gson.fromJson(latestResult.getJson(), type);
		Map<String, Object> map = (Map<String, Object>) stationMap.get("data");
		Map<String, Object> stationData = map;
		return stationData;

	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		GetCalidadDelAireIntentHandler handler = new GetCalidadDelAireIntentHandler();
		String address = "Vidauri 205, santa catarina";
		String speechText = null;

		LatLng geolocation;
		try {
			geolocation = handler.monitor.getGeoLocation(address);

			WaqiStation station = handler.getNearestStation(address);

			double meters = handler.monitor.distance(geolocation.lat, geolocation.lng,
					Double.valueOf(station.getLatitude()), Double.valueOf(station.getLongitude()), 0, 0);
			boolean isStationNear = false;
			if (meters < 15000) {
				isStationNear = true;
			}
			if (isStationNear) {
				String alias = station.getAlias();
				String city = station.getCity();
				String mentionCity = "de " + city;
				if (alias.toUpperCase().equals(city.toUpperCase())) {
					mentionCity = "";
				}
				Map<String, Object> stationData = handler.getCalidadDelAire(station);
				double aqiValue = Math.round((Double) stationData.get("aqi"));
				int aqi = (int) aqiValue;
				if (aqi < 50) {
					speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
							+ " es buena, solo " + aqi + " puntos AQI de contaminación";
				}
				if (aqi >= 50 && aqi < 100) {
					speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
							+ " es moderadamente buena, actualmente hay " + aqi + " puntos AQI de contaminación";
				}
				if (aqi > 100 && aqi < 150) {
					speechText = "La calidad del aire en la estación de monitoreo " + alias + " " + mentionCity
							+ " es mala, actualmente hay " + aqi + " puntos AQI de contaminación";
				}
				if (aqi >= 150 && aqi < 200) {
					speechText = "hay mucha contaminación cerca de " + address + ", actualmente hay " + aqi
							+ " puntos AQI de contaminación. Evita el ejercicio al aire libre";
				}
				if (aqi >= 200) {
					speechText = " La contaminación del aire en en " + address + " es extredamente alta, con " + aqi
							+ " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario";
				}
			} else {

				speechText = "No hay ninguna estación de monitoreo cerca de tu ubicación";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(speechText);
		long endTime = System.nanoTime();
		System.out.println("Duración: " + (endTime - startTime) / 1e6 + " ms");

	}

}

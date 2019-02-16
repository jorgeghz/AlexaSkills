package com.jorgegarcia.alexa.skills.handlers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.StringsESMX;
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
		
		Request request = (Request) input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
		String speechText = StringsESMX.getRandonMessage(MessageType.NO_ADDRESS);
		DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory()
				.getDeviceAddressService();
		String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
		Address address = deviceAddressServiceClient.getFullAddress(deviceId);
		String addressStr = "";
		Slot addressSlot=slots.get("address");
		if (address.getAddressLine1() != null) {
			addressStr = address.getAddressLine1()+", "+address.getPostalCode()+", "+address.getCity()+", "+address.getStateOrRegion();
		} else if (address.getAddressLine2() != null) {
			addressStr = address.getAddressLine2()+", "+address.getPostalCode()+", "+address.getCity()+", "+address.getStateOrRegion();
		} else if (address.getAddressLine3() != null) {
			addressStr = address.getAddressLine3()+", "+address.getPostalCode()+", "+address.getCity()+", "+address.getStateOrRegion();
		} 
		speechText=addressStr;
		if(addressSlot.getValue()!=null) {
			addressStr=addressSlot.getValue();
		}
		
		
		if (!addressStr.equals("")) {
			try {
				LatLng geolocation = monitor.getGeoLocation(addressStr);
				WaqiStation station = getNearestStation(addressStr, geolocation);
				double distanceInMeters = monitor.distance(geolocation.lat, geolocation.lng,
						Double.valueOf(station.getLatitude()), Double.valueOf(station.getLongitude()), 0, 0);
				boolean isStationNear = false;
				//if station is near than 15Km
				if (distanceInMeters < 15000) {
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
					//save sation data in session
					Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
					Map<String, Object> timeMap = (Map<String, Object>) stationData.get("time");
					
					//localDate.get
					double aqiValue = Math.round((Double) stationData.get("aqi"));
					int aqi = (int) aqiValue;
					
					sessionAttributes.put("stationData", stationData);
					sessionAttributes.put("distance", distanceInMeters);
					sessionAttributes.put("alias", alias);
					sessionAttributes.put("address",addressStr);
					sessionAttributes.put("aqi",aqi);
					
					if (aqi < 50) {
						speechText=StringsESMX.getRandonMessage(MessageType.EXCELLENT_AIR_QUALITY)+" Tan solo hay " +aqi+" puntos AQI de contaminación en la estación "+alias+" "+mentionCity+".";
						
					}
					if (aqi >= 50 && aqi < 100) {
						
						speechText=StringsESMX.getRandonMessage(MessageType.GOOD_AIR_QUALITY)+". Ahorita hay " +aqi+ " puntos AQI de contaminación en la estación " + alias + " " + mentionCity+".";
					}
					if (aqi > 100 && aqi < 150) {
						speechText=StringsESMX.getRandonMessage(MessageType.BAD_AIR_QUALITY)+". La estación " +alias+ " "+mentionCity+" ha registrado "+aqi+" puntos AQI de contaminación.";
						
					}
					if (aqi >= 150 && aqi < 200) {
						speechText = "hay mucha contaminación cerca de " + addressStr + ", actualmente hay " + aqi
								+ " puntos AQI de contaminación. Evita el ejercicio al aire libre";
					}
					if (aqi >= 200) {
						speechText = "La contaminación del aire en " + addressStr + " es extredamente alta, con "
								+ aqi
								+ " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario";
					}
					
					
					
				} else {

					speechText = StringsESMX.getRandonMessage(MessageType.NO_NEAR_STATION)+ addressStr;
					return input.getResponseBuilder()
							.withSpeech(speechText)
							.withSimpleCard("Monitor Ambiental", speechText)
							.withShouldEndSession(false)
							.withReprompt(StringsESMX.getRandonMessage(MessageType.REPROMPT_SOMETHING_ELSE))
							.build();
				}
				System.out.println(speechText);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return input.getResponseBuilder()
					.withSpeech(speechText+" "+StringsESMX.getRandonMessage(MessageType.REPROMPT_GET_DETAILS))
					.withSimpleCard("Monitor Ambiental", speechText)
					.withShouldEndSession(false)
					.withReprompt(StringsESMX.getRandonMessage(MessageType.REPROMPT_GET_DETAILS))
					.build();
		}
		else {
			 speechText = "no se tu direccion, puedes preguntar ¿Cual es la calidad del aire en ? Y despues decir tu domicilio ";
	         String  repromptText =
	                    "dame la direccion";
	        return input.getResponseBuilder()
	                .withSimpleCard("Monitor Ambiental", speechText)
	                .withSpeech(speechText)
	                .withReprompt(repromptText)
	                .withShouldEndSession(false)
	                .build();
	    }

			
		
	}

	private WaqiStation getNearestStation(String addressStr, LatLng geolocation) {
		WaqiStation station = monitor.getNearestStation(geolocation.lat, geolocation.lng);

		return station;
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
		String addressStr = "Paseo Vilacova 195, 45645, Guadalajara, Jalisco";
		String speechText = null;

		LatLng geolocation;
		try {
			geolocation = handler.monitor.getGeoLocation(addressStr);
			WaqiStation station = handler.getNearestStation(addressStr);

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
				Map<String, Object> timeMap = (Map<String, Object>) stationData.get("time");
				
				String dateStr=(String) timeMap.get("s");
				System.out.println(dateStr);
				
				Date lastUpdate=new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").parse(dateStr);  
				Calendar cal=Calendar.getInstance();
				cal.setTime(lastUpdate);
				LocalDate localDate=LocalDate.now();
				int hour=cal.get(Calendar.HOUR);
				//localDate.get
				speechText=speechText+ " a la hora "+hour+" ";
				System.out.println(speechText);
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
					speechText = "La contaminación del aire en " + addressStr + " es extredamente alta, con "
							+ aqi
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

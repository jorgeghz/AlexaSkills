package com.jorgegarcia.alexa.skills.handlers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Context;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.interfaces.geolocation.Coordinate;
import com.amazon.ask.model.interfaces.geolocation.GeolocationState;
import com.amazon.ask.model.services.ServiceException;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.response.ResponseBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.Station;
import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class GetCalidadDelAireIntentHandler implements RequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(GetCalidadDelAireIntentHandler.class);
	MonitorAmbientalClient monitor;
	String addressStr = "";
	String cardText = "";
	String alias = "";
	private static final String ADDRESS_PERM = "read::alexa:device:all:address";
	private static final String GEO_PERM = "alexa::devices:all:geolocation:read";
	Station nearStation = null;
	LatLng deviceLocation = null;
	List<String> permissions = new ArrayList<String>();
	String username;

	public GetCalidadDelAireIntentHandler() {
		monitor = new MonitorAmbientalClient();
	}

	public boolean canHandle(HandlerInput input) {
		return input.matches(Predicates.intentName("GetCalidadDelAireIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		LOGGER.info("Address obtained from device successfully.");
		RequestEnvelope envelope = input.getRequestEnvelope();
		Request request = (Request) input.getRequestEnvelope().getRequest();
		IntentRequest intentRequest = (IntentRequest) request;
		Intent intent = intentRequest.getIntent();
		Device device=envelope.getContext().getSystem().getDevice();
		//alexa::profile:given_name:read
		//LOGGER.info("Received Intent=" + intent.getName() + ", session=" + envelope.getSession());
		String speechText = StringsESMX.getRandonMessage(MessageType.NO_ADDRESS);
		

		// If address was in the utterance;
		Map<String, Slot> slots = intent.getSlots();
		Slot addressSlot = slots.get("address");
		String address;
		
		if (addressSlot.getValue() != null) {
			address = addressSlot.getValue().trim();
			try {
				deviceLocation = monitor.getGeoLocation(address);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// else get device's address
		else {
			// try to get geolocation service
			if(this.isGeolocationCompatible(device)){
				deviceLocation = getDeviceGeoLocation(input, envelope.getContext());
			}
			else {
				
				deviceLocation = getLocationFromAddress(input);
			}
			

		}

		if (deviceLocation != null) {
			try {
				nearStation = getNearestStation(deviceLocation);
				double distanceInMeters = monitor.distance(deviceLocation.lat, deviceLocation.lng,
						Double.valueOf(nearStation.getLatitude()), Double.valueOf(nearStation.getLongitude()), 0, 0);
				
				// if station is near than 15Km
				if (distanceInMeters < 15000) {
					
					alias = nearStation.getAlias();
					String city = nearStation.getCity();
					String mentionCity = "de " + city;
					if (alias.toUpperCase().equals(city.toUpperCase())) {
						mentionCity = "";
					}
					Map<String, Object> stationData = getCalidadDelAire(nearStation);
					// save sation data in session
					Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
					Map<String, Object> timeMap = (Map<String, Object>) stationData.get("time");
					// localDate.get
					double aqiValue = Math.round((Double) stationData.get("aqi"));
					int aqi = (int) aqiValue;
					sessionAttributes.put("stationData", stationData);
					sessionAttributes.put("distance", distanceInMeters);
					sessionAttributes.put("alias", alias);
					sessionAttributes.put("address", addressStr);
					sessionAttributes.put("aqi", aqi);

					if (aqi < 50) {
						speechText = StringsESMX.getRandonMessage(MessageType.EXCELLENT_AIR_QUALITY) + ". Tan solo hay "
								+ aqi + " puntos AQI de contaminación en la estación " + alias + " " + mentionCity
								+ ".";
						cardText = "Muy Buena: ";
					}
					if (aqi >= 50 && aqi < 100) {

						speechText = StringsESMX.getRandonMessage(MessageType.GOOD_AIR_QUALITY) + ". Ahorita hay " + aqi
								+ " puntos AQI de contaminación en la estación " + alias + " " + mentionCity + ".";
						cardText = "Buena: ";
					}
					if (aqi > 100 && aqi < 150) {
						speechText = StringsESMX.getRandonMessage(MessageType.BAD_AIR_QUALITY) + ". La estación "
								+ alias + " " + mentionCity + " ha registrado " + aqi + " puntos AQI de contaminación.";
						cardText = "Mala: ";
					}
					if (aqi >= 150 && aqi < 200) {
						speechText = "hay mucha contaminación cerca de " + addressStr + ", actualmente hay " + aqi
								+ " puntos AQI de contaminación. Evita el ejercicio al aire libre";
						cardText = "Muy Mala: ";
					}
					if (aqi >= 200) {
						speechText = "La contaminación del aire en " + addressStr + " es extredamente alta, con " + aqi
								+ " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario";
						cardText = "Dañina :";
					}

					cardText = cardText + " " + aqi + " AQI";

				} else {

					speechText = StringsESMX.getRandonMessage(MessageType.NO_NEAR_STATION) + addressStr;
					cardText = "No hay estación cercana";
					return input.getResponseBuilder().withSpeech(speechText)
							.withSimpleCard("Monitor Ambiental", cardText).withShouldEndSession(false)
							.withReprompt(StringsESMX.getRandonMessage(MessageType.REPROMPT_SOMETHING_ELSE)).build();
				}
				System.out.println(speechText);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return input.getResponseBuilder()
					.withSpeech(speechText + " " + StringsESMX.getRandonMessage(MessageType.REPROMPT_GET_DETAILS))
					.withSimpleCard("Estación de Monitoreo " + alias, cardText).withShouldEndSession(false)
					.withReprompt(StringsESMX.getRandonMessage(MessageType.REPROMPT_GET_DETAILS)).build();
		} else {
			System.out.println("No obtuve la direccion");
			speechText = StringsESMX.getRandonMessage(MessageType.NO_ADDRESS);
			String repromptText = "dame la direccion";
			return input.getResponseBuilder()
					.withSimpleCard("Ubicacion Desconocida", cardText)
					.withSpeech(speechText)
					.withReprompt(repromptText)
					.withShouldEndSession(false).build();
		}

	}

	private LatLng getLocationFromAddress(HandlerInput input) {
		System.out.println("Trying to get Address");
		DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory()
				.getDeviceAddressService();
		String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();

		try {
			Address address = deviceAddressServiceClient.getFullAddress(deviceId);
			if(address==null) {
				System.out.println("Address is null");
				this.handleMissingPermissions(input.getResponseBuilder(), ADDRESS_PERM,
						"Dame permisos para saber tu dirección");
				
			}
			else {
				if (address.getAddressLine1() != null) {
					addressStr = address.getAddressLine1() + ", " + address.getPostalCode() + ", " + address.getCity()
							+ ", " + address.getStateOrRegion();
				} else if (address.getAddressLine2() != null) {
					addressStr = address.getAddressLine2() + ", " + address.getPostalCode() + ", " + address.getCity()
							+ ", " + address.getStateOrRegion();
				} else if (address.getAddressLine3() != null) {
					addressStr = address.getAddressLine3() + ", " + address.getPostalCode() + ", " + address.getCity()
							+ ", " + address.getStateOrRegion();
				}
	
				if (!addressStr.equals("")) {
					try {
						deviceLocation = monitor.getGeoLocation(addressStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ServiceException e) {

			this.handleMissingPermissions(input.getResponseBuilder(), ADDRESS_PERM,
					"Dame permisos para saber tu direccion");
		}
		return null;
	}

	private LatLng getDeviceGeoLocation(HandlerInput input, Context context) {
		System.out.println("Trying to get location");
		
		GeolocationState geolocation = context.getGeolocation();
		if (geolocation != null) {
			Coordinate coordinate = geolocation.getCoordinate();
			return new LatLng(coordinate.getLatitudeInDegrees(), coordinate.getLongitudeInDegrees());

		} else {
			System.out.println("Trying to get location permission");
			
			this.handleMissingPermissions(input.getResponseBuilder(), GEO_PERM,
					"Dame permisos para saber tu ubicacion");
		}
		return null;

	}

	private Station getNearestStation(LatLng geolocation) {
		Station station = monitor.getNearestStation(geolocation.lat, geolocation.lng);
		return station;
	}

	private Station getNearestStation(String address) throws ApiException, InterruptedException, IOException {
		LatLng geolocation = monitor.getGeoLocation(address);
		Station station = monitor.getNearestStation(geolocation.lat, geolocation.lng);

		return station;
	}

	private Map<String, Object> getCalidadDelAire(Station station)
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
			Station station = handler.getNearestStation(addressStr);

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

				String dateStr = (String) timeMap.get("s");
				System.out.println(dateStr);

				Date lastUpdate = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").parse(dateStr);
				Calendar cal = Calendar.getInstance();
				cal.setTime(lastUpdate);
				LocalDate localDate = LocalDate.now();
				int hour = cal.get(Calendar.HOUR);
				// localDate.get
				speechText = speechText + " a la hora " + hour + " ";
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
					speechText = "La contaminación del aire en " + addressStr + " es extredamente alta, con " + aqi
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

	private Optional<Response> handleMissingPermissions(ResponseBuilder respBuilder, String permission, String speech) {
		permissions.add(permission);

		//LOGGER.info("Missing permissions " + permission);
		return respBuilder.withAskForPermissionsConsentCard(permissions).withSpeech(speech).withShouldEndSession(true)
				.build();
	}
	
	private boolean isGeolocationCompatible(Device device) {
		return device.getSupportedInterfaces().getGeolocation() != null;
	}

}

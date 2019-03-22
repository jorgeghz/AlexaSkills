package com.jorgegarcia.alexa.skills.handlers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;

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
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.response.ResponseBuilder;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.Messages;
import com.jorgegarcia.airvisual.model.Station;
import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.StringsESES;
import com.jorgegarcia.airvisual.model.StringsESMX;
import com.jorgegarcia.alexa.skills.exceptions.NoAddressRegistededInDeviceException;
import com.jorgegarcia.alexa.skills.exceptions.NoLocationFromAddressException;

public class GetCalidadDelAireIntentHandler implements RequestHandler {
	// private static final Logger LOGGER =
	// LogManager.getLogger(GetCalidadDelAireIntentHandler.class);
	
	static {
	
	   System.setProperty("log4j.configurationFile",  "log4j2.xml");
		   }

	MonitorAmbientalClient monitor;
	String addressStr = null;
	String cardText = "";
	String alias = "";
	String speechText = null;
	private static final String ADDRESS_PERM = "read::alexa:device:all:address";
	private static final String GEO_PERM = "alexa::devices:all:geolocation:read";
	LatLng location = null;
	List<String> permissions = new ArrayList<String>();
	String username;
	final int maxDistanceFromStation=15000;
	final int resultsMarginInHours=3;
	Map<String, Object> sessionAttributes;
	Messages messages;
	final Logger logger = LogManager.getLogger(GetCalidadDelAireIntentHandler.class);
	
	
	public GetCalidadDelAireIntentHandler() {
		monitor = new MonitorAmbientalClient();
	}

	public boolean canHandle(HandlerInput input) {
		return input.matches(Predicates.intentName("GetCalidadDelAireIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		
		
        // Write log to CloudWatch using LambdaLogger.
       
		
		// LOGGER.info("Address obtained from device successfully.");
		sessionAttributes=input.getAttributesManager().getSessionAttributes();
		RequestEnvelope envelope = input.getRequestEnvelope();
		Request request = (Request) input.getRequestEnvelope().getRequest();
		// DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd
		// HH:mm:ss");

		// System.out.println(request.getTimestamp().to.format(formatter));
		String requestDate = request.getTimestamp().toString();
		String locale=request.getLocale().toLowerCase();
		// System.out.println(requestDate.getHourOfDay());

		IntentRequest intentRequest = (IntentRequest) request;
		Intent intent = intentRequest.getIntent();
		Device device = envelope.getContext().getSystem().getDevice();
		
		if(locale.equals("es-mx")) {
			messages=new StringsESMX();
		}
		else if(locale.equals("es-es")) {
			messages=new StringsESES();
			
		}
		System.out.println("GetCalidadDelAireIntent invoked from locale "+locale +" at "+request.getTimestamp());
		 logger.info("GetCalidadDelAireIntent invoked from locale "+locale +" at "+request.getTimestamp());
		
	

		try {
			location = getLocation(input, device, intent);
		} catch (NoLocationFromAddressException e1) {
			System.out.println("NO LOCATION FROM ADDRESS");
			logger.warn("NO LOCATION FROM ADDRESS");
			speechText = messages.getRandomMessage(MessageType.NO_LOCATION_FROM_ADDRESS);
			return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(false)
					.withReprompt(messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE)).build();
		} catch (NoAddressRegistededInDeviceException e) {
			System.out.println("NO ADDRESS SET IN DEVICES");
			logger.warn("NO ADDRESS SET IN DEVICES");
			speechText = messages.getRandomMessage(MessageType.NO_ADDRESS_IN_DEVICE);
			return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(false)
					.withReprompt(messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE)).build();
		}

		if (location != null) {
			
				Station nearStation = getNearestStation(location);
				double distanceInMeters = monitor.distance(location.lat, location.lng,
						Double.valueOf(nearStation.getLatitude()), Double.valueOf(nearStation.getLongitude()), 0, 0);
				
				saveSessionAttribute("distance",distanceInMeters);
				saveSessionAttribute("nearStation", nearStation);
				// if station is near than 15Km
				if (distanceInMeters <= maxDistanceFromStation) {
					alias = nearStation.getAlias();
					Map<String, Object> stationData;
					try {
						stationData = getCalidadDelAire(nearStation);
					} catch (Exception e) {
						speechText = messages.getRandomMessage(MessageType.ERROR);
						e.printStackTrace();
						return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true).build();
					} 
					// save sation data in session
					// if the station data was update up to two hours ago
					if (wasRecentlyUpdated(requestDate, stationData, resultsMarginInHours)) {
						int aqi = 0;
						System.out.println(stationData.get("aqi").toString());
						if (!NumberUtils.isParsable(stationData.get("aqi").toString())) {
							
							speechText = messages.getRandomMessage(MessageType.NO_RECENT_STATION_DATA) + " " + alias
									+ ". Por favor intenta mas tarde";
							return input.getResponseBuilder()
									.withSpeech(speechText)
									.withShouldEndSession(true)
									.withReprompt(messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE))
									.build();
						}
						else {
							double aqiValue = Math.round((Double) stationData.get("aqi"));
							aqi = (int) aqiValue;
							System.out.println(aqiValue + " " + aqi);
							saveSessionAttribute("aqi", aqi);
							saveSessionAttribute("stationData",stationData);
							saveSessionAttribute("alias", alias);
							ArrayList<String> speechAndCardText=getSpeechAndCardText();
							speechText=speechAndCardText.get(0);
							cardText=speechAndCardText.get(1);
						}
						
					} else {

						speechText = messages.getRandomMessage(MessageType.NO_RECENT_STATION_DATA) + " " + alias
								+ ". Por favor intenta mas tarde";
						return input.getResponseBuilder().withSpeech(speechText).withShouldEndSession(true)
								.withReprompt(messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE))
								.build();
					}

				} 
				//NO NEAR STATION
				else {
					
					String address=(String) getSessionAttribute("address");
					String speech = messages.getRandomMessage(MessageType.NO_NEAR_STATION);
					System.out.println("NO NEAR STATION");
					logger.warn("NO NEAR STATION");
					if(address!=null)
						speech=speech+" "+address;
					String reprompt= messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE);
					
					speech=speech+". "+reprompt;
					clearSessionAttributes();
					
					return input.getResponseBuilder()
							.withSpeech(speech)
							.withShouldEndSession(false)
							.withReprompt(reprompt).build();
				}
				System.out.println("RESULT:: "+speechText);
				logger.info("RESULT:: "+speechText);
				
			logger.info(speechText);
			
			//System.out.println(speechText);
			return input.getResponseBuilder()
					.withSpeech(speechText + " " + messages.getRandomMessage(MessageType.REPROMPT_GET_DETAILS))
					.withSimpleCard("Estación de Monitoreo " + alias, cardText).withShouldEndSession(true)
					.withReprompt(messages.getRandomMessage(MessageType.REPROMPT_GET_DETAILS)).build();
		}
		
		System.out.println("NO LOCATION GATHERED");
		logger.warn("NO LOCATION GATHERED");
		
		System.out.println(speechText);
		return input.getResponseBuilder()
				.withSpeech(messages.getRandomMessage(MessageType.NO_GEOLOCATION))
				.withShouldEndSession(true)
				.build();
	
	}

	

	private ArrayList<String> getSpeechAndCardText() {
		
		
		ArrayList<String> speechAndCardText=new ArrayList<String>();
		int aqi=(Integer) getSessionAttribute("aqi");
		Station nearStation=(Station) getSessionAttribute("nearStation");
		String city = nearStation.getCity();
		String mentionCity = "de " + city;
		String speech = null;
		String card = null;
		
		if (alias.toUpperCase().equals(city.toUpperCase())) {
			mentionCity = "";
		}
		
		if (aqi < 50) {
			speech = messages.getRandomMessage(MessageType.EXCELLENT_AIR_QUALITY)
					+ ". Tan solo hay " + aqi + " puntos AQI de contaminación en la estación " + alias
					+ " " + mentionCity + ".";
			card = "Muy Buena: ";
		}
		if (aqi >= 50 && aqi < 100) {

			speech = messages.getRandomMessage(MessageType.GOOD_AIR_QUALITY) + ". Ahorita hay "
					+ aqi + " puntos AQI de contaminación en la estación " + alias + " " + mentionCity
					+ ".";
			card = "Buena: ";
		}
		if (aqi > 100 && aqi < 150) {
			speech = messages.getRandomMessage(MessageType.BAD_AIR_QUALITY) + ". La estación "
					+ alias + " " + mentionCity + " ha registrado " + aqi
					+ " puntos AQI de contaminación.";
			card = "Mala: ";
		}
		if (aqi >= 150 && aqi < 200) {
			speech = "hay mucha contaminación cerca. La estacion de monitoreo " + alias + " detecto  " + aqi
					+ " puntos AQI de contaminación. Evita el ejercicio al aire libre";
			card = "Muy Mala: ";
		}
		if (aqi >= 200) {
			speech = "La contaminación del aire es extredamente alta, con "
					+ aqi
					+ " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario";
			card = "Dañina :";
		}

		card = card + " " + aqi + " AQI";
		speechAndCardText.add(speech);
		speechAndCardText.add(card);
		
		return speechAndCardText;
		
	}

	

	private LatLng getLocation(HandlerInput input, Device device, Intent intent) throws NoLocationFromAddressException, NoAddressRegistededInDeviceException {
		LatLng location = null;
		Map<String, Slot> slots = intent.getSlots();
		Slot addressSlot = slots.get("address");
		String addressString;
		if (addressSlot.getValue() != null) {
			System.out.println("Detected address slot: " + addressSlot.getValue());

			addressString = addressSlot.getValue().trim();
			System.out.println("Address Slot Read " + addressString);
			try {
				location = monitor.getGeoLocation(addressString);
				saveSessionAttribute("address",addressString);
				

			} catch (Exception e) {

				throw new NoLocationFromAddressException("no location found from address " + addressString);
			}
		}
		// else get device's address
		else {
			// try to get geolocation service
			if (isGeolocationCompatible(device)) {
				System.out.println("GetCalidadAire GEO Location COMPATIBLE");
				location = getDeviceGeoLocation(input, input.getRequestEnvelope().getContext());
			} 
			else {
				System.out.println("GetCalidadAire GEO Location NOT COMPATIBLE, trying to get device adrress");
				DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory()
						.getDeviceAddressService();
				String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
				
				
				try{
					Address address = deviceAddressServiceClient.getFullAddress(deviceId);
				
					if (address != null)
						location = getLocationFromAddress(address);
					else {
						this.handleMissingPermissions(input.getResponseBuilder(), ADDRESS_PERM,
							"Dame permisos para saber tu dirección");
					}
				}
				catch(Exception e) {
					
					this.handleMissingPermissions(input.getResponseBuilder(), ADDRESS_PERM,
							"Dame permisos para saber tu dirección");
				}

			}

		}
		return location;

	}

	private LatLng getLocationFromAddress(Address address) throws NoLocationFromAddressException, NoAddressRegistededInDeviceException {

		LatLng locationAddress = null;
		String addressString = null;
		
		System.out.println("TRYING TO GET DEVICE ADDRESS");
		logger.info("Trying to get Device Address");
	
		if (address.getAddressLine1() != null) {
			addressString = address.getAddressLine1();
		} else if (address.getAddressLine2() != null) {
			addressString = address.getAddressLine2();
		} else if (address.getAddressLine3() != null) {
			addressString = address.getAddressLine3() ;
			
		}
		if(address.getPostalCode()!=null) {
			addressString=addressString+", "+address.getPostalCode();
		}
		if(address.getCity()!=null) {
			addressString=addressString+", "+address.getCity();
		}
		if(address.getStateOrRegion()!=null) {
			addressString=addressString+", "+address.getStateOrRegion();
		}
		

		if (addressString != null) {

			System.out.println("READ ADDRESS: " + addressString);

			try {
				locationAddress = monitor.getGeoLocation(addressString);
				saveSessionAttribute("address",addressString);
			} catch (Exception e) {
				throw new NoLocationFromAddressException("No location found from address " + addressString);
			}
			System.out.println("LATLON FROM ADDRESS " +locationAddress.lat + ", " + locationAddress.lng);
			return locationAddress;

		}else {
			throw new NoAddressRegistededInDeviceException("The Address is not registered in the Alexa device");
		}
		
	}

	private LatLng getDeviceGeoLocation(HandlerInput input, Context context) {
		System.out.println("TRYING TO GET LOCATION FROM DEVICE GEOLOCATION");

		GeolocationState geolocation = context.getGeolocation();
		if (geolocation != null) {
			Coordinate coordinate = geolocation.getCoordinate();
			System.out.println(
					"Lat:" + coordinate.getLatitudeInDegrees() + " Long:" + coordinate.getLongitudeInDegrees());
			return new LatLng(coordinate.getLatitudeInDegrees(), coordinate.getLongitudeInDegrees());

		} else {
			System.out.println("TRYING TO GET LOCATION PERMISSION");

			this.handleMissingPermissions(input.getResponseBuilder(), GEO_PERM,
					"Dame permisos para saber tu ubicacion");
		}
		return null;

	}

	private Station getNearestStation(LatLng geolocation) {
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

	public boolean wasRecentlyUpdated(String requestTimestamp, Map<String, Object> stationData, int maxHours) {
		
		Map<String, Object> timeMap = (Map<String, Object>) stationData.get("time");
		String stationUpdateDate = (String) timeMap.get("s");
		System.out.println("STATION " + alias + " WAS UPDATED AT " + stationUpdateDate);
		DateTime requestDate = new DateTime(requestTimestamp, DateTimeZone.forID("America/Mexico_City"));

		org.joda.time.format.DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTimeZone timeZone = DateTimeZone.forID("America/Mexico_City");
		DateTime lastStationUpdate = formatter.parseDateTime(stationUpdateDate).withZone(timeZone);
		lastStationUpdate = lastStationUpdate.plusHours(6);
		System.out.println(lastStationUpdate.toString());
		saveSessionAttribute("stationUpdateDate",lastStationUpdate.toString("yyyy-MM-dd HH:mm:ss"));

		lastStationUpdate = lastStationUpdate.plusHours(maxHours);

		System.out.println("Last Update + " + maxHours + " hours " + lastStationUpdate.toString("yyyy-MM-dd HH:mm:ss"));

		System.out.println("Request Date " + requestDate.toString("yyyy-MM-dd HH:mm:ss"));

		if (requestDate.isBefore(lastStationUpdate)) {

			
			return true;
		}

		return false;

	}
	
	private void saveSessionAttribute(String attribute,Object value) {
		sessionAttributes.put(attribute, value);
		
	}
	
	private Object getSessionAttribute(String attribute) {
		return sessionAttributes.get(attribute);
	}
	
	private void clearSessionAttributes() {
		 sessionAttributes.clear();
	}

	public static void main(String[] args) {
		// 2019-02-18 23:23:23
		/*
		 * String date="2019-02-19T05:23:23Z"; DateTimeFormatter formatter =
		 * DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		 * 
		 * 
		 * DateTime dt=new DateTime(date,DateTimeZone.forID("America/Mexico_City"));
		 * //dt.toString("yyyy-MM-dd HH:mm:ss").parse(date,formatter);
		 * System.out.println(dt.toString("yyyy-MM-dd HH:mm:ss"));
		 * //System.out.println(dt.getHourOfDay());
		 * 
		 * //dt=dt.plusHours(2);
		 * //System.out.println(dt.toString("yyyy-MM-dd HH:mm:ss"));
		 * //System.out.println(dt.getHourOfDay());
		 * 
		 * 
		 * String stationDate="2019-02-18 12:00:00"; DateTime lastUpdate =
		 * formatter.parseDateTime(stationDate);
		 * System.out.println(lastUpdate.toString("yyyy-MM-dd HH:mm:ss"));
		 * //System.out.println(lastUpdate.getHourOfDay());
		 */
		GetCalidadDelAireIntentHandler handler = new GetCalidadDelAireIntentHandler();
		//System.out.println(handler.wasRecentlyUpdated(requestTimestamp, stationUpdate, maxHours).wasE("2019-02-20T00:46:25Z", "2019-02-19 17:00:00", 2));
		/*
		* 
		*/
		// lastUpdate.isA

		/*
		 * long startTime = System.nanoTime(); GetCalidadDelAireIntentHandler handler =
		 * new GetCalidadDelAireIntentHandler(); String addressStr =
		 * "Paseo Vilacova 195, 45645, Guadalajara, Jalisco"; String speechText = null;
		 * 
		 * LatLng geolocation; try { geolocation =
		 * handler.monitor.getGeoLocation(addressStr); Station station =
		 * handler.getNearestStation(addressStr);
		 * 
		 * double meters = handler.monitor.distance(geolocation.lat, geolocation.lng,
		 * Double.valueOf(station.getLatitude()),
		 * Double.valueOf(station.getLongitude()), 0, 0); boolean isStationNear = false;
		 * if (meters < 15000) { isStationNear = true; } if (isStationNear) { String
		 * alias = station.getAlias(); String city = station.getCity(); String
		 * mentionCity = "de " + city; if
		 * (alias.toUpperCase().equals(city.toUpperCase())) { mentionCity = ""; }
		 * Map<String, Object> stationData = handler.getCalidadDelAire(station);
		 * Map<String, Object> timeMap = (Map<String, Object>) stationData.get("time");
		 * 
		 * String dateStr = (String) timeMap.get("s"); System.out.println(dateStr);
		 * 
		 * Date lastUpdate = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss").parse(dateStr);
		 * Calendar cal = Calendar.getInstance(); cal.setTime(lastUpdate); LocalDate
		 * localDate = LocalDate.now(); int hour = cal.get(Calendar.HOUR); //
		 * localDate.get speechText = speechText + " a la hora " + hour + " ";
		 * System.out.println(speechText); double aqiValue = Math.round((Double)
		 * stationData.get("aqi")); int aqi = (int) aqiValue; if (aqi < 50) { speechText
		 * = "La calidad del aire en la estación de monitoreo " + alias + " " +
		 * mentionCity + " es buena, solo " + aqi + " puntos AQI de contaminación"; } if
		 * (aqi >= 50 && aqi < 100) { speechText =
		 * "La calidad del aire en la estación de monitoreo " + alias + " " +
		 * mentionCity + " es moderadamente buena, actualmente hay " + aqi +
		 * " puntos AQI de contaminación"; } if (aqi > 100 && aqi < 150) { speechText =
		 * "La calidad del aire en la estación de monitoreo " + alias + " " +
		 * mentionCity + " es mala, actualmente hay " + aqi +
		 * " puntos AQI de contaminación"; } if (aqi >= 150 && aqi < 200) { speechText =
		 * "hay mucha contaminación cerca de " + addressStr + ", actualmente hay " + aqi
		 * + " puntos AQI de contaminación. Evita el ejercicio al aire libre"; } if (aqi
		 * >= 200) { speechText = "La contaminación del aire en " + addressStr +
		 * " es extredamente alta, con " + aqi +
		 * " puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario"
		 * ; } } else {
		 * 
		 * speechText = "No hay ninguna estación de monitoreo cerca de tu ubicación"; }
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } System.out.println(speechText); long endTime =
		 * System.nanoTime(); System.out.println("Duración: " + (endTime - startTime) /
		 * 1e6 + " ms");
		 */
	}

	private Optional<Response> handleMissingPermissions(ResponseBuilder respBuilder, String permission, String speech) {
		permissions.add(permission);

		// LOGGER.info("Missing permissions " + permission);
		return respBuilder.withAskForPermissionsConsentCard(permissions).withSpeech(speech).withShouldEndSession(true)
				.build();
	}

	private boolean isGeolocationCompatible(Device device) {

		return device.getSupportedInterfaces().getGeolocation() != null;
	}

}

package com.jorgegarcia.alexa.skills.handlers;

import java.util.Arrays;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.geolocation.Coordinate;
import com.amazon.ask.model.interfaces.geolocation.GeolocationState;
import com.amazon.ask.request.Predicates;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.Messages;
import com.jorgegarcia.airvisual.model.StringsESES;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class LaunchRequestHandler implements RequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(LaunchRequestHandler.class);

	private final String[] PERMISSIONS = { "read::alexa:device:all:address", "alexa::devices:all:geolocation:read" };

	public boolean canHandle(HandlerInput input) {
		return input.matches(Predicates.requestType(LaunchRequest.class));
	}

	public Optional<Response> handle(HandlerInput input) {
		LOGGER.info("Address obtained from device successfully.");
		Messages messages = null;
    	RequestEnvelope envelope = input.getRequestEnvelope();
		Request request = (Request) input.getRequestEnvelope().getRequest();
    	Device device = envelope.getContext().getSystem().getDevice();
		String locale=request.getLocale().toLowerCase();
		if(locale.equals("es-mx")) {
			messages=new StringsESMX();
		}
		else if(locale.equals("es-es")) {
			messages=new StringsESES();
			
		}

		String speechText = messages.getRandomMessage(MessageType.LAUNCH);
	

		if (isGeolocationCompatible(device)) {

			System.out.println("Geolocation Compatible");
			/*
			 * GeolocationState geolocation =
			 * input.getRequestEnvelope().getContext().getGeolocation(); if (geolocation !=
			 * null) { Coordinate coordinate = geolocation.getCoordinate();
			 * 
			 * System.out.println("Lat: "+coordinate.getLatitudeInDegrees()+" Long:"
			 * +coordinate.getLongitudeInDegrees());
			 * 
			 * 
			 * if(coordinate != null) { GetCalidadDelAireIntentHandler getAireHandler=new
			 * GetCalidadDelAireIntentHandler(); getAireHandler.handle(input);
			 * //input.getResponseBuilder()..getRequestEnvelope(). //return
			 * input.getRequest()..matches(Predicates.requestType(LaunchRequest.class)); }
			 * 
			 * }
			 * 
			 * return null;
			 * 
			 * } else { System.out.println("Geolocation NOT Compatible");
			 * 
			 * /*return input.getResponseBuilder()
			 * .withSpeech(StringsESMX.getRandonMessage(MessageType.NO_GEOLOCATION))
			 * .withAskForPermissionsConsentCard(Arrays.asList(PERMISSIONS)) .build();
			 * 
			 * /*} else {
			 */
		}
		return input.getResponseBuilder().withSpeech(speechText)
				//.withSimpleCard("Monitor Ambiental", "Puedes preguntarme: ¿Que tanta contaminación hay?")
				.withReprompt(speechText).build();
		// }*/
	}

	private boolean isGeolocationCompatible(Device device) {
		return device.getSupportedInterfaces().getGeolocation() != null;
	}

}
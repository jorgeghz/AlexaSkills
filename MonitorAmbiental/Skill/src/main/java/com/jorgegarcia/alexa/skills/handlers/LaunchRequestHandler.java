package com.jorgegarcia.alexa.skills.handlers;

import java.util.Arrays;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.geolocation.Coordinate;
import com.amazon.ask.model.interfaces.geolocation.GeolocationState;
import com.amazon.ask.request.Predicates;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class LaunchRequestHandler implements RequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(LaunchRequestHandler.class);
	
	private   final String[] PERMISSIONS = {"read::alexa:device:all:address","alexa::devices:all:geolocation:read"};

    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    public Optional<Response> handle(HandlerInput input) {
    	LOGGER.info("Address obtained from device successfully.");
		
        String speechText = StringsESMX.getRandonMessage(MessageType.LAUNCH);
        Device device=input.getRequestEnvelope().getContext().getSystem().getDevice();
        
        
        
        if(isGeolocationCompatible(device)) {
        	GeolocationState geolocation = input.getRequestEnvelope().getContext().getGeolocation();
    		if (geolocation != null) {
    			Coordinate coordinate = geolocation.getCoordinate();
    			
    			
    			if(coordinate != null) {
    				GetCalidadDelAireIntentHandler getAireHandler=new GetCalidadDelAireIntentHandler();
    				getAireHandler.handle(input);
    				//input.getResponseBuilder()..getRequestEnvelope().
    				//return input.getRequest()..matches(Predicates.requestType(LaunchRequest.class));    				
    			}
    			
    		}
        	
        }
        return input.getResponseBuilder()
                .withSpeech(StringsESMX.getRandonMessage(MessageType.NO_GEOLOCATION))
                .withAskForPermissionsConsentCard(Arrays.asList(PERMISSIONS))
                .build();
       
        /*}  
        else 
        	{
        	return input.getResponseBuilder()
        	    .withSpeech(speechText)
                .withSimpleCard("Monitor Ambiental", "Puedes preguntarme: ¿Que tanta contaminación hay?")
                .withReprompt(speechText)
                .build();
        	//}*/
    }
    
    private boolean isGeolocationCompatible(Device device) {
		return device.getSupportedInterfaces().getGeolocation() != null;
	}

}
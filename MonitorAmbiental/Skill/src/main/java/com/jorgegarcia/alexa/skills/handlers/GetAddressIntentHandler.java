package com.jorgegarcia.alexa.skills.handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;

public class GetAddressIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		 return input.matches(Predicates.intentName("GetAddressIntent"));
		 
	}

	public Optional<Response> handle(HandlerInput input) {
		String speechText = "¡no la se!";
        DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
        
        if(deviceAddressServiceClient!=null) {
	        String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
	        if(deviceId!=null) {
	        
		        Address address = deviceAddressServiceClient.getFullAddress(deviceId);
		        if(address!=null) {
		        	speechText=address.getAddressLine1();
		        }
	        }
        }  
        
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Calidad del Aire", speechText)
                .build();
	}

}

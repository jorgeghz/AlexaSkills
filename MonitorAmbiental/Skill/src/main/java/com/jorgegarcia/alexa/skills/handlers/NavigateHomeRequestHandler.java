package com.jorgegarcia.alexa.skills.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.Messages;
import com.jorgegarcia.airvisual.model.StringsESES;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class NavigateHomeRequestHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NavigateHomeIntent"));
    }

    public Optional<Response> handle(HandlerInput input) {
    	
    	Messages messages = null;
    	Request request = (Request) input.getRequestEnvelope().getRequest();

		String locale=request.getLocale().toLowerCase();
		
		if(locale.equals("es-mx")) {
			messages=new StringsESMX();
		}
		else if(locale.equals("es-es")) {
			messages=new StringsESES();
			
		}
    	String speechText = messages.getRandomMessage(MessageType.LAUNCH);
		return input.getResponseBuilder()
				.withSpeech(speechText)
				//.withReprompt(speechText)
				.withShouldEndSession(false)
				.build();
    }

}

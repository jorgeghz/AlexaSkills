package com.jorgegarcia.alexa.skills.handlers;

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

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    public Optional<Response> handle(HandlerInput input) {
    	Messages messages = null;
    	RequestEnvelope envelope = input.getRequestEnvelope();
		Request request = (Request) input.getRequestEnvelope().getRequest();
    	IntentRequest intentRequest = (IntentRequest) request;
		String locale=request.getLocale().toLowerCase();
		if(locale.equals("es-mx")) {
			messages=new StringsESMX();
		}
		else if(locale.equals("es-es")) {
			messages=new StringsESES();
			
		}
        String speechText = messages.getRandomMessage(MessageType.NO_INTENT_RECOGNIZED);
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(speechText)
                .build();
    }
}

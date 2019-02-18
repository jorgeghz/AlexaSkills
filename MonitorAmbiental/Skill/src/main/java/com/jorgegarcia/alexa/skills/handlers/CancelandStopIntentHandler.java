package com.jorgegarcia.alexa.skills.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.StringsESMX;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

public class CancelandStopIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent").or(intentName("AMAZON.CancelIntent")));
    }

    public Optional<Response> handle(HandlerInput input) {
    	String speechText=StringsESMX.getRandonMessage(MessageType.GOODBYE);
    	
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Monitor Ambiental", "¡Bye!")
                .build();
    }
}
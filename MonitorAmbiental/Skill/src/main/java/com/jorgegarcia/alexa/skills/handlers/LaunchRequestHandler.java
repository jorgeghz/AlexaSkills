package com.jorgegarcia.alexa.skills.handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class LaunchRequestHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = StringsESMX.getRandonMessage(MessageType.LAUNCH);
        return input.getResponseBuilder()
                .withSpeech(speechText)
                
                .withSimpleCard("Monitor Ambiental", speechText)
                .withReprompt(speechText)
                .build();
    }

}
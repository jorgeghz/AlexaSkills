package com.jorgegarcia.alexa.skills.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.StringsESMX;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class FallbackIntentHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = StringsESMX.getRandonMessage(MessageType.NO_INTENT_RECOGNIZED);
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(speechText)
                .build();
    }
}

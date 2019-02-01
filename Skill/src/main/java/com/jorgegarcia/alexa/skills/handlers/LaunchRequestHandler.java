package com.jorgegarcia.alexa.skills.handlers;

import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

public class LaunchRequestHandler implements RequestHandler {

    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Hola, soy el Monitor Ambiental ¿Quieres saber la calidad del aire de tu entorno? Yo te lo puedo decir si me preguntas: ¿Cuál es la calidad del aire?";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                
                .withSimpleCard("Monitor Ambiental", speechText)
                .withReprompt(speechText)
                .build();
    }

}
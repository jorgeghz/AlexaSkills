package com.jorgegarcia.alexa.skills.handlers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Device;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.MessageType;
import com.jorgegarcia.airvisual.model.Messages;
import com.jorgegarcia.airvisual.model.StringsESES;
import com.jorgegarcia.airvisual.model.StringsESMX;

public class GetDetailsIntentHandler implements RequestHandler {
	MonitorAmbientalClient monitor;
	Map<String, Object> sessionAttributes;

	public GetDetailsIntentHandler() {
		monitor = new MonitorAmbientalClient();
	}

	public boolean canHandle(HandlerInput input) {
		return input.matches(Predicates.intentName("GetDetailsIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		Messages messages = null;
		RequestEnvelope envelope = input.getRequestEnvelope();
		Request request = (Request) input.getRequestEnvelope().getRequest();
		IntentRequest intentRequest = (IntentRequest) request;

		String locale = request.getLocale().toLowerCase();
		if (locale.equals("es-mx")) {
			messages = new StringsESMX();
		} else if (locale.equals("es-es")) {
			messages = new StringsESES();

		}

		String speechText;
		String cardText;
		sessionAttributes = input.getAttributesManager().getSessionAttributes();
		Map<String, Object> stationData = (Map<String, Object>) sessionAttributes.get("stationData");

		Map<String, Object> dataDetails;
		if (stationData == null) {
			sessionAttributes.clear();
			return input.getResponseBuilder().withSpeech(
					"Aún no me haz preguntrado por la calidad del aire. Que tal si intentas diciendo: ¿Cúal es la calidad del aire?")
					.withShouldEndSession(false).build();

		}

		try {
			dataDetails = fillStationDetails(stationData);

		} catch (NullPointerException e) {
			return input.getResponseBuilder().withSpeech(messages.getRandomMessage(MessageType.ERROR))
					.withShouldEndSession(true).build();

		}
		ArrayList<String> speechaAndCardText = getSpeechAndCardText(dataDetails);
		speechText = speechaAndCardText.get(0);
		cardText = speechaAndCardText.get(1);

		String station = (String) sessionAttributes.get("alias");
		clearSessionAttributes();

		return input.getResponseBuilder().withSimpleCard("Estación de Monitoreo " + station, cardText)
				.withSpeech(speechText).withReprompt(messages.getRandomMessage(MessageType.REPROMPT_SOMETHING_ELSE))
				.withShouldEndSession(true).build();

	}

	private ArrayList<String> getSpeechAndCardText(Map<String, Object> dataDetails) {

		String speech;
		String card;
		ArrayList<String> speechAndCard = new ArrayList<String>();

		String contaminante = "";
		String dominantPollulant = (String) dataDetails.get("dominentPollulant");

		if (dominantPollulant.equals("co")) {
			contaminante = "es el monóxido de carbono";
		} else if (dominantPollulant.equals("o3")) {
			contaminante = "es el ozono";
		} else if (dominantPollulant.equals("so2")) {
			contaminante = "es el dióxido de Azufre";
		} else if (dominantPollulant.equals("no2")) {
			contaminante = "es el dióxido de Nitrogeno";
		} else if (dominantPollulant.equals("pm10")) {
			contaminante = "son las partículas sólidas";
		} else if (dominantPollulant.equals("pm25")) {
			contaminante = "son las partículas suspendidas";
		}

		double distance = Math.round((Double) sessionAttributes.get("distance"));
		int aqi = (Integer) sessionAttributes.get("aqi");
		String alias = (String) sessionAttributes.get("alias");
		String address = (String) sessionAttributes.get("address");
		if (address == null) {

		}

		distance = distance / 1000;
		DecimalFormat decimalFormat = new DecimalFormat("##.0");
		String dateStr = (String) sessionAttributes.get("stationUpdateDate");

		String dateAndTime[] = dateStr.split(" ");

		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		System.out.println(dateStr);
		int temperature = (int) Math.round((Double) dataDetails.get("temperature"));
		int humidity = (int) Math.round((Double) dataDetails.get("humidity"));
		int windSpeed = (int) Math.round((Double) dataDetails.get("windSpeed"));

		// temperature t, wind speeh (w), (p) presion atmosferica en mb, humedad (h)
		if (address != null) {
			if (address.contains(",")) {
				int separator = address.indexOf(",");
				address = address.substring(0, separator);
			}
		}

		String sayAsDate = "<say-as interpret-as=\"date\">" + dateAndTime[0] + "</say-as>";
		String sayAsTime = "<say-as interpret-as=\"time\">" + dateAndTime[1].substring(0, 5) + "</say-as>";

		speech = "La estación de monitoreo " + sessionAttributes.get("alias") + ", ";
		if (address != null)
			speech = speech + " ubicada  a " + decimalFormat.format(distance) + " Kilómetros de " + address;
		else
			speech = speech + " ubicada a " + decimalFormat.format(distance) + " Kilómetros de distancia ";

		speech = speech + ", registró " + aqi + " puntos AQI de contaminación a " + sayAsTime + " horas.";
		// + " el día "+sayAsDate+" a "+sayAsTime+" horas.";

		speech = speech + " El contaminante dominante " + contaminante + ".";
		if (temperature != -1000)
			speech = speech + " Hay una temperatura de " + temperature + " grados.";
		if (humidity != -1000)
			speech = speech + " Una humedad relativa de " + humidity + " por ciento.";
		if (windSpeed != -1000)
			speech = speech + " Con una velocidad del viento de " + windSpeed + " kilometros por hora";

		card = "Ultima actualizacion " + dateStr + "\n" + aqi + " Puntos AQI\n" + "Mayor contaminante "
				+ dominantPollulant.toUpperCase() + "\n";
		if (temperature != -1000)
			card = card + "Temperatura " + temperature + " °C\n";
		if (humidity != -1000)
			card = card + "Humedad Relativa " + humidity + " %\n";
		if (windSpeed != -1000)
			card = card + "Viento " + windSpeed + " km/h";

		speechAndCard.add(speech);
		speechAndCard.add(card);
		return speechAndCard;
	}

	private Map<String, Object> fillStationDetails(Map<String, Object> stationData) {

		double windSpeed = -1000;
		double humidity = -1000;
		double temperature = -1000;
		double airPressure = -1000;
		Map<String, Object> details = new HashMap<String, Object>();

		Map<String, Object> iAqi = (Map<String, Object>) stationData.get("iaqi");
		Map<String, Object> humidityMap = (Map<String, Object>) iAqi.get("h");
		Map<String, Object> temperatureMap = (Map<String, Object>) iAqi.get("t");
		Map<String, Object> windSpeedMap = (Map<String, Object>) iAqi.get("w");
		Map<String, Object> pressionMap = (Map<String, Object>) iAqi.get("p");

		String dominentPollulant = (String) stationData.get("dominentpol");

		if (humidityMap != null)
			humidity = (Double) humidityMap.get("v");
		if (temperatureMap != null)
			temperature = (Double) temperatureMap.get("v");
		if (windSpeedMap != null)
			windSpeed = (Double) windSpeedMap.get("v");
		if (pressionMap != null)
			airPressure = (Double) pressionMap.get("v");

		details.put("dominentPollulant", dominentPollulant);
		details.put("humidity", humidity);
		details.put("temperature", temperature);
		details.put("windSpeed", windSpeed);
		details.put("airPressure", airPressure);

		return details;
	}

	public static void main(String[] args) {
		long startTime = System.nanoTime();
		GetDetailsIntentHandler handler = new GetDetailsIntentHandler();

		System.out.println("");
		long endTime = System.nanoTime();
		System.out.println("Duración: " + (endTime - startTime) / 1e6 + " ms");

	}

	private void clearSessionAttributes() {
		sessionAttributes.clear();
	}

}

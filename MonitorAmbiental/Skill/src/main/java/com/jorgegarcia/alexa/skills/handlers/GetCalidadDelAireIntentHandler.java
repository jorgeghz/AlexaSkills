package com.jorgegarcia.alexa.skills.handlers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.deviceAddress.Address;
import com.amazon.ask.model.services.deviceAddress.DeviceAddressServiceClient;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.jorgegarcia.airvisual.client.MonitorAmbientalClient;
import com.jorgegarcia.airvisual.model.StationResultJSON;
import com.jorgegarcia.airvisual.model.WaqiStation;

public class GetCalidadDelAireIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		
		 return input.matches(Predicates.intentName("GetCalidadDelAireIntent"));
		 
	}

	public Optional<Response> handle(HandlerInput input) {
		String speechText = "No la se";
        DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
        String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
        Address address = deviceAddressServiceClient.getFullAddress(deviceId);
        String addressLine1 = null;
        addressLine1=address.getAddressLine1();
        if(addressLine1==null) {
        	
        	addressLine1="Rosa Navarro 579, Guadalajara";
       	 
        }
        
        try {
        	WaqiStation station=getNearestStation(addressLine1);
        	String alias=station.getAlias();
        	String city=station.getCity();
        	
			Map<String, Object> stationData = getCalidadDelAire(station);
			double aqiValue=  Math.round((Double) stationData.get("aqi"));
			int aqi=(int) aqiValue;
			if(aqi<50) {
				//speechText="La calidad del aire en "+addressLine1+" es buena, solo "+aqi+" puntos AQI de contaminación"; 
				speechText="La calidad del aire en la estación de monitoreo "+alias+" de "+city+" es buena, solo "+aqi+" puntos AQI de contaminación"; 
			
			}
			if(aqi>=50&&aqi<100) {
				//speechText="La calidad del aire en "+addressLine1+" es moderadamente buena, actualmente hay "+aqi+" puntos AQI de contaminación"; 
				speechText="La calidad del aire en la estación de monitoreo "+alias+" de "+city+" es moderadamente buena, actualmente hay "+aqi+" puntos AQI de contaminación";
			}
			if(aqi>100&&aqi<150) {
				//speechText="La calidad del aire en "+addressLine1+" es mala, actualmente hay "+aqi+" puntos AQI de contaminación";
				speechText="La calidad del aire en la estación de monitoreo "+alias+" de "+city+" es mala, actualmente hay "+aqi+" puntos AQI de contaminación"; 
			}
			if(aqi>=150&&aqi<200) {
				speechText="hay mucha contaminación cerca de "+addressLine1+", actualmente hay "+aqi+" puntos AQI de contaminación. Evita el ejercicio al aire libre"; 
			}
			if(aqi>=200) {
				speechText=" La contaminación del aire en en "+addressLine1+" es extredamente alta, con "+aqi+" puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario"; 
			}
			
			//speechText="La calidad del aire en "+addressLine1+" es de "+aqi+" puntos AQI";
			System.out.println(speechText);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Calidad del Aire", speechText)
                .build();
	}
	
	private WaqiStation getNearestStation(String addressLine1) throws ApiException, InterruptedException, IOException {
		MonitorAmbientalClient monitor = new MonitorAmbientalClient();
		LatLng geolocation=monitor.getGeoLocation(addressLine1);
		WaqiStation station= monitor.getNearestStation(geolocation.lat,geolocation.lng);
		return station;
	}
	
	private Map<String, Object> getCalidadDelAire(WaqiStation station) throws ApiException, InterruptedException, IOException {
		MonitorAmbientalClient monitor = new MonitorAmbientalClient();
				
		StationResultJSON latestResult=monitor.getLatestStationResult(station);
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		Gson gson=new Gson();
		Map<String, Object> stationMap = gson.fromJson(latestResult.getJson(), type);
		Map<String, Object> map = (Map<String, Object>) stationMap.get("data");
		Map<String, Object> stationData = map;
		return stationData;
		
	}
	public static void main(String[] args) {
		long startTime = System.nanoTime();
		GetCalidadDelAireIntentHandler handler=new GetCalidadDelAireIntentHandler();
		String speechText = "¡Obligame Perro!";
	       // DeviceAddressServiceClient deviceAddressServiceClient = input.getServiceClientFactory().getDeviceAddressService();
	        //String deviceId = input.getRequestEnvelope().getContext().getSystem().getDevice().getDeviceId();
	        //Address address = deviceAddressServiceClient.getFullAddress(deviceId);
	        String addressLine1 = null;
	        //addressLine1=address.getAddressLine1();
	        //if(addressLine1==null) {
	        	
	        	addressLine1="Rosa Navarro 579, Guadalajara";
	       	 
	        //}
	        
	        try { 
	        	WaqiStation station=handler.getNearestStation(addressLine1);
	        	String alias=station.getAlias();
	        	String city=station.getCity();
	        	
				
				Map<String, Object> stationData = handler.getCalidadDelAire(station);
				double aqiValue=  Math.round((Double) stationData.get("aqi"));
				int aqi=(int) aqiValue;
				if(aqi<50) {
					//speechText="La calidad del aire en "+addressLine1+" es buena, solo "+aqi+" puntos AQI de contaminación"; 
					speechText="La calidad del aire en la estacion "+alias+" de "+city+" es buena, solo "+aqi+" puntos AQI de contaminación"; 
				
				}
				if(aqi>=50&&aqi<100) {
					//speechText="La calidad del aire en "+addressLine1+" es moderadamente buena, actualmente hay "+aqi+" puntos AQI de contaminación"; 
					speechText="La calidad del aire en la estacion "+alias+" de "+city+" es moderadamente buena, actualmente hay "+aqi+" puntos AQI de contaminación";
				}
				if(aqi>100&&aqi<150) {
					//speechText="La calidad del aire en "+addressLine1+" es mala, actualmente hay "+aqi+" puntos AQI de contaminación";
					speechText="La calidad del aire en la estacion de monitoreo "+alias+" de "+city+" es mala, actualmente hay "+aqi+" puntos AQI de contaminación"; 
				}
				if(aqi>=150&&aqi<200) {
					speechText="hay mucha contaminación cerca de "+addressLine1+", actualmente hay "+aqi+" puntos AQI de contaminación. Evita el ejercio al aire libre"; 
				}
				if(aqi>=200) {
					speechText=" La contaminacion del aire en en "+addressLine1+" es extredamente alta, con "+aqi+" puntos AQI. Por tu salud evita salir a la calle o usar tapabocas en caso contrario"; 
				}
				
				//speechText="La calidad del aire en "+addressLine1+" es de "+aqi+" puntos AQI";
				System.out.println(speechText);
				
				//speechText="La calidad del aire en "+addressLine1+" es de "+aqi+" puntos AQI";
				System.out.println(speechText);
				long endTime = System.nanoTime();
				System.out.println("Duración: " + (endTime-startTime)/1e6 + " ms");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}

}

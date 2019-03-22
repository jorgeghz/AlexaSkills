package com.jorgegarcia.airvisual.model;

import java.util.concurrent.ThreadLocalRandom;

public class StringsESES implements Messages {
	
	public  final String[] NO_INTENT_RECOGNIZED= {"Perdona pero eso no lo se, mejor preg�ntame: �Cu�l es la calidad del aire?"};
	public  final String[] EXCELLENT_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">genial</say-as>. La contaminaci�n es m�nima en este momento",
			"<say-as interpret-as=\"interjection\">bazinga</say-as>. Casi no hay contaminaci�n. �Acaso nadie est� usando el autom�vil?",
			"<say-as interpret-as=\"interjection\">guau</say-as>. La calidad del aire es muy muy buena"};
	public  final String[] GOOD_AIR_QUALITY= {
			"<say-as interpret-as=\"interjection\">bien bien</say-as>.  La calidad del aire es buena",
			"<say-as interpret-as=\"interjection\">bazinga</say-as>.  La calidad del aire es buena",
			"<say-as interpret-as=\"interjection\">anda</say-as>.  La calidad del aire es buena",
													"<say-as interpret-as=\"interjection\">fiu</say-as>.  La calidad del aire es buena",
													"<say-as interpret-as=\"interjection\">genial</say-as>.  La calidad del aire es buena"};
	public  final String[] REGULAR_AIR_QUALITY= {};
	public  final String[] BAD_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">ay ay ay</say-as>. La calidad del aire es mala en este momento",
			"<say-as interpret-as=\"interjection\">cielo santo</say-as>. La calidad del aire es mala en este momento",
													
			"<say-as interpret-as=\"interjection\">caramba</say-as>. hay mucha contaminaci�n ahorita",
													"<say-as interpret-as=\"interjection\">caray</say-as>. hay mucha contaminaci�n ahorita",
																									
	"<say-as interpret-as=\"interjection\">puff</say-as>. Hay demasiada contaminaci�n en estos momentos",
"<say-as interpret-as=\"interjection\">diablos</say-as>. Hay demasiada contaminaci�n en estos momentos"};

	
	public  final String[] HAZARD_AIR_QUALITY= {};
	public  final String[] AIR_QUALITY_IN= {"La estaci�n de monitoreo",
												"La contaminaci�n del aire en ",
												"Ahorita la contaminaci�n en "};
	
	public  final String[] HELP= {"Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preg�ntame: �Que tanta contaminaci�n hay?" 
										+ "o tambi�n puedes saber la contaminaci�n de alg�n otro lugar dentro de Espa�a si me preguntas: �Cual es la calidad del aire en? Y despues menciona el nombre de la calle y  ciudad de Espa�a",  
										"Me puedes preguntar cosas como: �Cual es la calidad del aire? o �Que tanta contaminaci�n en? mencionando alguna direccion valida de Espa�a"};
	
	public  final String[] LAUNCH_MESSAGE= {"�Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preguntame: �Que tanta contaminaci�n hay?",
													//"�Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preg�ntame: �Que tanta contaminaci�n hay? "
													//+ "o tambi�n puedes saber la contaminaci�n de alg�n otro lugar dentro de M�xico si me preguntas: �Cual es la calidad del aire en? Y despues me mencionas el nombre de la calle y la ciudad",
													"�Al�!, soy el Monitor Ambiental �Quieres saber la calidad del aire de tu entorno? Tan solo di: �Que tanta contaminaci�n hay?",
													//"<say-as interpret-as=\"interjection\">qu� ondas</say-as>. Monitor Ambiental aqu�. �Como te puedo ayudar?",
													//"<say-as interpret-as=\"interjection\">qu� ondas</say-as>. �Como te puedo ayudar? "
													};
	public  final String[] NO_ADDRESS_IN_DEVICE={"No tienes ninguna direcci�n registrada en tu dispositivo",
								"No tienes la direccion registrada en tu dispositivo Alexa",
								"No me se tu direcci�n de tu dispositivo, por favor registrala",
								"No hay ninguna direccion registrada en tu dispositivo Alexa"};
	public  final String[] NO_GEOLOCATION={"Por favor activa el permiso de localizacion",
			"Para que pueda saber cual es tu ubicaci�n, por favor activa el permiso de localizaci�n",
			"No me se tu direcci�n, por favor reg�strala."
			};
	
	public  final String[] NO_PERMISSIONS={"Por favor activa el permiso de localizaci�n o el de la direcci�n de este dispositivo"};
	
	
	
	
	
	public   String[] NO_NEAR_STATION= {"No hay ninguna estaci�n de monitoreo cerca de ",
								"No hay ninguna estacion de monitoreo cercana a "};
	public  final String[] NO_RECENT_STATION_DATA= {"No hay informaci�n reciente de la estaci�n de monitoreo ",
	"Perdona, no he podido obtener informaci�n reciente de la estaci�n de monitoreo "};
	
	public  final String[] ERROR= {"<say-as interpret-as=\"interjection\">puff</say-as>." + 
										"<prosody rate=\"medium\"> Se cay� el sistema, t�pico. Intentare arreglarlo, por favor vuelve a preguntarme en un ratito</prosody>",
										"<say-as interpret-as=\"interjection\">ay ay ay</say-as>." + 
												"<prosody rate=\"medium\"> Algo fallo. Dame algunos minutos para arreglarlo</prosody>" };							
			
	public  final String[] GOODBYE= {"<say-as interpret-as=\"interjection\">dale</say-as>",
									"<say-as interpret-as=\"interjection\">hasta luego</say-as>",
									"<say-as interpret-as=\"interjection\">hecho</say-as>",
									"<say-as interpret-as=\"interjection\">okey dokey</say-as>",
									"<say-as interpret-as=\"interjection\">au revoir</say-as>"};	
	
	public  final String[] NO_LOCATION_FROM_ADDRESS= {"<say-as interpret-as=\"interjection\">puff</say-as>. Esa no parece ser una direccion valida de Espa�a"};	
	
	public static final String[] GOODBYE2= {"�Adios!",
			"Nos vemos",
			"Que tengas un buen dia",
			"Hasta la vista baby"};			
	
	public  final String[] REPROMPT_SOMETHING_ELSE= {"�Deseas saber la calidad de aire de algun otro lugar?"};
	public  final String[] REPROMPT_GET_DETAILS= {"�Deseas que te diga informaci�n detallada?"};
	

	public  String getRandomMessage(MessageType type) {
		if(type==MessageType.LAUNCH) {
			return LAUNCH_MESSAGE[ThreadLocalRandom.current().nextInt(LAUNCH_MESSAGE.length)];
			
		}
		if(type==MessageType.LAUNCH_SHORT) {
			return LAUNCH_MESSAGE[ThreadLocalRandom.current().nextInt(LAUNCH_MESSAGE.length)];
			
		}
		if(type==MessageType.NO_ADDRESS_IN_DEVICE) {
			return NO_ADDRESS_IN_DEVICE[ThreadLocalRandom.current().nextInt(NO_ADDRESS_IN_DEVICE.length)];
			
		}
		if(type==MessageType.NO_NEAR_STATION) {
			return NO_NEAR_STATION[ThreadLocalRandom.current().nextInt(NO_NEAR_STATION.length)];
			
		}
		if(type==MessageType.ERROR) {
			return ERROR[ThreadLocalRandom.current().nextInt(ERROR.length)];
			
		}
		if(type==MessageType.GOODBYE) {
			return GOODBYE[ThreadLocalRandom.current().nextInt(GOODBYE.length)];
			
		}
		if(type==MessageType.HELP) {
			return HELP[ThreadLocalRandom.current().nextInt(HELP.length)];
			
		}
		if(type==MessageType.NO_INTENT_RECOGNIZED) {
			return NO_INTENT_RECOGNIZED[ThreadLocalRandom.current().nextInt(NO_INTENT_RECOGNIZED.length)];
			
		}
		if(type==MessageType.REPROMPT_GET_DETAILS) {
			return REPROMPT_GET_DETAILS[ThreadLocalRandom.current().nextInt(REPROMPT_GET_DETAILS.length)];
			
		}
		if(type==MessageType.REPROMPT_SOMETHING_ELSE) {
			return REPROMPT_SOMETHING_ELSE[ThreadLocalRandom.current().nextInt(REPROMPT_SOMETHING_ELSE.length)];
			
		}
		
		if(type==MessageType.EXCELLENT_AIR_QUALITY) {
			return EXCELLENT_AIR_QUALITY[ThreadLocalRandom.current().nextInt(EXCELLENT_AIR_QUALITY.length)];
			
		}
		if(type==MessageType.GOOD_AIR_QUALITY) {
			return GOOD_AIR_QUALITY[ThreadLocalRandom.current().nextInt(GOOD_AIR_QUALITY.length)];
			
		}
		if(type==MessageType.REGULAR_AIR_QUALITY) {
			return REGULAR_AIR_QUALITY[ThreadLocalRandom.current().nextInt(REGULAR_AIR_QUALITY.length)];
			
		}
		if(type==MessageType.BAD_AIR_QUALITY) {
			return BAD_AIR_QUALITY[ThreadLocalRandom.current().nextInt(BAD_AIR_QUALITY.length)];
			
		}
		if(type==MessageType.HAZARD_AIR_QUALITY) {
			return HAZARD_AIR_QUALITY[ThreadLocalRandom.current().nextInt(HAZARD_AIR_QUALITY.length)];
			
		}
		if(type==MessageType.AIR_QUALITY_IN) {
			return AIR_QUALITY_IN[ThreadLocalRandom.current().nextInt(AIR_QUALITY_IN.length)];
			
		}
		if(type==MessageType.NO_GEOLOCATION) {
			return NO_GEOLOCATION[ThreadLocalRandom.current().nextInt(NO_GEOLOCATION.length)];
			
		}
		if(type==MessageType.NO_PERMISSIONS) {
			return NO_PERMISSIONS[ThreadLocalRandom.current().nextInt(NO_PERMISSIONS.length)];
			
		}
		if(type==MessageType.NO_RECENT_STATION_DATA) {
			return NO_RECENT_STATION_DATA[ThreadLocalRandom.current().nextInt(NO_RECENT_STATION_DATA.length)];
			
		}
		if(type==MessageType.NO_LOCATION_FROM_ADDRESS) {
			return NO_LOCATION_FROM_ADDRESS[ThreadLocalRandom.current().nextInt(NO_LOCATION_FROM_ADDRESS.length)];
			
		}
		
		
		return null;
	}
	
	public static void main(String[] args) {
		
		
		for(int i=0; i<50;i++) {
		//	int x=ThreadLocalRandom.current().nextInt(NO_ADDRESS_IN_DEVICE.length);
			//System.out.println(x);
			
		}
		
		
	}

}

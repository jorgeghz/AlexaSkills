package com.jorgegarcia.airvisual.model;

import java.util.concurrent.ThreadLocalRandom;

public class StringsESES implements Messages {
	
	public  final String[] NO_INTENT_RECOGNIZED= {"Perdona pero eso no lo se, mejor pregúntame: ¿Cuál es la calidad del aire?"};
	public  final String[] EXCELLENT_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">genial</say-as>. La contaminación es mínima en este momento",
			"<say-as interpret-as=\"interjection\">bazinga</say-as>. Casi no hay contaminación. ¿Acaso nadie está usando el automóvil?",
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
													
			"<say-as interpret-as=\"interjection\">caramba</say-as>. hay mucha contaminación ahorita",
													"<say-as interpret-as=\"interjection\">caray</say-as>. hay mucha contaminación ahorita",
																									
	"<say-as interpret-as=\"interjection\">puff</say-as>. Hay demasiada contaminación en estos momentos",
"<say-as interpret-as=\"interjection\">diablos</say-as>. Hay demasiada contaminación en estos momentos"};

	
	public  final String[] HAZARD_AIR_QUALITY= {};
	public  final String[] AIR_QUALITY_IN= {"La estación de monitoreo",
												"La contaminación del aire en ",
												"Ahorita la contaminación en "};
	
	public  final String[] HELP= {"Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo pregúntame: ¿Que tanta contaminación hay?" 
										+ "o también puedes saber la contaminación de algún otro lugar dentro de España si me preguntas: ¿Cual es la calidad del aire en? Y despues menciona el nombre de la calle y  ciudad de España",  
										"Me puedes preguntar cosas como: ¿Cual es la calidad del aire? o ¿Que tanta contaminación en? mencionando alguna direccion valida de España"};
	
	public  final String[] LAUNCH_MESSAGE= {"¡Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preguntame: ¿Que tanta contaminación hay?",
													//"¡Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo pregúntame: ¿Que tanta contaminación hay? "
													//+ "o también puedes saber la contaminación de algún otro lugar dentro de México si me preguntas: ¿Cual es la calidad del aire en? Y despues me mencionas el nombre de la calle y la ciudad",
													"¡Aló!, soy el Monitor Ambiental ¿Quieres saber la calidad del aire de tu entorno? Tan solo di: ¿Que tanta contaminación hay?",
													//"<say-as interpret-as=\"interjection\">qué ondas</say-as>. Monitor Ambiental aquí. ¿Como te puedo ayudar?",
													//"<say-as interpret-as=\"interjection\">qué ondas</say-as>. ¿Como te puedo ayudar? "
													};
	public  final String[] NO_ADDRESS_IN_DEVICE={"No tienes ninguna dirección registrada en tu dispositivo",
								"No tienes la direccion registrada en tu dispositivo Alexa",
								"No me se tu dirección de tu dispositivo, por favor registrala",
								"No hay ninguna direccion registrada en tu dispositivo Alexa"};
	public  final String[] NO_GEOLOCATION={"Por favor activa el permiso de localizacion",
			"Para que pueda saber cual es tu ubicación, por favor activa el permiso de localización",
			"No me se tu dirección, por favor regístrala."
			};
	
	public  final String[] NO_PERMISSIONS={"Por favor activa el permiso de localización o el de la dirección de este dispositivo"};
	
	
	
	
	
	public   String[] NO_NEAR_STATION= {"No hay ninguna estación de monitoreo cerca de ",
								"No hay ninguna estacion de monitoreo cercana a "};
	public  final String[] NO_RECENT_STATION_DATA= {"No hay información reciente de la estación de monitoreo ",
	"Perdona, no he podido obtener información reciente de la estación de monitoreo "};
	
	public  final String[] ERROR= {"<say-as interpret-as=\"interjection\">puff</say-as>." + 
										"<prosody rate=\"medium\"> Se cayó el sistema, típico. Intentare arreglarlo, por favor vuelve a preguntarme en un ratito</prosody>",
										"<say-as interpret-as=\"interjection\">ay ay ay</say-as>." + 
												"<prosody rate=\"medium\"> Algo fallo. Dame algunos minutos para arreglarlo</prosody>" };							
			
	public  final String[] GOODBYE= {"<say-as interpret-as=\"interjection\">dale</say-as>",
									"<say-as interpret-as=\"interjection\">hasta luego</say-as>",
									"<say-as interpret-as=\"interjection\">hecho</say-as>",
									"<say-as interpret-as=\"interjection\">okey dokey</say-as>",
									"<say-as interpret-as=\"interjection\">au revoir</say-as>"};	
	
	public  final String[] NO_LOCATION_FROM_ADDRESS= {"<say-as interpret-as=\"interjection\">puff</say-as>. Esa no parece ser una direccion valida de España"};	
	
	public static final String[] GOODBYE2= {"¡Adios!",
			"Nos vemos",
			"Que tengas un buen dia",
			"Hasta la vista baby"};			
	
	public  final String[] REPROMPT_SOMETHING_ELSE= {"¿Deseas saber la calidad de aire de algun otro lugar?"};
	public  final String[] REPROMPT_GET_DETAILS= {"¿Deseas que te diga información detallada?"};
	

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

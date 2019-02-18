package com.jorgegarcia.airvisual.model;

import java.util.concurrent.ThreadLocalRandom;

public class StringsESMX {
	
	public static final String[] NO_INTENT_RECOGNIZED= {"Perdona pero eso no lo se, mejor preg�ntame: �Cu�l es la calidad del aire?"};
	public static final String[] EXCELLENT_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">chulada</say-as>. La contaminacion es m�nima en este momento",
			"<say-as interpret-as=\"interjection\">chulada</say-as>. Casi no hay contaminaci�n. �Acaso nadie esta usando el autom�vil?",
			"<say-as interpret-as=\"interjection\">fabuloso</say-as> La calidad del aire es muy muy buena"};
	public static final String[] GOOD_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">ande</say-as>.  La calidad del aire es buena",
													"<say-as interpret-as=\"interjection\">chido</say-as>.  La calidad del aire es buena",
													"<say-as interpret-as=\"interjection\">chulada</say-as>.  La calidad del aire es buena"};
	public static final String[] REGULAR_AIR_QUALITY= {};
	public static final String[] BAD_AIR_QUALITY= {"<say-as interpret-as=\"interjection\">chale</say-as>. La calidad del aire es mala en este momento",
													"<say-as interpret-as=\"interjection\">ay no</say-as>. hay mucha contaminaci�n ahorita",
													"<say-as interpret-as=\"interjection\">puff</say-as>. Hay demasiada contaminaci�n en estos momentos"};
	public static final String[] HAZARD_AIR_QUALITY= {};
	public static final String[] AIR_QUALITY_IN= {"La estaci�n de monitoreo",
												"La contaminaci�n del aire en ",
												"Ahorita la contaminaci�n en "};
	
	public static final String[] HELP= {"�Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preg�ntame: �Que tanta contaminaci�n hay?" 
										+ "o tambi�n puedes saber la contaminaci�n de alg�n otro lugar dentro de M�xico si me preguntas: �Cual es la calidad del aire en? Y despues mencionas el nombre de la calle y la ciudad",  
										"Me puedes preguntar cosas como: �Cual es la calidad del aire? o �Que tanta contaminaci�n en? mencionando alguna direccion y ciudad"};
	
	public static final String[] LAUNCH_MESSAGE= {"�Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preguntame: �Que tanta contaminaci�n hay?",
													"�Hola! Monitor Ambiental te ayuda a saber la calidad del aire en tiempo real, solo preg�ntame: �Que tanta contaminaci�n hay? "
													+ "o tambi�n puedes saber la contaminaci�n de alg�n otro lugar dentro de M�xico si me preguntas: �Cual es la calidad del aire en? Y despues me mencionas el nombre de la calle y la ciudad",
													"�Al�!, soy el Monitor Ambiental �Quieres saber la calidad del aire de tu entorno? Yo te lo puedo decir si me preguntas: �Que tanta contaminaci�n hay?",
													"<say-as interpret-as=\"interjection\">qu� ondas</say-as>. Monitor Ambiental aqu�. �Como te puedo ayudar?",
													"<say-as interpret-as=\"interjection\">qu� ondas</say-as>. �Como te puedo ayudar? "
													};
	public static final String[] NO_ADDRESS={"No tienes ninguna direcci�n registrada en tu dispositivo",
								"No tienes la direccion registrada en tu dispositivo Alexa",
								"No me se tu direcci�n, por favor registrala",
								"No hay ninguna direccion registrada en tu dispositivo Alexa"};
	public static final String[] NO_GEOLOCATION={"Por favor activa el permiso de localizacion",
			"Para que pueda saber cual es tu ubicacion, por favor activa el permiso de localizaci�n",
			"No me se tu direcci�n, por favor registrala"
			};
	
	public static final String[] NO_PERMISSIONS={"Por favor activa el permiso de localizacion o el de la direcci�n de este dispositivo"};
	
	
	
	
	
	public static final String[] NO_NEAR_STATION= {"No hay ninguna estaci�n de monitoreo cerca de ",
								"No hay ninguna estacion de monitoreo cercana a "};
	
	public static final String[] ERROR= {"<say-as interpret-as=\"interjection\">puff</say-as>." + 
										"<prosody rate=\"medium\">Se cay� el sistema, t�pico. Intentare arreglarlo, por favor vuelve a preguntarme en un ratito</prosody>" };							
			
	public static final String[] GOODBYE= {"<say-as interpret-as=\"interjection\">hasta luego</say-as>",
									"<say-as interpret-as=\"interjection\">hasta pronto</say-as>",
									"<say-as interpret-as=\"interjection\">nos vemos luego</say-as>",
									"<say-as interpret-as=\"interjection\">okey</say-as>",
									"<say-as interpret-as=\"interjection\">que te vaya bien</say-as>",
									"<say-as interpret-as=\"interjection\">bonito d�a</say-as>"};	
	
	
	public static final String[] GOODBYE2= {"�Adios!",
			"Nos vemos",
			"Que tengas un buen dia",
			"Hasta la vista baby"};			
	
	public static final String[] REPROMPT_SOMETHING_ELSE= {"�Deseas saber la calidad de algun otro lugar?"};
	public static final String[] REPROMPT_GET_DETAILS= {"�Deseas que te diga informacion detallada?"};
	

	public static String getRandonMessage(MessageType type) {
		if(type==MessageType.LAUNCH) {
			return LAUNCH_MESSAGE[ThreadLocalRandom.current().nextInt(LAUNCH_MESSAGE.length)];
			
		}
		if(type==MessageType.LAUNCH_SHORT) {
			return LAUNCH_MESSAGE[ThreadLocalRandom.current().nextInt(LAUNCH_MESSAGE.length)];
			
		}
		if(type==MessageType.NO_ADDRESS) {
			return NO_ADDRESS[ThreadLocalRandom.current().nextInt(NO_ADDRESS.length)];
			
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
		
		
		return null;
	}
	
	public static void main(String[] args) {
		
		
		for(int i=0; i<50;i++) {
			int x=ThreadLocalRandom.current().nextInt(NO_ADDRESS.length);
			System.out.println(x);
			
		}
		
		
	}

}

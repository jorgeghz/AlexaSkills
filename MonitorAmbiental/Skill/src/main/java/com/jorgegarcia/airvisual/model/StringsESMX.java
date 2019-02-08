package com.jorgegarcia.airvisual.model;

import java.util.concurrent.ThreadLocalRandom;

public class StringsESMX {
	
	public static final String[] EXCELLENT_AIR_QUALITY= {"Que bonito es lo bonito, la contaminacion es minima"};
	public static final String[] GOOD_AIR_QUALITY= {};
	public static final String[] REGULAR_AIR_QUALITY= {};
	public static final String[] BAD_AIR_QUALITY= {};
	public static final String[] HAZARD_AIR_QUALITY= {};
	public static final String[] AIR_QUALITY_IN= {"La calidad del aire en la estación de monitoreo",
												"La contaminación del aire en ",
												"Actualmente la contaminación en "};
	
	public static final String[] LAUNCH_MESSAGE= {"Hola, soy el Monitor Ambiental ¿Quieres saber la calidad del aire de tu entorno? Yo te lo puedo decir si me preguntas: ¿Cuál es la calidad del aire",
													"Bienvenido al Monitor Ambiental",
													};
	public static final String[] NO_ADDRESS={"No tienes ninguna dirección registrada en tu dispositivo",
								"No tienes la direccion registrada en tu dispositivo Alexa",
								"No me se tu dirección, por favor registrala",
								"No hay ninguna direccion registrada en tu dispositivo Alexa"};
	public static final String[] NO_NEAR_STATION= {"No hay ninguna estación de monitoreo cerca de tu ubicación",
								"No hay ninguna estacion de monitoreo cercana a ti, lo siento"};
	
	public static final String[] ERROR= {"¡Oops! Se cayo el sistema, tipico ¿no?. Intentare arreglarlo, por favor vuelve a preguntarme mas tarde"};							
			
	public static final String[] GOODBYE= {"¡Adios!",
									"Nos vemos",
									"Que tengas un buen dia",
									"Hasta la vista baby"};							
	

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
		return null;
	}
	
	public static void main(String[] args) {
		
		
		for(int i=0; i<50;i++) {
			int x=ThreadLocalRandom.current().nextInt(NO_ADDRESS.length);
			System.out.println(x);
			
		}
		
		
	}

}

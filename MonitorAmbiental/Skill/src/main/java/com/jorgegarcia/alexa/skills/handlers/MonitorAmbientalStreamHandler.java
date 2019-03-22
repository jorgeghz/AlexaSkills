package com.jorgegarcia.alexa.skills.handlers;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;

 public class MonitorAmbientalStreamHandler extends SkillStreamHandler {
	 
     private static Skill getSkill() {
         return Skills.standard()
                 .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new GetCalidadDelAireIntentHandler(),
                        new GetDetailsIntentHandler(),
                        new HelpIntentHandler(),
                        new NavigateHomeRequestHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                 .build();
     }

     public MonitorAmbientalStreamHandler() {
         super(getSkill());
     }
     
    

 }
package com.jorgegarcia.alexa.skills.handlers;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;


import com.jorgegarcia.alexa.skills.handlers.*;

 public class HelloWorldStreamHandler extends SkillStreamHandler {
	 
     private static Skill getSkill() {
         return Skills.standard()
                 .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new GetAddressIntentHandler(),
                        new GetCalidadDelAireIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                 .build();
     }

     public HelloWorldStreamHandler() {
         super(getSkill());
     }
     
    

 }
/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.amazon.ask.colorpicker.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.colorpicker.handlers.WhatsMyColorIntentHandler.NAME;
import static com.amazon.ask.colorpicker.handlers.WhatsMyColorIntentHandler.NAME_KEY;
import static com.amazon.ask.request.Predicates.intentName;

public class MyColorIsIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("MyColorIsIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot firstNameSlot = slots.get(NAME);

        String speechText, repromptText;
        boolean isAskResponse = false;

        // Check for favorite color and create output to user.
        if (firstNameSlot != null) {
            // Store the user's favorite color in the Session and create response.
            String firstName = firstNameSlot.getValue();
            input.getAttributesManager().setSessionAttributes(Collections.singletonMap(NAME_KEY, firstName));

            speechText =
                    String.format("<span>Thanks %s. pleasure to have you here.</span> <span>am your virtual help desk, you can ask me things like </span>"
                            + "what can i explore in hotel premise?" +  "tell me about the hotel premise" + "do we have swimming/fitness/ within hotel premise"
                            		+ "", firstName);
            repromptText = speechText;
                    

        } else {
            // Render an error since we don't know what the users favorite color is.
            speechText = "I'm not sure what your name is, please try again";
            repromptText =
                    "i did not quiet get that. You can tell me your name"
                            + "by saying, my name is joy";
            isAskResponse = true;
        }

        ResponseBuilder responseBuilder = input.getResponseBuilder();

        responseBuilder.withSimpleCard("Hotel Premise", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false);

        if (isAskResponse) {
            responseBuilder.withShouldEndSession(false)
                    .withReprompt(repromptText);
        }

        return responseBuilder.build();
    }

}

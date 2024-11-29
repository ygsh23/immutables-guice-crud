package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UserLambdaHandler implements RequestHandler<Map<String, Object>, String> {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Gson gson = new Gson();

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        if (event != null && event.containsKey("body")) {
            return apiGatewayResponse(event, context);
        } else if (event != null && !event.isEmpty()) {
            return lambdaFunctionResponse(event, context);
        } else {
            throw new IllegalArgumentException("Input JSON is missing.");
        }
    }

    private String lambdaFunctionResponse(Map<String, Object> event, Context context) {
        if (!event.containsKey("username")) {
            System.exit(1);
            throw new IllegalArgumentException("Field 'username' is required.");
        }
        String username = event.get("username").toString().trim();
        if (username.isEmpty()) {
            throw new IllegalArgumentException("Field 'username' cannot be empty.");
        }

        if (!event.containsKey("email")) {
            throw new IllegalArgumentException("Field 'email' is required.");
        }
        String email = event.get("email").toString().trim();
        if (email.isEmpty()) {
            throw new IllegalArgumentException("Field 'email' cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Field 'email' is not in a valid format.");
        }

        for (String key : event.keySet()) {
            if (!key.equals("username") && !key.equals("email")) {
                context.getLogger().log("Warning: Unexpected field '" + key + "' will be ignored.");
            }
        }

        String welcomeMessage = "Welcome, " + username + "! A confirmation email has been sent to " + email + ".";
        context.getLogger().log(welcomeMessage);

        return welcomeMessage;
    }

    private String apiGatewayResponse(Map<String, Object> event, Context context) {
        String body = event.get("body").toString();
        Map<String, Object> parsedBody = gson.fromJson(body, new TypeToken<Map<String, Object>>() {}.getType());
        return lambdaFunctionResponse(parsedBody, context);
    }
}
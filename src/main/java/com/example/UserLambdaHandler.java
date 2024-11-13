package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Pattern;

public class UserLambdaHandler implements RequestHandler<Map<String, Object>, String> {
    private static final int TIMEOUT_SECONDS = 1;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @Override
    public String handleRequest(Map<String, Object> event, Context context) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Callable<String> task = () -> {
            try {
                return processRequest(event, context);
            } catch (IllegalArgumentException e) {
                return errorResponse(e.getMessage());
            }
        };

        Future<String> future = executorService.submit(task);
        String result;

        try {
            result = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            result = errorResponse("The request timed out. Please try again later.");
            context.getLogger().log("TimeoutException: " + e.getMessage());
        } catch (Exception e) {
            result = errorResponse("An unexpected error occurred.");
            context.getLogger().log("Exception: " + e.getMessage());
        } finally {
            executorService.shutdown();
        }

        return result;
    }

    private String processRequest(Map<String, Object> event, Context context) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Error: Interrupted exception during processing.");
        }

        if (event == null || event.isEmpty()) {
            throw new IllegalArgumentException("Input JSON is missing.");
        }

        if (!event.containsKey("username")) {
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

    private String errorResponse(String message) {
        return "Error: " + message;
    }
}
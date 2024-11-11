package com.example;

import java.util.Map;

public class ApiGatewayResponse {

    private final int statusCode;
    private final Map<String, String> headers;
    private final Object body;

    private ApiGatewayResponse(int statusCode, Map<String, String> headers, Object body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Object getBody() {
        return body;
    }

    public static class Builder {
        private int statusCode;
        private Map<String, String> headers;
        private Object body;

        public Builder setStatusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setObjectBody(Object body) {
            this.body = body;
            return this;
        }

        public ApiGatewayResponse build() {
            return new ApiGatewayResponse(statusCode, headers, body);
        }
    }
}
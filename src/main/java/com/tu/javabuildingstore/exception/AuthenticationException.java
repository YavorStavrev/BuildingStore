package com.tu.javabuildingstore.exception;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

import java.util.Map;

public class AuthenticationException extends RuntimeException implements ErrorResponse {
    private final ProblemDetail body;
    private final HttpStatus status;

    public AuthenticationException(String message, String username) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, getMessage());
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setProperties(Map.of(
                "username", username
        ));

        this.body = problemDetail;
    }

    @Override
    @NonNull
    public HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    @NonNull
    public ProblemDetail getBody() {
        return body;
    }
}

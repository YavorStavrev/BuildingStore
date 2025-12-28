package com.tu.javabuildingstore.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches the Custom Not Found Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ex.getBody();
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        return problemDetail;
    }

    // For @Valid (@RequestBody requestDTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleRequestObjectValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "One or more fields are invalid");
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setTitle("Request Validation Error");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    // For @Validate (@PathVariable and @RequestParam)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleRequestVariableValidation(ConstraintViolationException ex,
                                                         HttpServletRequest req) {

        ProblemDetail pd = ProblemDetail.forStatus(400);
        pd.setTitle("Constraint violation");
        pd.setDetail("Invalid request parameters");
        pd.setInstance(URI.create(req.getRequestURI()));

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(
                v -> errors.put(v.getPropertyPath().toString(), v.getMessage())
        );

        pd.setProperty("errors", errors);
        return pd;
    }
}

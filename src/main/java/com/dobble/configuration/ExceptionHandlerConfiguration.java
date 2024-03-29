package com.dobble.configuration;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

/**
 * This advice is necessary because MockMvc is not a real servlet environment,
 * therefore it does not redirect error
 * responses to [ErrorController], which produces validation response. So we
 * need to fake it in tests.
 * It's not ideal, but at least we can use classic MockMvc tests for testing
 * error response + document it.
 */
@ControllerAdvice
public class ExceptionHandlerConfiguration {
    private final ObjectMapper objectMapper;

    public ExceptionHandlerConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.objectMapper.setSerializationInclusion(Include.ALWAYS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, HttpServletResponse response) throws IOException {
        ex.printStackTrace();
        String jsonBody = objectMapper.writeValueAsString(Map.of("data", ex.getLocalizedMessage()));
        return new ResponseEntity<String>(jsonBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

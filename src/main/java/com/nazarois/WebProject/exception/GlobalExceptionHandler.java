package com.nazarois.WebProject.exception;

import com.nazarois.WebProject.dto.ExceptionResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    private ErrorAttributes errorAttributes;

    @ExceptionHandler()
    public final ResponseEntity<?> handleBadRequest(WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));

        log.warn(exceptionResponse.getMessage(), exceptionResponse);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler({SecurityException.class})
    public final ResponseEntity<?> handleForbiddenRequest(WebRequest request){
        ExceptionResponse exceptionResponse = new ExceptionResponse(getErrorAttributes(request));

        log.warn(exceptionResponse.getMessage(), exceptionResponse);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exceptionResponse);
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest) {
        return new HashMap<>(errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE)));
    }
}

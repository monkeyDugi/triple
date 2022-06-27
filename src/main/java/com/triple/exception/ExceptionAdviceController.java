package com.triple.exception;

import com.triple.web.dto.ApiResponse;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;

@RestControllerAdvice
public class ExceptionAdviceController {
    private final MessageSource messageSource;

    public ExceptionAdviceController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handlerBusinessException(BusinessException e) {
        ExceptionResponse exceptionResponse = ExceptionResponse.createBusiness(e.getExceptionCode());

        return ResponseEntity.status(exceptionResponse.getStatus()).body(ApiResponse.fail(exceptionResponse));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<ApiResponse<Void>> handlerMethodArgumentNotValidException(BindException e) {
        ValidationResult validationResult = new ValidationResult(e, messageSource, Locale.KOREA);
        FieldErrorDetail fieldErrorDetail = validationResult.getErrorDetail();
        ExceptionResponse exceptionResponse = ExceptionResponse.createBind(fieldErrorDetail);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.fail(exceptionResponse));
    }
}

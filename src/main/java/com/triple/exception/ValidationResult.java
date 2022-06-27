package com.triple.exception;

import org.springframework.context.MessageSource;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ValidationResult {
    private final List<FieldErrorDetail> errorDetails;

    public ValidationResult(Errors errors, MessageSource messageSource, Locale locale) {
        this.errorDetails = errors.getFieldErrors().stream()
                .map(fieldError -> new FieldErrorDetail(fieldError, messageSource, locale))
                .collect(Collectors.toList());
    }

    public FieldErrorDetail getErrorDetail() {
        return errorDetails.get(0);
    }
}

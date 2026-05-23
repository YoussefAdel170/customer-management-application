package com.yadel.customerclient.ui.validation;

public class EmailValidator {
    public static ValidationResult validate(String email) {
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.error("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ValidationResult.error("Invalid email format (e.g., name@domain.com)");
        }
        return ValidationResult.success();
    }
}
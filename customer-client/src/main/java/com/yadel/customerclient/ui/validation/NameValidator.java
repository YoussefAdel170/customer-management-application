package com.yadel.customerclient.ui.validation;

public class NameValidator {
    public static ValidationResult validate(String name) {
        if (name == null || name.trim().isEmpty()) {
            return ValidationResult.error("Name is required");
        }
        return ValidationResult.success();
    }
}
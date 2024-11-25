package com.web.aldalu.aldalu.utils.validators;

import com.web.aldalu.aldalu.utils.PasswordConstraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<PasswordConstraint, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext arg1) {
        if (password == null) {
            return false;
        }    
        return password.length() >= 8 && password.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$");
    }   
    
}

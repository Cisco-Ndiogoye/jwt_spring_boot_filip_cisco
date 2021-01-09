package com.cisco.jwt_spring_boot.entities.validator;

import com.cisco.jwt_spring_boot.entities.request.RegisterForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        RegisterForm user = (RegisterForm) obj;
        return user.getPassword().equals(user.getRepassword());
    }

}

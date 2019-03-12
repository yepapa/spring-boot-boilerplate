package com.yethree.springbootboilerplate.model;

import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonTest {

    @Test
    void testValidator() {
        Person person = Person.builder()
            .name("테스터")
            .age(-1)
            .build();

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Person>> validate = validator.validate(person);

        validate.stream().forEach(violation -> {
            assertEquals("반드시 10보다 같거나 커야 합니다.", violation.getMessage());
        });
    }
}

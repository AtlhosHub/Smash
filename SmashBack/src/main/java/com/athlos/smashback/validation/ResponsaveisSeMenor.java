package com.athlos.smashback.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ResponsaveisSeMenorValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponsaveisSeMenor {
    String message() default "Alunos menores de idade devem ter pelo menos um responsável.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

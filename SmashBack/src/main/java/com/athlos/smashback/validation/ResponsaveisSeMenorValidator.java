package com.athlos.smashback.validation;

import com.athlos.smashback.model.Aluno;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ResponsaveisSeMenorValidator implements ConstraintValidator<ResponsaveisSeMenor, Aluno> {

    @Override
    public boolean isValid(Aluno aluno, ConstraintValidatorContext context) {
        if (aluno == null || aluno.getDataNascimento() == null) return true;

        int idade = Period.between(aluno.getDataNascimento(), LocalDate.now()).getYears();
        List<?> responsaveis = aluno.getResponsaveis();

        if (idade < 18 && (responsaveis == null || responsaveis.isEmpty())) {
            return false;
        }

        return true;
    }
}

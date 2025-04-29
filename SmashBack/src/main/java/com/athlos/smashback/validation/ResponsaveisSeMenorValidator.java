package com.athlos.smashback.validation;

import com.athlos.smashback.exception.InvalidDataException;
import com.athlos.smashback.model.Aluno;
import com.athlos.smashback.model.Responsavel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class ResponsaveisSeMenorValidator implements ConstraintValidator<ResponsaveisSeMenor, Aluno> {

    @Override
    public boolean isValid(Aluno aluno, ConstraintValidatorContext context) {
        if (aluno == null || aluno.getDataNascimento() == null) return true;

        List<?> responsaveis = aluno.getResponsaveis();

        if (aluno.isMenor() && (responsaveis == null || responsaveis.isEmpty())) {
            throw new InvalidDataException("O aluno menor de idade deve ter pelo menos um responsável");
        }

        if (!aluno.isMenor() && (aluno.getEmail() == null || aluno.getEmail().isEmpty())) {
            throw new InvalidDataException("O aluno maior de idade deve ter um e-mail");
        }

        return true;
    }
}

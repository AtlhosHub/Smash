package com.athlos.smashback.validation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {
    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if(cpf == null || cpf.isEmpty() || cpf.length() != 11 || !cpf.matches("[0-9]{11}") || cpf.matches("(\\d)\\1{10}")) return false;

        int soma = 0;
        int peso = 10;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
        }

        int digito1 = 11 - (soma % 11);
        if (digito1 == 10 || digito1 == 11) digito1 = 0;

        soma = 0;
        peso = 11;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
        }

        int digito2 = 11 - (soma % 11);
        if (digito2 == 10 || digito2 == 11) digito2 = 0;

        return digito1 == Character.getNumericValue(cpf.charAt(9)) && digito2 == Character.getNumericValue(cpf.charAt(10));
    }
}

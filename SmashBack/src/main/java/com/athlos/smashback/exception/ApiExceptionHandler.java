package com.athlos.smashback.exception;

import com.athlos.smashback.dto.ErrosDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrosDTO>> handleValidacao(MethodArgumentNotValidException ex) {
        List<ErrosDTO> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> {
                    String fieldPath = error.getField(); // Ex: "responsaveis[0].nome"
                    String classe;
                    if (fieldPath.contains(".")) {
                        // Pega o nome do objeto antes do primeiro ponto
                        String objeto = fieldPath.substring(0, fieldPath.indexOf('.'));
                        // Transforma para o nome da classe com inicial maiúscula
                        classe = Character.toUpperCase(objeto.charAt(0)) + objeto.substring(1);
                    } else {
                        // Se não for aninhado, usa o nome do objeto raiz
                        classe = Character.toUpperCase(error.getObjectName().charAt(0))
                                + error.getObjectName().substring(1);
                    }
                    // Pega apenas o nome do campo após o último ponto
                    String campo;
                    if (fieldPath.contains(".")) {
                        campo = fieldPath.substring(fieldPath.lastIndexOf('.') + 1);
                    } else {
                        campo = fieldPath;
                    }
                    return new ErrosDTO(
                            classe,
                            campo,
                            error.getDefaultMessage());
                })
                .toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidData(InvalidDataException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<String> handleDataConflict(DataConflictException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(401).body(ex.getMessage());
    }
}

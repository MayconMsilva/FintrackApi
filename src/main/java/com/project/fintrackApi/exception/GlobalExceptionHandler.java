package com.project.fintrackApi.exception;

import com.project.fintrackApi.dto.ErroResponseDTO;
import com.project.fintrackApi.dto.ErroResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 — recurso não encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponseDTO handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return ErroResponseDTO.builder()
                .status(404)
                .erro("Recurso não encontrado")
                .mensagem(ex.getMessage()) // "Usuário não encontrado" vem da exception
                .path(request.getRequestURI()) // URL real da requisição
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 409 — conflito de dados
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResponseDTO handleConflict(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return ErroResponseDTO.builder()
                .status(409)
                .erro("Conflito de dados")
                .mensagem(ex.getMessage()) // "Email já cadastrado" vem da exception
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 400 — campos inválidos (@Valid falhou)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponseDTO handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        // Coleta todos os campos inválidos e suas mensagens
        // { "email": "Email inválido", "senha": "Senha deve ter entre 6 e 20 caracteres" }
        Map<String, String> campos = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        // se o mesmo campo tiver 2 erros, pega o primeiro
                        (existing, replacement) -> existing
                ));

        return ErroResponseDTO.builder()
                .status(400)
                .erro("Dados inválidos")
                .mensagem("Verifique os campos e tente novamente")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .campos(campos) // ← campo extra só para validação
                .build();
    }

    // 500 — erro inesperado — sempre tenha esse handler
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResponseDTO handleGeneric(
            Exception ex,
            HttpServletRequest request) {


        ex.printStackTrace();

        return ErroResponseDTO.builder()
                .status(500)
                .erro("Erro interno")
                .mensagem("Ocorreu um erro inesperado. Tente novamente.")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
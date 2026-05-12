package com.project.fintrackApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

    @NotBlank(message = "Email é Obrigatório")
    @Email(message = "Email Inválido")
    private String email;

    @NotBlank(message = "Senha é Obrigatória")
    private String senha;
}

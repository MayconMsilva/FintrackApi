package com.project.fintrackApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioRequestDTO {


    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email Inválido")
    private String email;

    @NotBlank(message = "A Senha é Obrigatória")
    @Size(min = 6, message = "A Senha deve ter pelo menos 6 caracteres")
    private String senha;
}

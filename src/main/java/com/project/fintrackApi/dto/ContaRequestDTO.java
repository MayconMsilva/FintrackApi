package com.project.fintrackApi.dto;

import com.project.fintrackApi.domain.enums.TipoConta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContaRequestDTO {

    @NotBlank(message = "Nome é Obrigatório")
    @Size(min = 2, max = 100, message = "Nome de ter entre 2 e 100 caracteres")
    private String nome;

    @NotNull(message = "Tipo da Conta é Obrigatório")
    private TipoConta tipoConta;
}

package com.project.fintrackApi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Não serializa campos null no JSON — campos não aparece em erros 404/409
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroResponseDTO {

    private int status;       // ← minúsculo, era maiúsculo no seu código

    private String erro;

    private String mensagem;

    private String path;

    private LocalDateTime timestamp;

    // Só aparece em erros de validação (400)
    // @JsonInclude garante que não aparece nos outros erros
    private Map<String, String> campos;
}
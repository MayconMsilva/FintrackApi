package com.project.fintrackApi.dto;


import com.project.fintrackApi.domain.enums.TipoTransacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransacaoResponseDTO {

    private Long id;
    private BigDecimal valor;
    private TipoTransacao tipoTransacao;
    private String descricao;
    private Long contaId;
    private String nomeConta;
    private Long categoriaId;
    private Long transacaoOrigemId;
    private LocalDateTime dataTransacao;
}

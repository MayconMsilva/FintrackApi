package com.project.fintrackApi.dto;


import com.project.fintrackApi.domain.enums.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransacaoRequestDTO {


    @NotNull(message = "Valor é Obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser Maior que Zero")
    private BigDecimal valor;

    @NotNull(message = "Tipo é Obrigatório")
    private TipoTransacao tipoTransacao;


    private String descricao;

    @NotNull(message = "Conta de Origem é Obrigatório")
    private Long contaOrigemId;

    private Long contaDestinoId;

    private Long categoriaId;
}

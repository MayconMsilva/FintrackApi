package com.project.fintrackApi.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumoFinanceiroDTO {


    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal saldoPeriodo;
    private Long quantidadeTransacoes;
}

package com.project.fintrackApi.domain;


import com.project.fintrackApi.domain.enums.TipoCategoria;
import com.project.fintrackApi.domain.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;


    private LocalDateTime dataTransacao;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "conta_id")
    private Conta conta;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


    private Long transacaoOrigemId;


    @PrePersist
    private void prePersist() {
        this.dataTransacao = LocalDateTime.now();

    }
}

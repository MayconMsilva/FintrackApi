package com.project.fintrackApi.domain;


import com.project.fintrackApi.domain.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Builder.Default
    private BigDecimal saldo = BigDecimal.ZERO;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    private LocalDateTime dataCriacao;


    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "conta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Transacao> transacoes;

    @PrePersist
    private void prePersist() {
        this.dataCriacao = LocalDateTime.now();
        if(this.saldo == null){
            this.saldo = BigDecimal.ZERO;
        }
    }
}

package com.project.fintrackApi.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false, length = 100)
    private String nome;


    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;


    @PrePersist
    private void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}

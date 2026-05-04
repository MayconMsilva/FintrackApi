package com.project.fintrackApi.domain;


import com.project.fintrackApi.domain.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCategoria tipoCategoria;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(length = 50)
    private String icone;



}

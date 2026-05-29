package com.project.fintrackApi.service;


import com.project.fintrackApi.domain.Conta;
import com.project.fintrackApi.domain.Usuario;
import com.project.fintrackApi.dto.ContaRequestDTO;
import com.project.fintrackApi.dto.ContaResponseDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.repository.ContaRepository;
import com.project.fintrackApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContaService {


    private final ContaRepository contaRepository;
    private final UsuarioRepository usuarioRepository;


    private ContaResponseDTO toResponseDTO(Conta conta){
        return ContaResponseDTO.builder()
                .id(conta.getId())
                .nome(conta.getNome())
                .saldo(conta.getSaldo())
                .tipoConta(conta.getTipoConta())
                .dataCriacao(conta.getDataCriacao())
                .build();
    }

    @Transactional
    public ContaResponseDTO criar(ContaRequestDTO dto, String emailUsuario){

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario Não Encontrado"));

        Conta conta = Conta.builder()
                .nome(dto.getNome())
                .tipoConta(dto.getTipoConta())
                .usuario(usuario)
                .build();
        return toResponseDTO(contaRepository.save(conta));

    }


    @Transactional(readOnly = true)
    public List<ContaResponseDTO> listarPorUsuario(String emailUsuario){

        return contaRepository.findByUsuarioEmail(emailUsuario)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResponseDTO buscarPorId(Long id, String emailUsuario){

        return contaRepository.findByIdAndUsuarioEmail(id, emailUsuario)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Conta Não Encontrada"));
    }

    @Transactional
    public void deletar(Long id, String emailUsuario){

        Conta conta = contaRepository.findByIdAndUsuarioEmail(id, emailUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Conta Não Encontrada"));

        contaRepository.delete(conta);
    }
}


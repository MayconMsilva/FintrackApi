package com.project.fintrackApi.service;


import com.project.fintrackApi.domain.Usuario;
import com.project.fintrackApi.dto.UsuarioRequestDTO;
import com.project.fintrackApi.dto.UsuarioResponseDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {


    private final   UsuarioRepository usuarioRepository;




    private UsuarioResponseDTO toResponseDTO(Usuario usuario){
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataCriacao(usuario.getDataCriacao())
                .build();
    }




    @Transactional
    public UsuarioResponseDTO cadastrar (UsuarioRequestDTO dto){

        if(usuarioRepository.existsByEmail(dto.getEmail()))
            throw new ResourceNotFoundException("Email Já Cadastrado");


        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();

        Usuario salvo =usuarioRepository.save(usuario);

        return toResponseDTO(salvo);
    }



    public UsuarioResponseDTO buscarPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário Não Encontrado"));

        return toResponseDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }
}

package com.project.fintrackApi.service;


import com.project.fintrackApi.domain.Usuario;
import com.project.fintrackApi.dto.UsuarioRequestDTO;
import com.project.fintrackApi.dto.UsuarioResponseDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {


    private UsuarioRepository usuarioRepository;

    public Usuario cadastrar (UsuarioRequestDTO dto){
        if(usuarioRepository.existsByEmail(dto.getEmail()))
            throw new ResourceNotFoundException("Email Já Cadastrado");


        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(dto.getSenha())
                .build();

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário Não Encontrado" + id));
    }
}

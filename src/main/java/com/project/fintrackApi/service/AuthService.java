package com.project.fintrackApi.service;

import com.project.fintrackApi.config.security.TokenService;
import com.project.fintrackApi.domain.Usuario;
import com.project.fintrackApi.dto.AuthRequestDTO;
import com.project.fintrackApi.dto.AuthResponseDTO;
import com.project.fintrackApi.exception.ResourceNotFoundException;
import com.project.fintrackApi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthResponseDTO login(AuthRequestDTO dto) {

        // Busca usuário pelo email
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email ou senha inválidos"));

        // Verifica se está ativo
        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        // Compara senha enviada com hash no banco
        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            // Mensagem genérica — nunca diga qual dos dois está errado
            // "Email não encontrado" ajuda atacantes a descobrir emails válidos
            throw new IllegalArgumentException("Email ou senha inválidos");
        }

        // Gera token JWT
        String token = tokenService.gerarToken(usuario);

        return AuthResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .build();
    }
}

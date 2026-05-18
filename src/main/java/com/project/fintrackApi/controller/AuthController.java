package com.project.fintrackApi.controller;


import com.project.fintrackApi.config.security.TokenService;
import com.project.fintrackApi.domain.Usuario;
import com.project.fintrackApi.dto.AuthRequestDTO;
import com.project.fintrackApi.dto.AuthResponseDTO;
import com.project.fintrackApi.repository.UsuarioRepository;
import com.project.fintrackApi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid  @RequestBody AuthRequestDTO dto){

        return ResponseEntity.ok(authService.login(dto));

    }
}

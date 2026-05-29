package com.project.fintrackApi.controller;


import com.project.fintrackApi.dto.ContaRequestDTO;
import com.project.fintrackApi.dto.ContaResponseDTO;
import com.project.fintrackApi.service.ContaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
public class ContaController {


    private final ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criar(@Valid @RequestBody ContaRequestDTO dto,
                                                  @AuthenticationPrincipal UserDetails userDetails){

        ContaResponseDTO response = contaService.criar(dto, userDetails.getUsername());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }


    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> listar(@AuthenticationPrincipal
                                                         UserDetails userDetails){
        return ResponseEntity.ok(contaService.listarPorUsuario(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> buscarPorId(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(contaService.buscarPorId(id, userDetails.getUsername()));
    }


    public ResponseEntity<Void> deletar(@PathVariable Long id, @AuthenticationPrincipal
                                        UserDetails userDetails){
        contaService.deletar(id, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }


}

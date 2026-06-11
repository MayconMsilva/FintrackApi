package com.project.fintrackApi.controller;


import com.project.fintrackApi.dto.ResumoFinanceiroDTO;
import com.project.fintrackApi.dto.TransacaoRequestDTO;
import com.project.fintrackApi.dto.TransacaoResponseDTO;
import com.project.fintrackApi.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {


    private final TransacaoService transacaoService;


    @PostMapping
    public ResponseEntity<TransacaoResponseDTO> criar(@Valid @RequestBody
                                                      TransacaoRequestDTO dto, @AuthenticationPrincipal UserDetails userDetails){
        TransacaoResponseDTO response = transacaoService.criar(dto, userDetails.getUsername());


        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();


        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransacaoResponseDTO>> listarTodas(
            @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok(transacaoService.listarTodas(userDetails.getUsername()));
    }

    @GetMapping("/conta/{contaId}")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorConta(
            @PathVariable Long contaId, @AuthenticationPrincipal UserDetails userDetails){

        return ResponseEntity.ok(transacaoService.listarPorConta(contaId, userDetails.getUsername()));
    }

    @GetMapping("/resumo/{contaId}")
    public ResponseEntity<ResumoFinanceiroDTO> resumo(
            @PathVariable Long contaId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok(transacaoService.resumoPorConta(contaId, userDetails.getUsername(),dataInicio,dataFim));
    }


    public ResponseEntity<Void> cancelar(
            @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){

        transacaoService.cancelar(id, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}

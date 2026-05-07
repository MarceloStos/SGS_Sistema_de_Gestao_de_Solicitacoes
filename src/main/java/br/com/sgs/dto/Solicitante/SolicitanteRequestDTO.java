package br.com.sgs.dto.Solicitante;

import jakarta.validation.constraints.NotBlank;

public record SolicitanteRequestDTO(@NotBlank String nome, @NotBlank String cpfCnpj) {}
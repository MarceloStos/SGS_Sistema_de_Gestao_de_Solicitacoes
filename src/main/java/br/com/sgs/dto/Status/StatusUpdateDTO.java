package br.com.sgs.dto.Status;

import jakarta.validation.constraints.NotBlank;

public record StatusUpdateDTO(@NotBlank String status) {}

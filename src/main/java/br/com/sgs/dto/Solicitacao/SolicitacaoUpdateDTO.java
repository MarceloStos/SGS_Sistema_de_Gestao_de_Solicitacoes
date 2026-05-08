package br.com.sgs.dto.Solicitacao;

import br.com.sgs.model.Categoria;
import br.com.sgs.model.Solicitante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoUpdateDTO(
        @NotNull Solicitante solicitante,
        @NotNull Categoria categoria,
        @NotBlank String descricao,
        @NotNull BigDecimal valor,
        @NotBlank LocalDateTime dataSolicitacao
) {}

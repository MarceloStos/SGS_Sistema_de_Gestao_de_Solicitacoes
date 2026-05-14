package br.com.sgs.dto.Solicitacao;

import br.com.sgs.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SolicitacaoUpdateDTO(
        @NotNull Categoria categoria,
        @NotBlank String descricao,
        @NotNull BigDecimal valor
) {}

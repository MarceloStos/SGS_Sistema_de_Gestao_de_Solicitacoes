package br.com.sgs.dto.Solicitacao;

import br.com.sgs.model.Categoria;
import br.com.sgs.model.Solicitante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SolicitacaoRequestDTO(
        @NotNull Solicitante solicitante,
        @NotNull Categoria categoria,
        @NotBlank String descricao,
        @NotNull BigDecimal valor// O tipo BigDecimal, para valores monetários, é melhor que Double ou Float devido ao erro de arredondamento
) {}
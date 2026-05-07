package br.com.sgs.dto.Solicitacao;

import br.com.sgs.model.Categoria;
import br.com.sgs.model.Solicitacao;
import br.com.sgs.model.Solicitante;
import br.com.sgs.model.StatusSolicitacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SolicitacaoResponseDTO(
        @NotNull Long id,
        @NotNull Solicitante solicitante,
        @NotNull Categoria categoria,
        @NotBlank String descricao,
        @NotNull BigDecimal valor,
        @NotBlank LocalDateTime dataSolicitacao,
        @NotBlank StatusSolicitacao status
) {

    public SolicitacaoResponseDTO(Solicitacao solicitacao) {
        this(
                solicitacao.getId(),
                solicitacao.getSolicitante(),
                solicitacao.getCategoria(),
                solicitacao.getDescricao(),
                solicitacao.getValor(),
                solicitacao.getDataSolicitacao(),
                solicitacao.getStatus()
        );
    }
}

package br.com.sgs.dto.Solicitante;

import br.com.sgs.model.Solicitante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitanteResponseDTO(@NotNull Long id, @NotBlank String nome, @NotBlank String cpfCnpj) {

    public SolicitanteResponseDTO(Solicitante solicitante) {
        this(solicitante.getId(), solicitante.getNome(), solicitante.getCpfCnpj());
    }
}

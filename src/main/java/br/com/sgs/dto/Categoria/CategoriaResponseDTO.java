package br.com.sgs.dto.Categoria;

import br.com.sgs.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaResponseDTO(@NotNull Long id, @NotBlank String nome) {
    public CategoriaResponseDTO(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}
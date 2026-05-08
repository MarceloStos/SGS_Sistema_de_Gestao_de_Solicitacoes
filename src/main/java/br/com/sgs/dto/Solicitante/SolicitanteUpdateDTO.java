package br.com.sgs.dto.Solicitante;

import br.com.sgs.model.Solicitante;
import jakarta.validation.constraints.NotBlank;

public record SolicitanteUpdateDTO(@NotBlank String nome,@NotBlank String cpfCnpj){}

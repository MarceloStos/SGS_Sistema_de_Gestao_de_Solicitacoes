package br.com.sgs.service;


import br.com.sgs.dto.Solicitacao.SolicitacaoResponseDTO;
import br.com.sgs.repository.SolicitacaoRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;


    public  salvar() {}

    public SolicitacaoResponseDTO buscarPorId(Long id) {
        return solicitacaoRepository.buscarPorId(id)
                .map(SolicitacaoResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação " + id + " não encontrada"));
    }

    public  atualizar() {}
}
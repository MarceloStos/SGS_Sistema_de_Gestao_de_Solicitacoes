package br.com.sgs.service;


import br.com.sgs.dto.Solicitacao.SolicitacaoRequestDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoResponseDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoUpdateDTO;
import br.com.sgs.model.Solicitacao;
import br.com.sgs.model.StatusSolicitacao;
import br.com.sgs.repository.SolicitacaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    public List<SolicitacaoResponseDTO> listarComFiltros(String status, Long categoriaId, String dataInicio, String dataFim) {
        return solicitacaoRepository.buscarComFiltros(status, categoriaId, dataInicio, dataFim)
                .stream()
                .map(SolicitacaoResponseDTO::new)
                .toList();
    }

    @Transactional
    public SolicitacaoResponseDTO salvar(SolicitacaoRequestDTO dados) {
        Solicitacao solicitacao = new Solicitacao(
                dados.solicitante(),
                dados.categoria(),
                dados.descricao(),
                dados.valor(),
                LocalDateTime.now(),
                StatusSolicitacao.SOLICITADO
        );
        solicitacaoRepository.salvar(solicitacao);

        return new SolicitacaoResponseDTO(solicitacao);
    }

    public SolicitacaoResponseDTO buscarPorId(Long id) {
        return solicitacaoRepository.buscarPorId(id)
                .map(SolicitacaoResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação " + id + " não encontrada"));
    }

    @Transactional
    public SolicitacaoResponseDTO atualizar(Long id, SolicitacaoUpdateDTO dados) {
        Solicitacao solicitacao = solicitacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Não foi possível atualizar: Solicitação não encontrada"));

        solicitacao.setSolicitante(dados.solicitante());
        solicitacao.setCategoria(dados.categoria());
        solicitacao.setDescricao(dados.descricao());
        solicitacao.setValor(dados.valor());
        solicitacao.setDataSolicitacao(dados.dataSolicitacao());

        solicitacaoRepository.atualizar(solicitacao);

        return new SolicitacaoResponseDTO(solicitacao);
    }
}
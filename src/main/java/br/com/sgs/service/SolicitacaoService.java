package br.com.sgs.service;


import br.com.sgs.dto.Solicitacao.SolicitacaoRequestDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoResponseDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoUpdateDTO;
import br.com.sgs.model.Solicitacao;
import br.com.sgs.model.StatusSolicitacao;
import br.com.sgs.repository.SolicitacaoRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        log.info("Nova solicitação criada com sucesso. ID: {}", solicitacao.getId());
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

    @Transactional
    public SolicitacaoResponseDTO atualizarStatus(Long id, String status) {
        Solicitacao solicitacao = solicitacaoRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Não foi possível alterar o status: Solicitação não encontrada"));

        StatusSolicitacao statusAtual = solicitacao.getStatus();
        StatusSolicitacao novoStatus = StatusSolicitacao.valueOf(status.toUpperCase());

        validarRegraStatus(statusAtual, novoStatus);

        solicitacaoRepository.atualizarStatus(id, novoStatus.name());
        solicitacao.setStatus(novoStatus);

        return new SolicitacaoResponseDTO(solicitacao);
    }

    private void validarRegraStatus(StatusSolicitacao statusAtual, StatusSolicitacao novoStatus) {
        if (statusAtual == StatusSolicitacao.REJEITADO || statusAtual == StatusSolicitacao.CANCELADO) {
            throw new IllegalArgumentException("Status final. A solicitação não pode mais ser alterada.");
        }

        if (statusAtual == StatusSolicitacao.SOLICITADO && novoStatus != StatusSolicitacao.LIBERADO && novoStatus != StatusSolicitacao.REJEITADO) {
            throw new IllegalArgumentException("De SOLICITADO, só é possível mudar para LIBERADO ou REJEITADO.");
        }

        if (statusAtual == StatusSolicitacao.LIBERADO && novoStatus != StatusSolicitacao.APROVADO && novoStatus != StatusSolicitacao.REJEITADO) {
            throw new IllegalArgumentException("De LIBERADO, só é possível mudar para APROVADO ou REJEITADO.");
        }

        if (statusAtual == StatusSolicitacao.APROVADO && novoStatus != StatusSolicitacao.CANCELADO) {
            throw new IllegalArgumentException("De APROVADO, só é possível mudar para CANCELADO.");
        }
    }
}
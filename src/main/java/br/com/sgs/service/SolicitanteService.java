package br.com.sgs.service;

import br.com.sgs.dto.Solicitante.SolicitanteRequestDTO;
import br.com.sgs.dto.Solicitante.SolicitanteResponseDTO;
import br.com.sgs.dto.Solicitante.SolicitanteUpdateDTO;
import br.com.sgs.model.Solicitante;
import br.com.sgs.repository.SolicitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SolicitanteService {

    @Autowired
    private SolicitanteRepository solicitanteRepository;


    @Transactional
    public SolicitanteResponseDTO salvar(SolicitanteRequestDTO dados) {

        if (solicitanteRepository.existeCpfCnpj(dados.cpfCnpj(), null)) {
            throw new IllegalArgumentException("Este CPF/CNPJ já está cadastrado no sistema.");
        }
        Solicitante solicitante = new Solicitante(dados.nome(), dados.cpfCnpj());
        solicitanteRepository.salvar(solicitante);

        return new SolicitanteResponseDTO(solicitante);
    }

    public List<SolicitanteResponseDTO> listarSolicitantes() {
        return solicitanteRepository.buscarTodos()
                .stream()
                .map(SolicitanteResponseDTO::new)
                .toList();
    }

    public SolicitanteResponseDTO buscarPorId(Long id) {

        return solicitanteRepository.buscarPorId(id)
                .map(SolicitanteResponseDTO::new)
                .orElseThrow(() -> new EntityNotFoundException("Solicitante " + id + " não encontrado"));
    }

    @Transactional
    public SolicitanteResponseDTO atualizar(Long id, SolicitanteUpdateDTO dados) {
        Solicitante solicitante = solicitanteRepository.buscarPorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Não foi possível atualizar: Solicitante inexistente"));

        if (solicitanteRepository.existeCpfCnpj(dados.cpfCnpj(), id)) {
            throw new IllegalArgumentException("Este CPF/CNPJ já está cadastrado no sistema.");
        }

        solicitante.setNome(dados.nome());
        solicitante.setCpfCnpj(dados.cpfCnpj());

        solicitanteRepository.atualizar(solicitante);

        return new SolicitanteResponseDTO(solicitante);
    }

}

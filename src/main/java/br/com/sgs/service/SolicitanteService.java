package br.com.sgs.service;

import br.com.sgs.dto.Solicitante.SolicitanteResponseDTO;
import br.com.sgs.repository.SolicitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class SolicitanteService {

    @Autowired
    private SolicitanteRepository solicitanteRepository;


    public  salvar() {}

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


    public atualizar{}

}

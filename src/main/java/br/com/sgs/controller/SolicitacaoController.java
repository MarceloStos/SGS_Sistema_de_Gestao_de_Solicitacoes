package br.com.sgs.controller;

import br.com.sgs.dto.Solicitacao.SolicitacaoRequestDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoResponseDTO;
import br.com.sgs.dto.Solicitacao.SolicitacaoUpdateDTO;
import br.com.sgs.service.SolicitacaoService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDTO>> listar(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim
    ){
        return ResponseEntity.ok(solicitacaoService.listarComFiltros(status, categoriaId, dataInicio, dataFim));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criar(@RequestBody @Valid SolicitacaoRequestDTO dados){
        var solicitacao = solicitacaoService.salvar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitacao);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> visualizar(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid SolicitacaoUpdateDTO dados) {
        var response = solicitacaoService.atualizar(id, dados);
        return ResponseEntity.ok(response);
    }
}

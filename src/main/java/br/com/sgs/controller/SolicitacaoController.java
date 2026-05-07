package br.com.sgs.controller;

import br.com.sgs.dto.Solicitacao.SolicitacaoResponseDTO;
import br.com.sgs.service.SolicitacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public ResponseEntity<> listar(){
        return ResponseEntity.ok());
    }

    @PostMapping
    public ResponseEntity<> criar(){
        return ResponseEntity.status(HttpStatus.CREATED).body();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoResponseDTO> visualizar(@PathVariable Long id) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<> atualizar() {
        return ResponseEntity.ok();
    }
}

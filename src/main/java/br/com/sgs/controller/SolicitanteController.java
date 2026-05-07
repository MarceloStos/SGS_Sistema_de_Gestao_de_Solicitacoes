package br.com.sgs.controller;

import br.com.sgs.dto.Solicitante.SolicitanteResponseDTO;
import br.com.sgs.service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitantes")
public class SolicitanteController {

    @Autowired
    private SolicitanteService solicitanteService;

    @GetMapping
    public ResponseEntity<List<SolicitanteResponseDTO>> listar() {
        return ResponseEntity.ok(solicitanteService.listarSolicitantes());
    }

    @PostMapping
    public ResponseEntity<> criar(){
        return ResponseEntity.status(HttpStatus.CREATED).body();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitanteResponseDTO> visualizar(@PathVariable Long id) {
        return ResponseEntity.ok(solicitanteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<> atualizar() {
        return ResponseEntity.ok();
    }

}

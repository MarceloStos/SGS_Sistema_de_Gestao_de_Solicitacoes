package br.com.sgs.controller;

import br.com.sgs.service.SolicitanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/solicitantes")
public class SolicitanteController {

    @Autowired
    private SolicitanteService solicitanteService;

    @GetMapping
    public ResponseEntity<> listar() {
        return ResponseEntity.ok();
    }

    @PostMapping
    public ResponseEntity<> criar(){
        return ResponseEntity.status(HttpStatus.CREATED).body();
    }

    @GetMapping("/{id}")
    public ResponseEntity<> visualizar() {
        return ResponseEntity.ok();
    }

    @PutMapping("/{id}")
    public ResponseEntity<> atualizar() {
        return ResponseEntity.ok();
    }

}

package br.com.sgs.service;

import br.com.sgs.dto.Categoria.CategoriaResponseDTO;
import br.com.sgs.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.buscarTodos()
                .stream()
                .map(CategoriaResponseDTO::new)
                .toList();
    }
}
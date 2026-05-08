package br.com.sgs.repository;

import br.com.sgs.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoriaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Categoria> buscarTodos() {
        String sql = "SELECT * from categoria ORDER BY id ASC";
        return entityManager.createNativeQuery(sql, Categoria.class).getResultList();
    }
}
package br.com.sgs.repository;

import br.com.sgs.model.Solicitante;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SolicitanteRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Solicitante> buscarTodos () {

        String sql = "SELECT * FROM solicitante s ORDER BY s.nome ASC";
        return entityManager.createNativeQuery(sql, Solicitante.class).getResultList();
    }

    public Optional<Solicitante> buscarPorId (Long id) {

        String sql = "SELECT * FROM solicitante s WHERE s.id = :id";
        try {
            Solicitante solicitante = (Solicitante) entityManager.createNativeQuery(sql, Solicitante.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(solicitante);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void salvar() {
    }

    public void atualizar() {
    }
}

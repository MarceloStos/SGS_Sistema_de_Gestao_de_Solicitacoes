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

    public void salvar(Solicitante solicitante) {
        String sql = "INSERT into solicitante (nome, cpf_cnpj) VALUES (:nome, :cpfCnpj)";
        entityManager.createNativeQuery(sql)
                .setParameter("nome", solicitante.getNome())
                .setParameter("cpfCnpj", solicitante.getCpfCnpj())
                .executeUpdate();
    }

    public void atualizar(Solicitante solicitante) {

        String sql = "UPDATE solicitante set nome = :nome, cpf_cnpj = :cpfCnpj WHERE id = :id";
        entityManager.createNativeQuery(sql)
                .setParameter("nome", solicitante.getNome())
                .setParameter("cpfCnpj", solicitante.getCpfCnpj())
                .setParameter("id", solicitante.getId())
                .executeUpdate();
    }

    public boolean existeCpfCnpj(String cpfCnpj, Long id) {

        String sql = "SELECT COUNT(*) FROM solicitante s WHERE s.cpf_cnpj = :cpfCnpj AND s.id != :id";
        Object result = entityManager.createNativeQuery(sql)
                .setParameter("cpfCnpj", cpfCnpj)
                .setParameter("id", id == null ? -1L : id)
                .getSingleResult();

        return ((Number) result).longValue() > 0;
    }
}

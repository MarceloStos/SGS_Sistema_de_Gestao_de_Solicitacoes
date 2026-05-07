package br.com.sgs.repository;


import br.com.sgs.model.Solicitacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class SolicitacaoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Solicitacao> buscarTodos () {

        String sql = "SELECT * FROM solicitacao s ORDER BY s.data_solicitacao DESC";

        return entityManager.createNativeQuery(sql, Solicitacao.class).getResultList();
    }

    public Optional<Solicitacao> buscarPorId (Long id) {

        String sql = "SELECT * FROM solicitacao s WHERE s.id = :id";
        try {
            Solicitacao solicitacao = (Solicitacao) entityManager.createNativeQuery(sql, Solicitacao.class)
                    .setParameter("id", id)
                    .getSingleResult();

            return Optional.of(solicitacao);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public void salvar() {}

    public void atualizar() {}

}

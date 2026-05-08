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

    public void salvar(Solicitacao solicitacao) {

        String sql = """
            INSERT INTO solicitacao (solicitante_id, categoria_id, descricao, valor, data_solicitacao, status)
            VALUES (:solicitanteId, :categoriaId, :descricao, :valor, :data, :status)
            """;
        entityManager.createNativeQuery(sql)
                .setParameter("solicitanteId", solicitacao.getSolicitante().getId())
                .setParameter("categoriaId", solicitacao.getCategoria().getId())
                .setParameter("descricao", solicitacao.getDescricao())
                .setParameter("valor", solicitacao.getValor())
                .setParameter("data", solicitacao.getDataSolicitacao())
                .setParameter("status", solicitacao.getStatus().name())
                .executeUpdate();
    }

    public void atualizar(Solicitacao solicitacao) {

        String sql = "UPDATE solicitacao s SET solicitante_id = :solicitanteId, categoria_id = :categoriaId, descricao = :descricao, valor = :valor, data_solicitacao = :data, status = :status WHERE id = :id";
        entityManager.createNativeQuery(sql)
                .setParameter("solicitanteId", solicitacao.getSolicitante().getId())
                .setParameter("categoriaId", solicitacao.getCategoria().getId())
                .setParameter("descricao", solicitacao.getDescricao())
                .setParameter("valor", solicitacao.getValor())
                .setParameter("data", solicitacao.getDataSolicitacao())
                .setParameter("status", solicitacao.getStatus().name())
                .executeUpdate();
    }

}

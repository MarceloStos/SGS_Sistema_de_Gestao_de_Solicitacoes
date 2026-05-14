package br.com.sgs.repository;


import br.com.sgs.model.Solicitacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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

    public List<Solicitacao> buscarComFiltros(String status, Long categoriaId, String dataInicio, String dataFim) {

        StringBuilder sql = new StringBuilder(
                "SELECT s.* FROM solicitacao s " +
                        "INNER JOIN solicitante sol ON s.solicitante_id = sol.id " +
                        "INNER JOIN categoria cat ON s.categoria_id = cat.id " +
                        "WHERE 1=1 "
        );

        // Adicionando os filtros
        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND s.status = :status");
        }
        if (categoriaId != null) {
            sql.append(" AND s.categoria_id = :categoriaId");
        }
        if (dataInicio != null && !dataInicio.trim().isEmpty()) {

            sql.append(" AND s.data_solicitacao >= :dataInicio");
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            sql.append(" AND s.data_solicitacao <= :dataFim");
        }

        sql.append(" ORDER BY s.data_solicitacao DESC");

        // Criação a Query
        Query query = entityManager.createNativeQuery(sql.toString(), Solicitacao.class);

        if (status != null && !status.trim().isEmpty()) {
            query.setParameter("status", status);
        }
        if (categoriaId != null) {
            query.setParameter("categoriaId", categoriaId);
        }
        if (dataInicio != null && !dataInicio.trim().isEmpty()) {
            query.setParameter("dataInicio", java.sql.Timestamp.valueOf(dataInicio + " 00:00:00"));
        }
        if (dataFim != null && !dataFim.trim().isEmpty()) {
            query.setParameter("dataFim", java.sql.Timestamp.valueOf(dataFim + " 23:59:59"));
        }

        return query.getResultList();
    }

    public Optional<Solicitacao> buscarPorId (Long id) {

        StringBuilder sql = new StringBuilder(
                "SELECT s.* FROM solicitacao s " +
                        "INNER JOIN solicitante sol ON s.solicitante_id = sol.id "  +
                        "INNER JOIN categoria cat ON s.categoria_id = cat.id "  +
                        "WHERE s.id = :id "
        );
        try {
            Solicitacao solicitacao = (Solicitacao) entityManager.createNativeQuery(sql.toString(), Solicitacao.class)
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

        String sql = "UPDATE solicitacao s SET categoria_id = :categoriaId, descricao = :descricao, valor = :valor WHERE id = :id";
        entityManager.createNativeQuery(sql)
                .setParameter("categoriaId", solicitacao.getCategoria().getId())
                .setParameter("descricao", solicitacao.getDescricao())
                .setParameter("valor", solicitacao.getValor())
                .setParameter("id", solicitacao.getId())
                .executeUpdate();
    }

    public void atualizarStatus(Long id, String status) {
        String sql = "uPDATE solicitacao SET status = :status WHERE id = :id";
        entityManager.createNativeQuery(sql)
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

}

package br.com.sgs.service;

import br.com.sgs.model.Solicitacao; // Ajuste os imports para o seu pacote correto
import br.com.sgs.model.StatusSolicitacao;
import br.com.sgs.repository.SolicitacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito neste teste
class SolicitacaoServiceTest {

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Test
    @DisplayName("Caminho Feliz: Deve permitir transição de SOLICITADO para LIBERADO")
    void devePermitirTransicaoSolicitadoParaLiberado() {
        // 1. Arrange (Prepara os dados)
        Long id = 1L;
        Solicitacao solicitacaoMock = new Solicitacao();
        solicitacaoMock.setId(id);
        solicitacaoMock.setStatus(StatusSolicitacao.SOLICITADO);

        when(solicitacaoRepository.buscarPorId(id)).thenReturn(Optional.of(solicitacaoMock));

        // 2. Act & Assert (Executa a ação e verifica se NENHUMA exceção estourou)
        assertDoesNotThrow(() -> {
            solicitacaoService.atualizarStatus(id, "LIBERADO");
        });

        // 3. Valida se o status dentro do objeto foi realmente atualizado no final do processo
        assertEquals(StatusSolicitacao.LIBERADO, solicitacaoMock.getStatus());
    }

    @Test
    @DisplayName("Regra de Negócio: Deve bloquear transição direta de SOLICITADO para APROVADO")
    void deveBloquearTransicaoSolicitadoParaAprovado() {
        // 1. Arrange
        Long id = 1L;
        Solicitacao solicitacaoMock = new Solicitacao();
        solicitacaoMock.setId(id);
        solicitacaoMock.setStatus(StatusSolicitacao.SOLICITADO);

        when(solicitacaoRepository.buscarPorId(id)).thenReturn(Optional.of(solicitacaoMock));

        // 2. Act & Assert (Executa e garante que a IllegalArgumentException foi lançada)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            solicitacaoService.atualizarStatus(id, "APROVADO");
        });

        // 3. Verifica se a mensagem da exceção esta correta
        assertEquals("De SOLICITADO, só é possível mudar para LIBERADO ou REJEITADO.", exception.getMessage());
    }
}
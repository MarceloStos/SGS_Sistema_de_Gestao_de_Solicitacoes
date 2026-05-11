package br.com.sgs.exception;

import br.com.sgs.dto.Erro.ErrorDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Para tratamento de Unique no banco
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDTO> erroConflitoBanco(DataIntegrityViolationException ex, HttpServletRequest request) {
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflito de Dados",
                "O registro não pode ser salvo pois viola uma restrição única ou de relacionamento no banco de dados.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // Para não encontrado
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> erroNaoEncontrado(EntityNotFoundException ex, HttpServletRequest request) {
        log.warn("Erro ao buscar um(a) solicitante", ex);
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Para regra de negócio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> erroRegraDeNegocio(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Violação de Regra de Negócio",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Para tratamento das validações do DTO (@NotBlank, @NotNull, etc)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> erroValidacaoCampos(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensagens = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                mensagens,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // O erro genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> erroGenerico(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado no sistema: ", ex);
        ErrorDTO error = new ErrorDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno",
                "Ocorreu um erro interno no servidor.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

package com.fag.musicalmodel.config;

import com.fag.musicalmodel.exception.BusinessException;
import com.fag.musicalmodel.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Padroniza as respostas de erro da API:
 * - validação de campos do corpo: 400 {"campo": "mensagem"}
 * - demais erros: {"erro": "mensagem"}
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Extrai o nome da coluna da mensagem do PostgreSQL, ex.: "Key (email)=(x) already exists."
    private static final Pattern COLUNA_DUPLICADA = Pattern.compile("\\((\\w+)\\)=\\(");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Validação de parâmetros de query/path (ex.: volume fora de 0 a 100)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleMethodValidation(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getParameterValidationResults().forEach(result -> {
            String nome = result.getMethodParameter().getParameterName();
            result.getResolvableErrors().forEach(error -> errors.put(nome, error.getDefaultMessage()));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFound(RuntimeException ex) {
        return erro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, String>> handleBusiness(BusinessException ex) {
        return erro(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return erro(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos");
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(DisabledException ex) {
        return erro(HttpStatus.FORBIDDEN, "Usuário desativado");
    }

    // Restrições do banco: valor duplicado, registro em uso ou texto maior que a coluna
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Throwable causa = NestedExceptionUtils.getMostSpecificCause(ex);
        String sqlState = causa instanceof SQLException sql ? sql.getSQLState() : null;
        String mensagem = causa.getMessage() != null ? causa.getMessage() : "";

        if ("23505".equals(sqlState)) {
            Matcher matcher = COLUNA_DUPLICADA.matcher(mensagem);
            String detalhe = matcher.find() ? " para o campo '" + matcher.group(1) + "'" : "";
            return erro(HttpStatus.CONFLICT, "Já existe um registro com este valor" + detalhe);
        }
        if ("23503".equals(sqlState)) {
            return erro(HttpStatus.CONFLICT, "O registro está vinculado a outros registros e não pode ser removido ou alterado");
        }
        if ("22001".equals(sqlState)) {
            return erro(HttpStatus.BAD_REQUEST, "Um dos campos excede o tamanho máximo permitido");
        }
        log.warn("Violação de integridade não mapeada", ex);
        return erro(HttpStatus.CONFLICT, "A operação viola uma restrição dos dados");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleNotReadable(HttpMessageNotReadableException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Corpo da requisição ausente ou com formato inválido");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String mensagem = "Valor inválido para '" + ex.getName() + "': " + ex.getValue();
        Class<?> tipo = ex.getRequiredType();
        if (tipo != null && tipo.isEnum()) {
            mensagem += ". Valores aceitos: " + Arrays.toString(tipo.getEnumConstants());
        }
        return erro(HttpStatus.BAD_REQUEST, mensagem);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParameter(MissingServletRequestParameterException ex) {
        return erro(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente: " + ex.getParameterName());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return erro(HttpStatus.METHOD_NOT_ALLOWED, "Método " + ex.getMethod() + " não suportado nesta rota");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return erro(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Content-Type não suportado. Use application/json");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoResource(NoResourceFoundException ex) {
        return erro(HttpStatus.NOT_FOUND, "Rota não encontrada");
    }

    private ResponseEntity<Map<String, String>> erro(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(Map.of("erro", mensagem));
    }
}

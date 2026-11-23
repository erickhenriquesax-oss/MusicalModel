package com.fag.musicalmodel.config;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Hidden
@RestController
public class ErroController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, String>> erro(HttpServletRequest request) {
        Object codigo = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = codigo instanceof Integer valor && HttpStatus.resolve(valor) != null
                ? HttpStatus.valueOf(valor)
                : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(Map.of("erro", mensagem(status)));
    }

    private String mensagem(HttpStatus status) {
        return switch (status) {
            case BAD_REQUEST -> "Requisição inválida";
            case UNAUTHORIZED -> "Token ausente, inválido ou expirado";
            case FORBIDDEN -> "Acesso negado: você não tem permissão para esta operação";
            case NOT_FOUND -> "Rota não encontrada";
            case METHOD_NOT_ALLOWED -> "Método não suportado nesta rota";
            default -> status.is5xxServerError() ? "Erro interno no servidor" : status.getReasonPhrase();
        };
    }
}

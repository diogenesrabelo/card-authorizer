package br.com.diogenes.card_authorizer.controller.transaction;


import br.com.diogenes.card_authorizer.controller.transaction.dto.Transaction;
import br.com.diogenes.card_authorizer.controller.transaction.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/transactions")
public interface TransactionController {

    @Operation(summary = "Processa uma transação de cartão de crédito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transação processada com sucesso",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{\"code\": \"00\"}"))),
        @ApiResponse(responseCode = "200", description = "Transação rejeitada por saldo insuficiente",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{\"code\": \"51\"}"))),
        @ApiResponse(responseCode = "200", description = "Erro ao processar a transação",
            content = @Content(mediaType = "application/json",
                schema = @Schema(example = "{\"code\": \"07\"}")))
    })
    @PostMapping("/authorize")
    public ResponseEntity<TransactionResponse> authorizeTransaction(@RequestBody Transaction transaction);
}

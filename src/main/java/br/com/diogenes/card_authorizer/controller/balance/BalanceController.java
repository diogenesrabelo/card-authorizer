package br.com.diogenes.card_authorizer.controller.balance;

import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

public interface BalanceController {

    @Operation(summary = "Cria um novo balance", description = "Cria um novo registro de balance para um cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Balance criado com sucesso",
            content = @Content(schema = @Schema(implementation = Balance.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    ResponseEntity<Balance> createBalance(@RequestBody(description = "Dados do balance", required = true) @org.springframework.web.bind.annotation.RequestBody Balance balance);

    @Operation(summary = "Deposita novos valores", description = "Adiciona valores às categorias (meal, cash, food) para um account específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance atualizado com sucesso",
            content = @Content(schema = @Schema(implementation = Balance.class))),
        @ApiResponse(responseCode = "404", description = "Account não encontrado")
    })
    @PutMapping("/{account}/deposit")
    ResponseEntity<Balance> depositToAccount(
        @PathVariable String account,
        @RequestParam BigDecimal meal,
        @RequestParam BigDecimal cash,
        @RequestParam BigDecimal food);
    @Operation(summary = "Retorna todos os balances", description = "Retorna todos os balances com suporte a paginação.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de balances",
            content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    Page<Balance> getAllBalances(Pageable pageable);

    @Operation(summary = "Retorna um balance específico", description = "Retorna o balance de um account específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance encontrado",
            content = @Content(schema = @Schema(implementation = Balance.class))),
        @ApiResponse(responseCode = "404", description = "Account não encontrado")
    })
    @GetMapping("/{account}")
    ResponseEntity<Balance> getBalanceByAccount(@PathVariable String account);
}

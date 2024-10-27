package br.com.diogenes.card_authorizer.controller.balance.impl;

import br.com.diogenes.card_authorizer.controller.balance.BalanceController;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/balance")
public class BalanceControllerImpl implements BalanceController {


    private final BalanceService balanceService;

    public BalanceControllerImpl(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @PostMapping
    public ResponseEntity<Balance> createBalance(@RequestBody Balance balance) {
        Balance createdBalance = balanceService.saveOrUpdateBalance(balance);
        return new ResponseEntity<>(createdBalance, HttpStatus.CREATED);
    }

    @PutMapping("/{account}/deposit")
    public ResponseEntity<Balance> depositToAccount(
        @PathVariable String account,
        @RequestParam BigDecimal meal,
        @RequestParam BigDecimal cash,
        @RequestParam BigDecimal food) {
        Optional<Balance> updatedBalance = balanceService.depositToAccount(account, meal, cash, food);
        return updatedBalance
            .map(balance -> new ResponseEntity<>(balance, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Page<Balance> getAllBalances(Pageable pageable) {
        return balanceService.getAllBalances(pageable);
    }

    @GetMapping("/{account}")
    public ResponseEntity<Balance> getBalanceByAccount(@PathVariable String account) {
        Optional<Balance> balance = balanceService.findBalance(account);
        return balance
            .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}

package br.com.diogenes.card_authorizer.service.balance;

import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface BalanceService {

    Balance saveOrUpdateBalance(Balance balance);

    Optional<Balance> findBalance(String account);

    Optional<Balance> depositToAccount(String account, BigDecimal meal, BigDecimal cash, BigDecimal food);

    Page<Balance> getAllBalances(Pageable pageable);
}

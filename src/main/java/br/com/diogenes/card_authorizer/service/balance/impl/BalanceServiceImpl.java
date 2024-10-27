package br.com.diogenes.card_authorizer.service.balance.impl;

import br.com.diogenes.card_authorizer.repository.balance.BalanceRepository;
import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import br.com.diogenes.card_authorizer.service.balance.BalanceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    public BalanceServiceImpl(final BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    @Transactional
    public Balance saveOrUpdateBalance(Balance balance) {
        return balanceRepository.saveWithLockingPessimistic(balance);
    }

    @Override
    public Optional<Balance> findBalance(String account) {
        return balanceRepository.findById(account);
    }

    public Optional<Balance> depositToAccount(String account, BigDecimal meal, BigDecimal cash, BigDecimal food) {
        Optional<Balance> optionalBalance = balanceRepository.findById(account);
        optionalBalance.ifPresent(balance -> {
            balance.setMeal(balance.getMeal().add(meal));
            balance.setCash(balance.getCash().add(cash));
            balance.setFood(balance.getFood().add(food));
            balanceRepository.saveWithLockingPessimistic(balance);
        });
        return optionalBalance;
    }

    public Page<Balance> getAllBalances(Pageable pageable) {
        return balanceRepository.findAll(pageable);
    }
}

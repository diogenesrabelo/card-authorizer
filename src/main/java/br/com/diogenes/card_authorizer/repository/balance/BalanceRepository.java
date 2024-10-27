package br.com.diogenes.card_authorizer.repository.balance;

import br.com.diogenes.card_authorizer.repository.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, String>, CustomBalanceRepository {
}

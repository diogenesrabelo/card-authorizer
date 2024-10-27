package br.com.diogenes.card_authorizer.controller.transaction.dto;

import java.math.BigDecimal;

public record Transaction(
    String account,
    BigDecimal totalAmount,
    String merchant,
    String mcc
) {

}

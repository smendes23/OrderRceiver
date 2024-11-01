package br.com.ambev.orderreceiver.core.dto;

import java.math.BigDecimal;
import java.util.List;

public record RequestDTO(
        List<ProductRequest> products,
        BigDecimal totalAmount,
        String status,
        String idempotencyKey
) {
    public RequestDTO withStatus(String status){
        return new RequestDTO(products(), BigDecimal.ZERO, status, idempotencyKey());
    }
}
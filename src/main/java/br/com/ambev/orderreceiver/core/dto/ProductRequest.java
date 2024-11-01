package br.com.ambev.orderreceiver.core.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductRequest(
        String name,
        BigDecimal price,
        Integer quantity
) {
}

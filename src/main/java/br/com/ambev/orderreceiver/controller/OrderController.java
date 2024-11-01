package br.com.ambev.orderreceiver.controller;

import br.com.ambev.orderreceiver.core.dto.RequestDTO;
import br.com.ambev.orderreceiver.core.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.of;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer producer;

    @PostMapping
    public ResponseEntity<String> receiveOrder(@RequestBody RequestDTO order) {

        log.info("Receiving order: {}", order);

        return of(order)
                .map(orderRequest -> ResponseEntity.ok(producer.sendOrder(order.withStatus("EM_APROVACAO"))))
                .orElseThrow();
    }
}
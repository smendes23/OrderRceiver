package br.com.ambev.orderreceiver.core.producer.impl;

import br.com.ambev.orderreceiver.core.dto.RequestDTO;
import br.com.ambev.orderreceiver.core.exception.ProducerException;
import br.com.ambev.orderreceiver.core.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProducerImpl  implements OrderProducer {

    private final KafkaTemplate<String, RequestDTO> kafkaTemplate;

    @Value("${kafka.topic}")
    private String topic ;

    public String sendOrder(final RequestDTO order) {
        log.info("Publish order to topic: {}", topic);
        CompletableFuture<SendResult<String, RequestDTO>> future = kafkaTemplate.send(topic, order);

        return future
                .thenApply(result -> result.getProducerRecord().value().toString())
                .exceptionally(e -> {
                    log.error("Error to publish order: {}", order, e);
                    throw new ProducerException(e.getMessage());
                })
                .join();
    }
}

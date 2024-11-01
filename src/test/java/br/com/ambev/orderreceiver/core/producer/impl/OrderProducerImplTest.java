package br.com.ambev.orderreceiver.core.producer.impl;

import br.com.ambev.orderreceiver.core.dto.RequestDTO;
import br.com.ambev.orderreceiver.core.exception.ProducerException;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderProducerImplTest {

    @Mock
    private KafkaTemplate<String, RequestDTO> kafkaTemplate;

    private OrderProducerImpl orderProducer;

    @BeforeEach
    public void setUp() {
        orderProducer = new OrderProducerImpl(kafkaTemplate);
    }

    @Test
    public void testSendOrderWhenKafkaTemplateSucceedsThenReturnExpectedResult() {
        // Arrange
        RequestDTO order = mock(RequestDTO.class);
        SendResult<String, RequestDTO> sendResult = Mockito.mock(SendResult.class);
        when(sendResult.getProducerRecord()).thenReturn(new ProducerRecord<>("topic", order));
        CompletableFuture<SendResult<String, RequestDTO>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(any(String.class), any(RequestDTO.class))).thenReturn(future);

        // Act
        String result = orderProducer.sendOrder(order);

        // Assert
        assertEquals(order.toString(), result);
    }

    @Test
    public void testSendOrderWhenKafkaTemplateFailsThenThrowProducerException() {
        // Arrange
        RequestDTO order = mock(RequestDTO.class);
        CompletableFuture<SendResult<String, RequestDTO>> future = new CompletableFuture<>();
        future.completeExceptionally(new ProducerException("Kafka error"));
        when(kafkaTemplate.send(any(String.class), any(RequestDTO.class))).thenReturn(future);

        // Act & Assert
        assertThrows(ProducerException.class, () -> orderProducer.sendOrder(order));
    }
}

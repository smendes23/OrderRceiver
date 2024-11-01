package br.com.ambev.orderreceiver.core.producer;

import br.com.ambev.orderreceiver.core.dto.RequestDTO;

public interface OrderProducer {

    String sendOrder(RequestDTO order);
}

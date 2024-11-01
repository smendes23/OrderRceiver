package br.com.ambev.orderreceiver.controller;

import br.com.ambev.orderreceiver.core.dto.ProductRequest;
import br.com.ambev.orderreceiver.core.dto.RequestDTO;
import br.com.ambev.orderreceiver.core.producer.OrderProducer;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderProducer orderProducer;

    @Test
    public void testReceiveOrderWhenOrderIsValidThenReturnOk() throws Exception {
        // Arrange

        List<ProductRequest> listaProdutos = List.of(new ProductRequest("Skol", BigDecimal.TWO,6));
        RequestDTO validOrder = new RequestDTO(listaProdutos, BigDecimal.ZERO, "EM_APROVACAO", "54321");

        String expectedResponse = "Order received successfully";
        BDDMockito.given(orderProducer.sendOrder(validOrder)).willReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"products\": [\n" +
                        "        {\n" +
                        "           \"name\": \"Skol\",\n" +
                        "           \"price\": 2.00,\n" +
                        "           \"quantity\": 6\n" +
                        "        }\n" +
                        "    ],\n" +
                        "    \"idempotencyKey\": \"54321\"\n" +
                        "}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedResponse));
    }

    @Test
    public void testReceiveOrderWhenOrderIsInvalidThenReturnBadRequest() throws Exception {
        // Arrange
        String invalidOrderJson = "{\n" +
                "    \"products\": [\n" +
                "        {\n" +
                "           \"name\": \"Skol\",\n" +
                "           \"price\": 2.00,\n" +
                "           \"quantity\": 6\n" +
                "        }\n" +
                "    ],\n" +
                "    \"idempotencyKey\": \"54321\"\n" +
                "}";

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidOrderJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}

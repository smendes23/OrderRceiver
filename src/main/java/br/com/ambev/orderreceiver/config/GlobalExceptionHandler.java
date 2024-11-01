package br.com.ambev.orderreceiver.config;

import br.com.ambev.orderreceiver.core.exception.ProducerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    protected ResponseEntity<Object> producerException(ProducerException ex, WebRequest request){
      log.error("Erro ao publicar mensagem no topico, causa: {}", ex.getMessage());
      return ResponseEntity.unprocessableEntity().build();
    }
}

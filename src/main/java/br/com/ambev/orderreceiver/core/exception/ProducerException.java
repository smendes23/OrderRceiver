package br.com.ambev.orderreceiver.core.exception;

public class ProducerException extends RuntimeException{
    public ProducerException(String mensagem){
        super("Erro ao publicar mensagem! Causa: "+mensagem);
    }
}

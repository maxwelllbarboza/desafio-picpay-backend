package com.maxwell.picpay_desafio_backend.exception;

public class UnauthorizedTransactionException extends RuntimeException{

    public UnauthorizedTransactionException(String message){
        super(message);
    }
}

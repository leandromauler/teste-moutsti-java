package br.com.leandroauler.orders.api.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}

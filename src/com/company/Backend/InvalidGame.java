package com.company.Backend;

public class InvalidGame extends RuntimeException {
    public InvalidGame(String message) {
        super(message);
    }
}

package com.dev.foryourwishes.wishlist.exceptions;

public class WishNotFound extends RuntimeException {
    public WishNotFound(Long wishId) {
        super("Wish not found with id: " + wishId);
    }
}

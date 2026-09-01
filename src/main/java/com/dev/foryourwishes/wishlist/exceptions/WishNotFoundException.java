package com.dev.foryourwishes.wishlist.exceptions;

public class WishNotFoundException extends RuntimeException {
    public WishNotFoundException(Long wishId) {
        super("Wish not found with id: " + wishId);
    }
}

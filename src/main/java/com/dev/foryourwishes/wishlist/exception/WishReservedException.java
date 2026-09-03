package com.dev.foryourwishes.wishlist.exception;

public class WishReservedException extends RuntimeException {
    public WishReservedException(Long userId) {
        super("Wish is reserved for user " + userId);
    }
}

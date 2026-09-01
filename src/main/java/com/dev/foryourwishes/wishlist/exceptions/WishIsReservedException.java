package com.dev.foryourwishes.wishlist.exceptions;

public class WishIsReservedException extends RuntimeException {
    public WishIsReservedException(Long userId) {
        super("Wish is reserved for user " + userId);
    }
}

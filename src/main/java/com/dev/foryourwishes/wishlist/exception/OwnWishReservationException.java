package com.dev.foryourwishes.wishlist.exception;

public class OwnWishReservationException extends RuntimeException {
    public OwnWishReservationException(Long userId) {
        super("User with id = %d cannot reserve own wish".formatted(userId));
    }
}

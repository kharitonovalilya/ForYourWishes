package com.dev.foryourwishes.wishlist.exception;

public class WishlistAccessDeniedException extends RuntimeException {
    public WishlistAccessDeniedException(Long wishlistId, Long userId) {
        super("Access for user with id = %d denied for wishlistId: %d ".formatted(userId, wishlistId));
    }
}

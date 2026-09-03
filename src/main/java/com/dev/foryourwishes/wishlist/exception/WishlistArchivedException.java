package com.dev.foryourwishes.wishlist.exception;

public class WishlistArchivedException extends RuntimeException {
    public WishlistArchivedException(Long wishlistId) {
        super("Wishlist with id = %d archived".formatted(wishlistId));
    }
}

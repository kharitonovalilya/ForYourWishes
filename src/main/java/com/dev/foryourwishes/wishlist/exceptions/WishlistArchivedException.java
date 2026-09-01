package com.dev.foryourwishes.wishlist.exceptions;

public class WishlistArchivedException extends RuntimeException {
    public WishlistArchivedException(Long wishlistId) {
        super("Wishlist with id = %d archived".formatted(wishlistId));
    }
}

package com.dev.foryourwishes.wishlist.exceptions;

public class WishlistNotFoundException extends RuntimeException {
    public WishlistNotFoundException(Long wishlistId) {
        super("Wishlist with id " + wishlistId + " not found");
    }
}

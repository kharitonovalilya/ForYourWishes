package com.dev.foryourwishes.wishlist.exceptions;

public class WishIsFulfilledException extends RuntimeException {
    public WishIsFulfilledException(Long wishId) {
        super(
                "Wish id " + wishId + " is already fulfilled"
        );
    }
}

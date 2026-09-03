package com.dev.foryourwishes.wishlist.exception;

public class WishFulfilledException extends RuntimeException {
    public WishFulfilledException(Long wishId) {
        super(
                "Wish id " + wishId + " is already fulfilled"
        );
    }
}

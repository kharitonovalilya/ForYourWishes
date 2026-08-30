package com.dev.foryourwishes.wishlist.exceptions;

public class WishlistNotFoundException extends RuntimeException {
  public WishlistNotFoundException(String message) {
    super(message);
  }
}

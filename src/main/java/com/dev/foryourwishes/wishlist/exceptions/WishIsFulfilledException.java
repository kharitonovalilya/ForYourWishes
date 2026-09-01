package com.dev.foryourwishes.wishlist.exceptions;

public class WishIsFulfilledException extends RuntimeException {
  public WishIsFulfilledException(String message) {
    super(message);
  }
}

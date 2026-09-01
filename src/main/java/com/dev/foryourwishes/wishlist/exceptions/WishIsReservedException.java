package com.dev.foryourwishes.wishlist.exceptions;

public class WishIsReservedException extends RuntimeException {
  public WishIsReservedException(String message) {
    super(message);
  }
}

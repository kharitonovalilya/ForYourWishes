package com.dev.foryourwishes.wishlist.exceptions;

public class ReservationAccessDeniedException extends RuntimeException {
    public ReservationAccessDeniedException(Long reservationId) {
      super("Conflict with reservation = %d, user can cancel only own reservations".formatted(reservationId));
    }
}

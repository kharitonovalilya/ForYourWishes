package com.dev.foryourwishes.wishlist.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("Reservation with id " + reservationId + " not found");
    }
}
